package com.googleplaces.di.module

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.googleplaces.MainActivity
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class MainActivityModule {

    @Binds
    internal abstract fun provideMainActivity(activity: MainActivity): MainActivity
}