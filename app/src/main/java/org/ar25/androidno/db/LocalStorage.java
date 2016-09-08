package org.ar25.androidno.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.Changes;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import org.ar25.androidno.NOApplication;
import org.ar25.androidno.entities.Post;
import org.ar25.androidno.entities.PostStorIOSQLitePutResolver;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

import static org.ar25.androidno.db.DbOpenHelper.DB_POSTS_ID;
import static org.ar25.androidno.db.DbOpenHelper.DB_POSTS_TABLE;

public class LocalStorage {

  @Inject StorIOSQLite mStorIOSQLite;
  @Inject SQLiteOpenHelper mSQLiteOpenHelper;

  public LocalStorage(NOApplication application) {
    application.getNOAppComponent().inject(this);
  }

  public void savePosts(List<Post> posts){
    insertManyPostPreviews(mStorIOSQLite.lowLevel(), mSQLiteOpenHelper, posts);
  }
  public void savePost(Post post){
    mStorIOSQLite
        .put()
        .object(post)
        .withPutResolver(new PostStorIOSQLitePutResolver(){
          @NonNull @Override public ContentValues mapToContentValues(@NonNull Post object) {
            ContentValues values = super.mapToContentValues(object);

            if(values.getAsString("image_url").equals("")){
              values.remove("image_url");
            }

            return values;
          }
        })
        .prepare()
        .executeAsBlocking();
  }

  public List<Post> getPosts(){
    return mStorIOSQLite
        .get()
        .listOfObjects(Post.class)
        .withQuery(
            Query.builder()
                .table(DB_POSTS_TABLE)
                .build()
        ).prepare()
        .executeAsBlocking();
  }
  public Observable<List<Post>> getPostsObservable(){
    return mStorIOSQLite
        .get()
        .listOfObjects(Post.class)
        .withQuery(
            Query.builder()
                .table(DB_POSTS_TABLE)
                .build()
        ).prepare()
        .asRxObservable();
  }

  public Observable<Post> getPostObservable(long id){
    return mStorIOSQLite
        .get()
        .object(Post.class)
        .withQuery(
            Query.builder()
                .table(DB_POSTS_TABLE)
                .where(DB_POSTS_ID + "= ?")
                .whereArgs(id)
                .build()
        ).prepare()
        .asRxObservable();
  }


  private void insertManyPostPreviews(StorIOSQLite.LowLevel lowLevel, SQLiteOpenHelper helper, List<Post> posts) {
    SQLiteDatabase db = helper.getWritableDatabase();
    try {
      db.beginTransaction();

      String sql = "INSERT OR IGNORE INTO "+DB_POSTS_TABLE+" (id, header, image_url, publish_date, teaser) VALUES (?,?,?,?,?)";
      SQLiteStatement statement = db.compileStatement(sql);

      for (Post post : posts) {
        statement.clearBindings();
        statement.bindLong(1, post.getId());
        statement.bindString(2, post.getHeader());
        statement.bindString(3, post.getImageUrl());
        statement.bindString(4, post.getPublishDate());
        statement.bindString(5, post.getTeaser());
        statement.executeInsert();
      }

      db.setTransactionSuccessful();
    } finally {
      db.endTransaction();
    }
    lowLevel.notifyAboutChanges(Changes.newInstance("tweets"));
  }
}