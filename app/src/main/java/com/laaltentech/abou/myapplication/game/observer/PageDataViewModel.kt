package com.laaltentech.abou.myapplication.game.observer

import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.laaltentech.abou.myapplication.game.data.FacebookPageData
import com.laaltentech.abou.myapplication.game.repository.GameDataRepository
import com.laaltentech.abou.myapplication.network.Resource
import com.laaltentech.abou.myapplication.util.AbsentLiveData
import com.laaltentech.abou.myapplication.util.AppExecutors
import javax.inject.Inject

class PageDataViewModel @Inject constructor(
    private val repository: GameDataRepository,
    var executors: AppExecutors
) : ViewModel(), Observable {

    companion object{
        var pageId : String = ""
        var accessToken: String = ""
        var isInternet = true
    }

    var facebookPageData : FacebookPageData? = FacebookPageData()

    private val callbacks: PropertyChangeRegistry by lazy { PropertyChangeRegistry() }

    val apiCall = MutableLiveData<String>()
    var results: LiveData<Resource<FacebookPageData>>

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        callbacks.remove(callback)    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        callbacks.add(callback)    }

    fun notifyChange() {
        callbacks.notifyCallbacks(this, 0, null)
    }

    init {
        results = Transformations.switchMap(apiCall) {
            when (apiCall.value) {
                "available" -> {
                    repository.fetchPageData(
                        pageID = pageId,
                        accessToken = accessToken,
                        isInternet = isInternet
                    )
                }

                "postData" -> {
                    repository.postPageData(facebookPageData = facebookPageData!!, isInternet = isInternet, pageID = pageId)
                }
                else -> {
                    AbsentLiveData.create()
                }
            }
        }
    }
}