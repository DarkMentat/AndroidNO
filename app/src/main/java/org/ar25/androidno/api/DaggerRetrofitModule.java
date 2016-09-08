package org.ar25.androidno.api;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;


@Module
public class DaggerRetrofitModule {

  @Provides @Singleton
  public Retrofit provideRetrofit () {
    return new Retrofit.Builder()
                  .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                  .addConverterFactory(new HtmlPostConverterFactory())
                  .baseUrl("http://ar25.org/")
                  .build();
  }

  @Provides @Singleton
  public NOPostsApi providePostsApi (Retrofit retrofit) {
    return retrofit.create(NOPostsApi.class);
  }
}
