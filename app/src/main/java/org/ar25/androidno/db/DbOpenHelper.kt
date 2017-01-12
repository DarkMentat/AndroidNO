package org.ar25.androidno.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.ar25.androidno.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DbOpenHelper @Inject constructor(

        @ApplicationContext val context: Context

) : SQLiteOpenHelper(context, DbOpenHelper.DB_NAME, null, db_version) {

    companion object {
        const val DB_NAME = "no_local_database"
        const val DB_POSTS_TABLE = "no_posts"
        const val DB_POSTS_ID = "id"
        const val DB_POSTS_IS_FAVORITE = "is_favorite"
        const val DB_POSTS_HEADER = "header"
        const val DB_POSTS_PUBLISH_DATE = "publish_date"
        const val DB_POSTS_TEASER = "teaser"
        const val DB_POSTS_IMAGE_URL = "image_url"
        const val DB_POSTS_IMAGE_TITLE = "image_title"
        const val DB_POSTS_TEXT = "text"
        const val DB_POSTS_GAMER = "gamer"
        const val DB_POSTS_SOURCE = "source"
        const val DB_POSTS_SOURCE_LINK = "source_link"

        const val db_version = 1
    }

    val createPostsTableQuery =
         "CREATE TABLE $DB_POSTS_TABLE (" +
             "$DB_POSTS_ID INTEGER NOT NULL PRIMARY KEY, " +
             "$DB_POSTS_IS_FAVORITE INTEGER DEFAULT 0, " +
             "$DB_POSTS_HEADER TEXT NOT NULL, " +
             "$DB_POSTS_PUBLISH_DATE TEXT NOT NULL, " +
             "$DB_POSTS_IMAGE_URL TEXT NOT NULL, " +
             "$DB_POSTS_TEASER TEXT NOT NULL, " +
             "$DB_POSTS_TEXT TEXT NULL, " +
             "$DB_POSTS_GAMER TEXT NULL, " +
             "$DB_POSTS_IMAGE_TITLE TEXT NULL, " +
             "$DB_POSTS_SOURCE TEXT NULL, " +
             "$DB_POSTS_SOURCE_LINK TEXT NULL " +
         ");"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createPostsTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        if(oldVersion != newVersion) {

            db.execSQL("DROP TABLE IF EXISTS $DB_POSTS_TABLE")
            db.execSQL(createPostsTableQuery)
        }
    }
}

