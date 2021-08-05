package com.laaltentech.abou.myapplication.di.module

import com.laaltentech.abou.myapplication.facebook.owner.fragments.FacebookProfileFragment
import com.laaltentech.abou.myapplication.facebook.owner.fragments.FragmentPageData
import com.laaltentech.abou.myapplication.facebook.owner.fragments.FacebookLoginFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentModule {
    //todo just an example to add fragments here
    @ContributesAndroidInjector
    abstract fun contributeGameInstructionsFragment(): FacebookLoginFragment

    @ContributesAndroidInjector
    abstract fun contributeFacebookProfileFragment(): FacebookProfileFragment

    @ContributesAndroidInjector
    abstract fun contributeFragmentPageData(): FragmentPageData
}