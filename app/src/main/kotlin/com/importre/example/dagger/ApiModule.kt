package com.importre.example.dagger

import com.importre.example.BuildConfig
import com.importre.example.api.Api
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory as RxJavaCall

@Module
class ApiModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(initHttpLoggingInterceptor())
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(client: OkHttpClient): Api {
        return makeRetrofit(client, Api.BASE_URL)
            .create(Api::class.java)
    }

    fun initHttpLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    fun makeRetrofit(client: OkHttpClient,
                     baseUrl: String): Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(baseUrl)
        .addCallAdapterFactory(RxJavaCall.createWithScheduler(Schedulers.io()))
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
