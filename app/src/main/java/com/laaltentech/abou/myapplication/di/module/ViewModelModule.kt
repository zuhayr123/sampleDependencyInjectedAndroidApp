package com.laaltentech.abou.myapplication.di.module

import androidx.lifecycle.ViewModelProvider
import com.laaltentech.abou.myapplication.factory.AppModelFactory
import dagger.Binds
import dagger.Module

@Suppress("unused")
@Module
abstract class ViewModelModule {

    //todo add these details to the app model factory
//    @Binds
//    @IntoMap
//    @ViewModelKey(UsersListOrderViewModel::class)
//    abstract fun bindUsersListOrderViewModel(newOrdersViewModel: UsersListOrderViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: AppModelFactory): ViewModelProvider.Factory
}