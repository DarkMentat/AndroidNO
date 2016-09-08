package org.ar25.androidno.db;

import android.app.Application;
import android.database.sqlite.SQLiteOpenHelper;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;

import org.ar25.androidno.NOApplication;
import org.ar25.androidno.entities.Post;
import org.ar25.androidno.entities.PostSQLiteTypeMapping;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DaggerDbModule {

  Application mApplication;

  public DaggerDbModule(Application application) {
    mApplication = application;
  }

  @Provides @Singleton
  public StorIOSQLite provideStorioSQLite(SQLiteOpenHelper sqLiteOpenHelper) {
    return DefaultStorIOSQLite.builder()
        .sqliteOpenHelper(sqLiteOpenHelper)
        .addTypeMapping(Post.class, new PostSQLiteTypeMapping())
        .build();
  }

  @Provides @Singleton
  public SQLiteOpenHelper provideSQLiteOpenHelper() {
    return new DbOpenHelper(mApplication);
  }

  @Provides @Singleton
  public LocalStorage provideLocalStorage() {
    return new LocalStorage((NOApplication) mApplication);
  }
}