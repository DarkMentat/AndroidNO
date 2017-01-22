package org.ar25.androidno.api

import com.facebook.stetho.okhttp3.StethoInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BASIC
import org.ar25.androidno.BuildConfig
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.inject.Singleton


@Module
class DaggerRetrofitModule {

    @Provides @Singleton
    fun provideRetrofit(): Retrofit {

        val okHttpBuilder = OkHttpClient.Builder()

        okHttpBuilder
                .connectTimeout(5000L, MILLISECONDS)
                .readTimeout(10000L, MILLISECONDS)
                .retryOnConnectionFailure(true)

        if (BuildConfig.DEBUG && BuildConfig.FLAVOR == "dev") {

            okHttpBuilder.addNetworkInterceptor(StethoInterceptor())
            okHttpBuilder.addInterceptor(HttpLoggingInterceptor().apply { level = BASIC })
        }

        return Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(HtmlPostConverterFactory())
                .client(okHttpBuilder.build())
                .baseUrl("http://www.ar25.org/")
                .build()
    }

    @Provides @Singleton
    fun providePostsApi(retrofit: Retrofit): NOPostsApi {
        return retrofit.create(NOPostsApi::class.java)
    }
}
