package com.laaltentech.abou.myapplication.di.module

import android.app.Application
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.laaltentech.abou.myapplication.database.MasterDatabase
import com.laaltentech.abou.myapplication.di.WebService
import com.laaltentech.abou.myapplication.factory.LiveDataCallAdapterFactory
import com.laaltentech.abou.myapplication.util.URL_HUB.Companion.BASE_URL
import com.laaltentech.abou.myapplication.util.WebServiceHolder
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class DaoModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): MasterDatabase {
        return Room.databaseBuilder(application,
            MasterDatabase::class.java, "masterDatabase.db")
            .fallbackToDestructiveMigration()
            .build()
    }

//    -----------------------------------------ADD DAOS HERE-------------------------------------------------------------------

//    @Provides
//    @Singleton
//    fun provideFlickDAO(database: MasterDatabase) = database.flickDAO() //todo add Dao here in future

    @Provides
    @Singleton
    fun provideApiWebservice(restAdapter: Retrofit): WebService {
        val webService = restAdapter.create(WebService::class.java)
        WebServiceHolder.instance.setAPIService(webService)
        return webService
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(object: Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val request = chain.request()
                    return chain.proceed(request)
                }
            }).build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .enableComplexMapKeySerialization()
            .serializeNulls()
            .setPrettyPrinting()
            .setLenient()
            .create()
    }

    @Provides
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()
    }
}