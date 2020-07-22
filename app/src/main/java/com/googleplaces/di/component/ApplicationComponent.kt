package com.googleplaces.di.component

import android.app.Application
import com.googleplaces.app.App
import com.googleplaces.di.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.android.support.DaggerApplication
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ContextModule::class,
    MainActivityModule::class,
    AndroidSupportInjectionModule::class,
    ApplicationModule::class,
    LocationApiModule::class,
    ActivityBindingModule::class
   ]
)

interface ApplicationComponent : AndroidInjector<DaggerApplication> {

    fun inject(application: App)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): ApplicationComponent
    }
}