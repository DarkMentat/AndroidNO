package org.ar25.androidno.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DbOpenHelper(context: Context) : SQLiteOpenHelper(context, DbOpenHelper.DB_NAME, null, 1) {

    companion object {
        const val DB_NAME = "no_local_database"
        const val DB_POSTS_TABLE = "no_posts"
        const val DB_POSTS_ID = "id"
        const val DB_POSTS_HEADER = "header"
        const val DB_POSTS_PUBLISH_DATE = "publish_date"
        const val DB_POSTS_TEASER = "teaser"
        const val DB_POSTS_IMAGE_URL = "image_url"
        const val DB_POSTS_TEXT = "text"
    }

    val createPostsTableQuery =
         "CREATE TABLE $DB_POSTS_TABLE (" +
             "$DB_POSTS_ID INTEGER NOT NULL PRIMARY KEY, " +
             "$DB_POSTS_HEADER TEXT NOT NULL, " +
             "$DB_POSTS_PUBLISH_DATE TEXT NOT NULL, " +
             "$DB_POSTS_IMAGE_URL TEXT NOT NULL, " +
             "$DB_POSTS_TEASER TEXT NOT NULL, " +
             "$DB_POSTS_TEXT TEXT NULL " +
         ");"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createPostsTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // no impl
    }
}

