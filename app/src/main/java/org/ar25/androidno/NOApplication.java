package org.ar25.androidno;

import android.app.Activity;
import android.app.Application;

import org.ar25.androidno.api.DaggerRetrofitModule;
import org.ar25.androidno.db.DaggerDbModule;
import org.ar25.androidno.db.LocalStorage;
import org.ar25.androidno.presenters.DaggerPresenterModule;
import org.ar25.androidno.presenters.MainPresenter;
import org.ar25.androidno.ui.MainActivity;

import javax.inject.Singleton;

import dagger.Component;


public class NOApplication extends Application {

  @Singleton @Component(modules={DaggerDbModule.class, DaggerPresenterModule.class, DaggerRetrofitModule.class })
  public interface NOAppComponent {
    void inject(MainPresenter presenter);
    void inject(MainActivity activity);
  }

  public static NOAppComponent getNOAppComponent(Activity activity){
    return ((NOApplication) activity.getApplication()).getNOAppComponent();
  }

  private NOAppComponent mNOAppComponent;

  @Override public void onCreate() {
    super.onCreate();

    mNOAppComponent = DaggerNOApplication_NOAppComponent.builder()
        .daggerDbModule(new DaggerDbModule(this))
        .daggerPresenterModule(new DaggerPresenterModule(this))
        .daggerRetrofitModule(new DaggerRetrofitModule())
        .build();
  }

  public NOAppComponent getNOAppComponent(){
    return mNOAppComponent;
  }
}
