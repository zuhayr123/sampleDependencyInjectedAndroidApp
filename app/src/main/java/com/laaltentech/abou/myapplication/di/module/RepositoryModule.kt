package com.laaltentech.abou.myapplication.di.module

import com.laaltentech.abou.myapplication.di.WebService
import com.laaltentech.abou.myapplication.game.data.GameDAO
import com.laaltentech.abou.myapplication.game.repository.FacebookDataRepository
import com.laaltentech.abou.myapplication.util.AppExecutors
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    //todo provides repository

    @Provides
    @Singleton
    fun provideFlickrRepository(webservice: WebService, dao: GameDAO, executor: AppExecutors): FacebookDataRepository {
        return FacebookDataRepository(webService = webservice, gameDAO = dao, appExecutors = executor)
    }

}