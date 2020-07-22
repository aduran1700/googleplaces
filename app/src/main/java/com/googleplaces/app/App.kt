package com.googleplaces.app

import com.googleplaces.di.component.ApplicationComponent
import com.googleplaces.di.component.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.DaggerApplication


class App : DaggerApplication(), HasAndroidInjector {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val component: ApplicationComponent = DaggerApplicationComponent.builder().application(this).build()
        component.inject(this)

        return component
    }
}