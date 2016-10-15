package org.ar25.androidno.db

import android.app.Application
import android.database.sqlite.SQLiteOpenHelper

import com.pushtorefresh.storio.sqlite.StorIOSQLite
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite

import org.ar25.androidno.entities.Post
import org.ar25.androidno.entities.PostSQLiteTypeMapping

import javax.inject.Singleton

import dagger.Module
import dagger.Provides

@Module
class DaggerDbModule(private val mApplication: Application) {

    @Provides @Singleton
    fun provideStorioSQLite(sqLiteOpenHelper: SQLiteOpenHelper): StorIOSQLite {
        return DefaultStorIOSQLite
                .builder()
                .sqliteOpenHelper(sqLiteOpenHelper)
                .addTypeMapping(Post::class.java, PostSQLiteTypeMapping())
                .build()
    }

    @Provides @Singleton
    fun provideSQLiteOpenHelper(): SQLiteOpenHelper {
        return DbOpenHelper(mApplication)
    }

    @Provides @Singleton
    fun provideLocalStorage(): LocalStorage {
        return LocalStorage()
    }
}