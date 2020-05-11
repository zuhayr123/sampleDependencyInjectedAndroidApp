package com.laaltentech.abou.myapplication.network

import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.laaltentech.abou.myapplication.util.*

abstract class NetworkBoundResource<ResultType, RequestType> @MainThread constructor(
    val appExecutors: AppExecutors
) {

    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        //result.value = Resource.loading(null)
        try {
            val dbSource = loadFromDb()
//        Logger.e(Thread.currentThread(), "Source: $dbSource")
            result.addSource(dbSource) { resultType ->
                result.removeSource(dbSource)
//            Logger.e(Thread.currentThread(), "Remove Source: $dbSource")

//            if (tagHolder.containsTag(uploadTag())) {
//                Logger.e(Thread.currentThread(), "tag exist & breaking now")
//            } else {
                if (shouldFetch(resultType)) {
                    fetchFromNetwork(dbSource)
//                    tagHolder.addTag(uploadTag())
//                Logger.e(Thread.currentThread(), "Remove Network: $dbSource")
                } else {
                    result.addSource(dbSource) { rT ->
                        result.value = Resource.success(rT)
                    }
                }
//            }
            }
        } catch (e: Exception) {
//            Logger.e(Thread.currentThread(), "NetworkBound Resource exception $e")
        }
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        val apiResponse = createCall()
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly

        //TODO comment Logger.d(Thread.currentThread(), "Call: $apiResponse")
        result.addSource(dbSource) { resultType ->
            setValue(Resource.loading(resultType))
        }

//        print("fetch_reponse: $apiResponse")
//        Logger.e(Thread.currentThread(), "fetch_ka_reponse: $apiResponse")

        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            result.removeSource(dbSource)
//            tagHolder.removeTag(uploadTag())

            Log.e("test of return ","Response_of_result ${Gson().toJson(response)}")//todo remove gson

            when (response) {
                is ApiSuccessResponse -> {
                    appExecutors.diskIO().execute {
                        processResponse(response)?.let { saveCallResult(it) }
                        appExecutors.mainThread().execute {
                            // we specially request a new live data,
                            // otherwise we will get immediately last cached value,
                            // which may not be updated with latest results received from network.
                            result.addSource(loadFromDb()) { newData ->
                                setValue(Resource.success(newData))
                            }
                        }
                    }
                }
                is ApiEmptyResponse -> {
                    appExecutors.mainThread().execute {
                        // reload from disk whatever we had
                        result.addSource(loadFromDb()) { newData ->
                            setValue(Resource.success(newData))
                        }
                    }
                }
                is ApiErrorResponse -> {
                    appExecutors.diskIO().execute {
                        var value = false
                        if (response.code == 409) value = onDuplicateEntry()
                        if (!value) onFetchFailed()

                        appExecutors.mainThread().execute {
                            result.addSource(dbSource) { newData ->
                                if (value) setValue(Resource.success(newData))
                                else setValue(Resource.error(response.errorMessage, newData, response.code))
                            }
                        }
                    }
                }
            }
        }
    }

    protected open fun onFetchFailed() {}

    fun asLiveData(): LiveData<Resource<ResultType>> {
        return result
    }

    @WorkerThread
    private fun processResponse(response: ApiSuccessResponse<RequestType>): RequestType? {
        return response.body
    }

    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>

    @MainThread
    protected abstract fun uploadTag(): String?

    @WorkerThread
    protected open fun onDuplicateEntry(): Boolean = false
}