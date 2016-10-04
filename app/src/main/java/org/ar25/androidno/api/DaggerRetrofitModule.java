package org.ar25.androidno.api;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static okhttp3.logging.HttpLoggingInterceptor.Level.BASIC;

@Module
public class DaggerRetrofitModule {

  @Provides @Singleton
  public Retrofit provideRetrofit () {

    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    logging.setLevel(BASIC);

    return new Retrofit.Builder()
                  .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                  .addConverterFactory(new HtmlPostConverterFactory())
                  .client(new OkHttpClient.Builder()
                      .connectTimeout(5000L, MILLISECONDS)
                      .readTimeout(10000L, MILLISECONDS)
                      .retryOnConnectionFailure(true)
                      .addInterceptor(logging)
                      .build())
                  .baseUrl("http://ar25.org/")
                  .build();
  }

  @Provides @Singleton
  public NOPostsApi providePostsApi (Retrofit retrofit) {
    return retrofit.create(NOPostsApi.class);
  }
}
