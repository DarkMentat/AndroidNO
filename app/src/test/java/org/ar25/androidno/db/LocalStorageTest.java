package org.ar25.androidno.db;

import com.pushtorefresh.storio.sqlite.queries.Query;

import org.ar25.androidno.BuildConfig;
import org.ar25.androidno.NOApplication;
import org.ar25.androidno.api.FakeNOPostsApi;
import org.ar25.androidno.entities.Post;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class LocalStorageTest {

  @Test public void savePostsTest() throws Exception {
    NOApplication app = (NOApplication) RuntimeEnvironment.application;
    DaggerDbModule daggerDbModule = new DaggerDbModule(app);

    LocalStorage localStorage = new LocalStorage(
        daggerDbModule.provideStorioSQLite(daggerDbModule.provideSQLiteOpenHelper()));

    ArrayList<Post> fakePosts = new FakeNOPostsApi().getFakePosts();

    localStorage.savePosts(fakePosts);

    assertEquals(fakePosts,
        localStorage.mStorIOSQLite
            .get()
            .listOfObjects(Post.class)
            .withQuery(
              Query.builder().table(DbOpenHelper.DB_POSTS_TABLE).build())
            .prepare()
            .executeAsBlocking());
  }

  @Test public void getPostsTest() throws Exception {

    NOApplication app = (NOApplication) RuntimeEnvironment.application;
    DaggerDbModule daggerDbModule = new DaggerDbModule(app);

    LocalStorage localStorage = new LocalStorage(
        daggerDbModule.provideStorioSQLite(daggerDbModule.provideSQLiteOpenHelper()));

    ArrayList<Post> fakePosts = new FakeNOPostsApi().getFakePosts();
    localStorage.savePosts(fakePosts);

    List<Post> posts = localStorage.getPosts();

    assertEquals(fakePosts, posts);
  }

  @Test public void getPostsTestObservable() throws Exception {

    NOApplication app = (NOApplication) RuntimeEnvironment.application;
    DaggerDbModule daggerDbModule = new DaggerDbModule(app);

    LocalStorage localStorage = new LocalStorage(
        daggerDbModule.provideStorioSQLite(daggerDbModule.provideSQLiteOpenHelper()));

    ArrayList<Post> fakePosts = new FakeNOPostsApi().getFakePosts();
    localStorage.savePosts(fakePosts);

    localStorage.getPostsObservable().subscribe(
        posts -> assertEquals(fakePosts, posts)
    );
  }
}