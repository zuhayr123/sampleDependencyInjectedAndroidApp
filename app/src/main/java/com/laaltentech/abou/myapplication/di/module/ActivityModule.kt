package com.laaltentech.abou.myapplication.di.module

import com.laaltentech.abou.myapplication.facebook.owner.activity.FacebookCentralActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class ActivityModule {

    //todo add here activies that needs to be added, an example is added below
    @ContributesAndroidInjector(modules = [(FragmentModule::class)])
    abstract fun contributeFacebookCentralActivity(): FacebookCentralActivity



}