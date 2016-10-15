package org.ar25.androidno.api

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory

import java.util.concurrent.TimeUnit.MILLISECONDS
import okhttp3.logging.HttpLoggingInterceptor.Level.BASIC

@Module
class DaggerRetrofitModule {

    @Provides @Singleton
    fun provideRetrofit(): Retrofit {

        val logging = HttpLoggingInterceptor()
        logging.level = BASIC

        return Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(HtmlPostConverterFactory()).
                client(OkHttpClient.Builder()
                        .connectTimeout(5000L, MILLISECONDS)
                        .readTimeout(10000L, MILLISECONDS)
                        .retryOnConnectionFailure(true)
                        .addInterceptor(logging)
                        .build())
                .baseUrl("http://ar25.org/")
                .build()
    }

    @Provides @Singleton
    fun providePostsApi(retrofit: Retrofit): NOPostsApi {
        return retrofit.create(NOPostsApi::class.java)
    }
}
