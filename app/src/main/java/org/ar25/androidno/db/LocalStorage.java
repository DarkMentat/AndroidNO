package org.ar25.androidno.db;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import org.ar25.androidno.entities.Post;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

import static org.ar25.androidno.db.DbOpenHelper.DB_POSTS_ID;
import static org.ar25.androidno.db.DbOpenHelper.DB_POSTS_TABLE;

public class LocalStorage {

  @Inject StorIOSQLite mStorIOSQLite;

  public LocalStorage(StorIOSQLite storIOSQLite) {
    mStorIOSQLite = storIOSQLite;
  }

  public void savePosts(List<Post> posts){
    mStorIOSQLite
        .put()
        .objects(posts)
        .prepare()
        .executeAsBlocking();
  }
  public void savePost(Post post){
    mStorIOSQLite
        .put()
        .object(post)
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
}
