package com.laaltentech.abou.myapplication.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.laaltentech.abou.myapplication.di.ViewModelKey
import com.laaltentech.abou.myapplication.factory.AppModelFactory
import com.laaltentech.abou.myapplication.facebook.observer.FacebookProfileViewModel
import com.laaltentech.abou.myapplication.facebook.observer.PageDataViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {

    //todo add these details to the app model factory
    @Binds
    @IntoMap
    @ViewModelKey(FacebookProfileViewModel::class)
    abstract fun bindGameDataViewModel(newFacebookProfileViewModel: FacebookProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PageDataViewModel::class)
    abstract fun bindPageDataViewModel(newGameDataViewModel: PageDataViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: AppModelFactory): ViewModelProvider.Factory
}