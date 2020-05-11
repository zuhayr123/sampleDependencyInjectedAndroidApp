package com.laaltentech.abou.myapplication.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.laaltentech.abou.myapplication.util.AppExecutors
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes =
[
    (ViewModelModule::class),
    (DaoModule::class),
    (RepositoryModule::class)
])

class AppModule {


    /**
     * shared preferences
     */
    @Provides
    @Singleton
    fun provideSharedPreferences(application: Application): SharedPreferences {
        return application.getSharedPreferences(
            "VehicleTap",
            Context.MODE_PRIVATE)
    }

    /**
     * Pagination injection
     */
    @Provides
    @Singleton
    fun providePagingConfig(): PagedList.Config {
        return PagedList.Config.Builder()
            .setPageSize(10)
            .setInitialLoadSizeHint(30)
            .setPrefetchDistance(10)
            .setEnablePlaceholders(true)
            .build()
    }


    @Provides
    @Singleton
    fun providePageBuilder(
        source: DataSource.Factory<Int, Any>,
        config: PagedList.Config,
        executor: AppExecutors
    ): LiveData<PagedList<Any>> {
        return LivePagedListBuilder(source, config)
            .setFetchExecutor(executor.diskIO())
            .build()
    }


}