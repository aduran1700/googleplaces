package com.googleplaces.di.module

import com.googleplaces.BuildConfig
import com.googleplaces.client.api.NearByLocationApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory



@Module
class LocationApiModule {

    @Provides
    fun provideClient(): OkHttpClient {
        val clientBuilder = OkHttpClient().newBuilder()
        clientBuilder.addInterceptor {

            val original = it.request()
            val originalHttpUrl = original.url()

            val url = originalHttpUrl.newBuilder()
                .addQueryParameter("key", BuildConfig.API_KEY)
                .build()

            val requestBuilder = original.newBuilder()
                .url(url)

            val request = requestBuilder.build()
            it.proceed(request)
        }
        return clientBuilder.build()
    }

    @Provides
    fun provideRetroFit(baseUrl: String, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideApiService(): NearByLocationApi {
        return provideRetroFit(BASE_URL, provideClient()).create(NearByLocationApi::class.java)
    }

    companion object {
        const val BASE_URL = "https://maps.googleapis.com/"
    }
}