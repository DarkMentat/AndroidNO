package org.ar25.androidno.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DbOpenHelper extends SQLiteOpenHelper {

  public static final String DB_NAME = "no_local_database";
  public static final String DB_POSTS_TABLE = "no_posts";
  public static final String DB_POSTS_ID = "id";
  public static final String DB_POSTS_HEADER = "header";
  public static final String DB_POSTS_PUBLISH_DATE = "publish_date";
  public static final String DB_POSTS_TEASER = "teaser";
  public static final String DB_POSTS_IMAGE_URL = "image_url";
  public static final String DB_POSTS_TEXT = "text";

  private static String getCreateTablePostsQuery() {
    return "CREATE TABLE " + DB_POSTS_TABLE + " ("
        + DB_POSTS_ID + " INTEGER NOT NULL PRIMARY KEY, "
        + DB_POSTS_HEADER + " TEXT NOT NULL, "
        + DB_POSTS_PUBLISH_DATE + " TEXT NOT NULL, "
        + DB_POSTS_IMAGE_URL + " TEXT NOT NULL, "
        + DB_POSTS_TEASER + " TEXT NOT NULL, "
        + DB_POSTS_TEXT + " TEXT NULL "
        + ");";
  }

  public DbOpenHelper(Context context) {
    super(context, DB_NAME, null, 1);
  }

  @Override public void onCreate(SQLiteDatabase db) {
    db.execSQL(getCreateTablePostsQuery());
  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // no impl
  }
}

