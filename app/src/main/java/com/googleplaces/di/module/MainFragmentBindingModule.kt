package com.googleplaces.di.module

import com.googleplaces.ui.home.HomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentBindingModule {

    @ContributesAndroidInjector
    internal abstract fun provideHomeFragment(): HomeFragment
}