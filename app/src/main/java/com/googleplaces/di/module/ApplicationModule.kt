package com.googleplaces.di.module

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.googleplaces.MainActivity
import com.googleplaces.client.api.NearByLocationApi
import com.googleplaces.data.AppDatabase
import com.googleplaces.data.LocationRepository
import com.googleplaces.data.ResultDao
import com.googleplaces.ui.home.HomeFragment
import com.googleplaces.ui.home.HomeViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Singleton
    @Provides
    fun provideLocationRepository(api: NearByLocationApi, dao: ResultDao, sharedPreferences: SharedPreferences) : LocationRepository {
        return LocationRepository(api, dao, sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(MainActivity.SHARED_PREFS, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideLocationClient(context: Context): FusedLocationProviderClient
            = LocationServices.getFusedLocationProviderClient(context)

    @Singleton
    @Provides
    fun provideDatabase(context: Context) : AppDatabase {
        return Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "location-database").build()
    }

    @Provides
    fun provideAlbumDao(appDatabase: AppDatabase): ResultDao {
        return appDatabase.resultDao()
    }
}
