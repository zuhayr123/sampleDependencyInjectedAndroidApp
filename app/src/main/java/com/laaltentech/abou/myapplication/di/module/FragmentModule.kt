package com.laaltentech.abou.myapplication.di.module

import com.laaltentech.abou.myapplication.game.owner.fragments.FacebookProfileFragment
import com.laaltentech.abou.myapplication.game.owner.fragments.FragmentPageData
import com.laaltentech.abou.myapplication.game.owner.fragments.GameInstructionsFragment
import com.laaltentech.abou.myapplication.game.repository.SendDataJobService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentModule {
    //todo just an example to add fragments here
    @ContributesAndroidInjector
    abstract fun contributeGameInstructionsFragment(): GameInstructionsFragment

    @ContributesAndroidInjector
    abstract fun contributeFacebookProfileFragment(): FacebookProfileFragment

    @ContributesAndroidInjector
    abstract fun contributeFragmentPageData(): FragmentPageData
}