package org.ar25.androidno.presenters;

import android.app.Application;

import org.ar25.androidno.NOApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class DaggerPresenterModule {

  NOApplication mApplication;

  public DaggerPresenterModule(Application application) {
    mApplication = (NOApplication) application;
  }

  @Provides @Singleton
  MainPresenter provideMainPresenter() {
    MainPresenter presenter = new MainPresenter();

    mApplication.getNOAppComponent().inject(presenter);

    return presenter;
  }
}
