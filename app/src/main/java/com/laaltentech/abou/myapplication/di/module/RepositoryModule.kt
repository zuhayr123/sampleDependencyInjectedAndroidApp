package com.laaltentech.abou.myapplication.di.module

import com.laaltentech.abou.myapplication.di.WebService
import com.laaltentech.abou.myapplication.facebook.data.FacebookDAO
import com.laaltentech.abou.myapplication.facebook.repository.FacebookDataRepository
import com.laaltentech.abou.myapplication.util.AppExecutors
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    //todo provides repository

    @Provides
    @Singleton
    fun provideFlickrRepository(webservice: WebService, dao: FacebookDAO, executor: AppExecutors): FacebookDataRepository {
        return FacebookDataRepository(webService = webservice, facebookDAO = dao, appExecutors = executor)
    }

}