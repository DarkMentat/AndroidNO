package org.ar25.androidno.db;

import android.database.sqlite.SQLiteOpenHelper;

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
import static org.ar25.androidno.entities.Post.newPost;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class LocalStorageTest {

  @Test public void savePostsTest() throws Exception {
    NOApplication app = (NOApplication) RuntimeEnvironment.application;
    DaggerDbModule daggerDbModule = new DaggerDbModule(app);

    SQLiteOpenHelper sqLiteOpenHelper = daggerDbModule.provideSQLiteOpenHelper();
    LocalStorage localStorage = new LocalStorage(app);
    localStorage.mSQLiteOpenHelper = sqLiteOpenHelper;
    localStorage.mStorIOSQLite = daggerDbModule.provideStorioSQLite(sqLiteOpenHelper);

    List<Post> fakePosts = new ArrayList<>();
    fakePosts.add(newPost(0L, "Test post 1", "001", "http://pp.vk.me/c627130/v627130524/d0b2/TPRLR-PWSVo.jpg", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque tempor tempor magna, sed hendrerit nisi varius sit amet. Morbi iaculis pretium nunc, finibus posuere mi eleifend ac. Aliquam erat volutpat. In at tristique magna. Duis semper sodales quam vel maximus. Nam aliquam urna sit amet ante aliquam gravida. Fusce mattis ante metus, ac faucibus felis vestibulum ac. Praesent pulvinar dolor ex, in bibendum sapien pretium quis. Cras hendrerit, ante nec tempus dapibus, mauris massa convallis nisl, lobortis imperdiet velit lorem et felis. Vivamus bibendum, est eu sollicitudin pellentesque, odio elit porttitor libero, condimentum dapibus justo urna ac elit. Sed condimentum nunc ac iaculis vulputate. Nullam sit amet sapien eget lorem sodales fringilla. Cras luctus et ligula eu auctor. Nullam laoreet id elit id sagittis. Nullam lacinia in magna nec vulputate."));
    fakePosts.add(newPost(1L, "Test post 2", "002", "http://pp.vk.me/c624621/v624621448/1e79e/poETQJ4kCyE.jpg", "Morbi consectetur, nunc ut luctus suscipit, lacus ante venenatis lorem, non fermentum dui purus non sapien. Nullam auctor tempus quam, non volutpat metus luctus interdum. Praesent pretium ante libero, eu gravida eros porta vitae. Duis semper dapibus odio, eget fermentum lectus ullamcorper quis. Praesent vulputate, risus feugiat pulvinar lobortis, tortor ex consequat ligula, eu consectetur dui nisi vitae ipsum. Donec consectetur commodo eros ut aliquet. Aliquam suscipit ac dui ac sodales. Suspendisse dictum nulla sit amet vulputate fermentum. Nulla porttitor, leo non sodales scelerisque, nulla lacus lacinia erat, eu aliquam massa urna in leo. Suspendisse potenti. Nullam finibus purus nec tortor feugiat fermentum. Phasellus laoreet at tortor euismod iaculis."));
    fakePosts.add(newPost(2L, "Test post 3", "003", "http://pp.vk.me/c9568/v9568282/921/7OInBxko54U.jpg", "Suspendisse ac sapien maximus, interdum magna vitae, pharetra ex. Nam eget lorem ac enim aliquet malesuada sed sed ipsum. Sed at nisl neque. Nullam eu mauris metus. Maecenas vulputate leo dapibus risus convallis, quis commodo justo aliquet. Praesent egestas, sapien vitae mollis aliquet, velit odio finibus eros, in vulputate turpis risus et tortor. Donec vel lorem vitae massa facilisis finibus."));

    List<Post> newFakePosts = new ArrayList<>();
    newFakePosts.add(newPost(0L, "Test post 1", "", "http://pp.vk.me/c627130/v627130524/d0b2/TPRLR-PWSVo.jpg", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque tempor tempor magna, sed hendrerit nisi varius sit amet. Morbi iaculis pretium nunc, finibus posuere mi eleifend ac. Aliquam erat volutpat. In at tristique magna. Duis semper sodales quam vel maximus. Nam aliquam urna sit amet ante aliquam gravida. Fusce mattis ante metus, ac faucibus felis vestibulum ac. Praesent pulvinar dolor ex, in bibendum sapien pretium quis. Cras hendrerit, ante nec tempus dapibus, mauris massa convallis nisl, lobortis imperdiet velit lorem et felis. Vivamus bibendum, est eu sollicitudin pellentesque, odio elit porttitor libero, condimentum dapibus justo urna ac elit. Sed condimentum nunc ac iaculis vulputate. Nullam sit amet sapien eget lorem sodales fringilla. Cras luctus et ligula eu auctor. Nullam laoreet id elit id sagittis. Nullam lacinia in magna nec vulputate."));
    newFakePosts.add(newPost(1L, "Test post 2", "", "http://pp.vk.me/c624621/v624621448/1e79e/poETQJ4kCyE.jpg", "Morbi consectetur, nunc ut luctus suscipit, lacus ante venenatis lorem, non fermentum dui purus non sapien. Nullam auctor tempus quam, non volutpat metus luctus interdum. Praesent pretium ante libero, eu gravida eros porta vitae. Duis semper dapibus odio, eget fermentum lectus ullamcorper quis. Praesent vulputate, risus feugiat pulvinar lobortis, tortor ex consequat ligula, eu consectetur dui nisi vitae ipsum. Donec consectetur commodo eros ut aliquet. Aliquam suscipit ac dui ac sodales. Suspendisse dictum nulla sit amet vulputate fermentum. Nulla porttitor, leo non sodales scelerisque, nulla lacus lacinia erat, eu aliquam massa urna in leo. Suspendisse potenti. Nullam finibus purus nec tortor feugiat fermentum. Phasellus laoreet at tortor euismod iaculis."));
    newFakePosts.add(newPost(2L, "Test post 3", "", "http://pp.vk.me/c9568/v9568282/921/7OInBxko54U.jpg", "Suspendisse ac sapien maximus, interdum magna vitae, pharetra ex. Nam eget lorem ac enim aliquet malesuada sed sed ipsum. Sed at nisl neque. Nullam eu mauris metus. Maecenas vulputate leo dapibus risus convallis, quis commodo justo aliquet. Praesent egestas, sapien vitae mollis aliquet, velit odio finibus eros, in vulputate turpis risus et tortor. Donec vel lorem vitae massa facilisis finibus."));
    newFakePosts.add(newPost(3L, "Test post 4", "004", "http://pp.vk.me/c308617/v308617239/5826/Dog9X_cK31Q.jpg", "Sed in finibus tortor. Aliquam vulputate sodales orci quis mattis. Proin eleifend massa metus, non venenatis lacus consequat a. Sed aliquet ullamcorper tortor sit amet fermentum. Etiam rutrum tempor blandit. Fusce pretium non metus nec viverra. Phasellus porta feugiat magna nec ultrices. Mauris ut neque a felis facilisis dictum. Morbi vitae fermentum nulla, nec accumsan dui."));
    newFakePosts.add(newPost(4L, "Test post 5", "005", "http://pp.vk.me/c625524/v625524333/13b37/SpEz0XoMEgQ.jpg", "Nam mollis ullamcorper fringilla. Pellentesque pulvinar lorem est, non finibus ligula cursus quis. In id urna fringilla, faucibus nisi eget, interdum turpis. Integer ac velit sed ligula viverra aliquet. Quisque rhoncus risus varius lacinia mattis. Phasellus dapibus sapien a odio commodo, sed tristique est semper. Vestibulum quis congue eros, non faucibus massa."));

    List<Post> expectedFakePosts = new ArrayList<>();
    expectedFakePosts.add(newPost(0L, "Test post 1", "001", "http://pp.vk.me/c627130/v627130524/d0b2/TPRLR-PWSVo.jpg", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque tempor tempor magna, sed hendrerit nisi varius sit amet. Morbi iaculis pretium nunc, finibus posuere mi eleifend ac. Aliquam erat volutpat. In at tristique magna. Duis semper sodales quam vel maximus. Nam aliquam urna sit amet ante aliquam gravida. Fusce mattis ante metus, ac faucibus felis vestibulum ac. Praesent pulvinar dolor ex, in bibendum sapien pretium quis. Cras hendrerit, ante nec tempus dapibus, mauris massa convallis nisl, lobortis imperdiet velit lorem et felis. Vivamus bibendum, est eu sollicitudin pellentesque, odio elit porttitor libero, condimentum dapibus justo urna ac elit. Sed condimentum nunc ac iaculis vulputate. Nullam sit amet sapien eget lorem sodales fringilla. Cras luctus et ligula eu auctor. Nullam laoreet id elit id sagittis. Nullam lacinia in magna nec vulputate."));
    expectedFakePosts.add(newPost(1L, "Test post 2", "002", "http://pp.vk.me/c624621/v624621448/1e79e/poETQJ4kCyE.jpg", "Morbi consectetur, nunc ut luctus suscipit, lacus ante venenatis lorem, non fermentum dui purus non sapien. Nullam auctor tempus quam, non volutpat metus luctus interdum. Praesent pretium ante libero, eu gravida eros porta vitae. Duis semper dapibus odio, eget fermentum lectus ullamcorper quis. Praesent vulputate, risus feugiat pulvinar lobortis, tortor ex consequat ligula, eu consectetur dui nisi vitae ipsum. Donec consectetur commodo eros ut aliquet. Aliquam suscipit ac dui ac sodales. Suspendisse dictum nulla sit amet vulputate fermentum. Nulla porttitor, leo non sodales scelerisque, nulla lacus lacinia erat, eu aliquam massa urna in leo. Suspendisse potenti. Nullam finibus purus nec tortor feugiat fermentum. Phasellus laoreet at tortor euismod iaculis."));
    expectedFakePosts.add(newPost(2L, "Test post 3", "003", "http://pp.vk.me/c9568/v9568282/921/7OInBxko54U.jpg", "Suspendisse ac sapien maximus, interdum magna vitae, pharetra ex. Nam eget lorem ac enim aliquet malesuada sed sed ipsum. Sed at nisl neque. Nullam eu mauris metus. Maecenas vulputate leo dapibus risus convallis, quis commodo justo aliquet. Praesent egestas, sapien vitae mollis aliquet, velit odio finibus eros, in vulputate turpis risus et tortor. Donec vel lorem vitae massa facilisis finibus."));
    expectedFakePosts.add(newPost(3L, "Test post 4", "004", "http://pp.vk.me/c308617/v308617239/5826/Dog9X_cK31Q.jpg", "Sed in finibus tortor. Aliquam vulputate sodales orci quis mattis. Proin eleifend massa metus, non venenatis lacus consequat a. Sed aliquet ullamcorper tortor sit amet fermentum. Etiam rutrum tempor blandit. Fusce pretium non metus nec viverra. Phasellus porta feugiat magna nec ultrices. Mauris ut neque a felis facilisis dictum. Morbi vitae fermentum nulla, nec accumsan dui."));
    expectedFakePosts.add(newPost(4L, "Test post 5", "005", "http://pp.vk.me/c625524/v625524333/13b37/SpEz0XoMEgQ.jpg", "Nam mollis ullamcorper fringilla. Pellentesque pulvinar lorem est, non finibus ligula cursus quis. In id urna fringilla, faucibus nisi eget, interdum turpis. Integer ac velit sed ligula viverra aliquet. Quisque rhoncus risus varius lacinia mattis. Phasellus dapibus sapien a odio commodo, sed tristique est semper. Vestibulum quis congue eros, non faucibus massa."));

    localStorage.savePosts(fakePosts);

    assertEquals(fakePosts,
        localStorage.mStorIOSQLite
            .get()
            .listOfObjects(Post.class)
            .withQuery(
              Query.builder().table(DbOpenHelper.DB_POSTS_TABLE).build())
            .prepare()
            .executeAsBlocking());

    localStorage.savePosts(newFakePosts);

    assertEquals(expectedFakePosts,
        localStorage.mStorIOSQLite
            .get()
            .listOfObjects(Post.class)
            .withQuery(
                Query.builder().table(DbOpenHelper.DB_POSTS_TABLE).build())
            .prepare()
            .executeAsBlocking());
  }
  @Test public void savePostTest() throws Exception {
    NOApplication app = (NOApplication) RuntimeEnvironment.application;
    DaggerDbModule daggerDbModule = new DaggerDbModule(app);

    SQLiteOpenHelper sqLiteOpenHelper = daggerDbModule.provideSQLiteOpenHelper();
    LocalStorage localStorage = new LocalStorage(app);
    localStorage.mSQLiteOpenHelper = sqLiteOpenHelper;
    localStorage.mStorIOSQLite = daggerDbModule.provideStorioSQLite(sqLiteOpenHelper);

    Post fakePost = newPost(4L, "Test post 5", "005", "http://pp.vk.me/c625524/v625524333/13b37/SpEz0XoMEgQ.jpg", "Nam mollis ullamcorper fringilla. Pellentesque pulvinar lorem est, non finibus ligula cursus quis. In id urna fringilla, faucibus nisi eget, interdum turpis. Integer ac velit sed ligula viverra aliquet. Quisque rhoncus risus varius lacinia mattis. Phasellus dapibus sapien a odio commodo, sed tristique est semper. Vestibulum quis congue eros, non faucibus massa.");
    Post updatedPost = newPost(4L, "Test post 5", "005", "", "Nam mollis ullamcorper fringilla. Pellentesque pulvinar lorem est, non finibus ligula cursus quis. In id urna fringilla, faucibus nisi eget, interdum turpis. Integer ac velit sed ligula viverra aliquet. Quisque rhoncus risus varius lacinia mattis. Phasellus dapibus sapien a odio commodo, sed tristique est semper. Vestibulum quis congue eros, non faucibus massa.", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque tempor tempor magna, sed hendrerit nisi varius sit amet. Morbi iaculis pretium nunc, finibus posuere mi eleifend ac. Aliquam erat volutpat. In at tristique magna. Duis semper sodales quam vel maximus. Nam aliquam urna sit amet ante aliquam gravida. Fusce mattis ante metus, ac faucibus felis vestibulum ac. Praesent pulvinar dolor ex, in bibendum sapien pretium quis. Cras hendrerit, ante nec tempus dapibus, mauris massa convallis nisl, lobortis imperdiet velit lorem et felis. Vivamus bibendum, est eu sollicitudin pellentesque, odio elit porttitor libero, condimentum dapibus justo urna ac elit. Sed condimentum nunc ac iaculis vulputate. Nullam sit amet sapien eget lorem sodales fringilla. Cras luctus et ligula eu auctor. Nullam laoreet id elit id sagittis. Nullam lacinia in magna nec vulputate.");
    Post expectedPost = newPost(4L, "Test post 5", "005", "http://pp.vk.me/c625524/v625524333/13b37/SpEz0XoMEgQ.jpg", "Nam mollis ullamcorper fringilla. Pellentesque pulvinar lorem est, non finibus ligula cursus quis. In id urna fringilla, faucibus nisi eget, interdum turpis. Integer ac velit sed ligula viverra aliquet. Quisque rhoncus risus varius lacinia mattis. Phasellus dapibus sapien a odio commodo, sed tristique est semper. Vestibulum quis congue eros, non faucibus massa.", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque tempor tempor magna, sed hendrerit nisi varius sit amet. Morbi iaculis pretium nunc, finibus posuere mi eleifend ac. Aliquam erat volutpat. In at tristique magna. Duis semper sodales quam vel maximus. Nam aliquam urna sit amet ante aliquam gravida. Fusce mattis ante metus, ac faucibus felis vestibulum ac. Praesent pulvinar dolor ex, in bibendum sapien pretium quis. Cras hendrerit, ante nec tempus dapibus, mauris massa convallis nisl, lobortis imperdiet velit lorem et felis. Vivamus bibendum, est eu sollicitudin pellentesque, odio elit porttitor libero, condimentum dapibus justo urna ac elit. Sed condimentum nunc ac iaculis vulputate. Nullam sit amet sapien eget lorem sodales fringilla. Cras luctus et ligula eu auctor. Nullam laoreet id elit id sagittis. Nullam lacinia in magna nec vulputate.");

    localStorage.savePost(fakePost);
    localStorage.savePost(updatedPost);

    assertEquals(expectedPost,
        localStorage.mStorIOSQLite
            .get()
            .object(Post.class)
            .withQuery(
                Query.builder().table(DbOpenHelper.DB_POSTS_TABLE).where("id = 4").build())
            .prepare()
            .executeAsBlocking());
  }

  @Test public void getPostsTest() throws Exception {

    NOApplication app = (NOApplication) RuntimeEnvironment.application;
    DaggerDbModule daggerDbModule = new DaggerDbModule(app);

    SQLiteOpenHelper sqLiteOpenHelper = daggerDbModule.provideSQLiteOpenHelper();
    LocalStorage localStorage = new LocalStorage(app);
    localStorage.mSQLiteOpenHelper = sqLiteOpenHelper;
    localStorage.mStorIOSQLite = daggerDbModule.provideStorioSQLite(sqLiteOpenHelper);

    ArrayList<Post> fakePosts = new FakeNOPostsApi().getFakePosts();
    localStorage.savePosts(fakePosts);

    List<Post> posts = localStorage.getPosts();

    assertEquals(fakePosts, posts);
  }

  @Test public void getPostsTestObservable() throws Exception {

    NOApplication app = (NOApplication) RuntimeEnvironment.application;
    DaggerDbModule daggerDbModule = new DaggerDbModule(app);

    SQLiteOpenHelper sqLiteOpenHelper = daggerDbModule.provideSQLiteOpenHelper();
    LocalStorage localStorage = new LocalStorage(app);
    localStorage.mSQLiteOpenHelper = sqLiteOpenHelper;
    localStorage.mStorIOSQLite = daggerDbModule.provideStorioSQLite(sqLiteOpenHelper);

    ArrayList<Post> fakePosts = new FakeNOPostsApi().getFakePosts();
    localStorage.savePosts(fakePosts);

    localStorage.getPostsObservable().subscribe(
        posts -> assertEquals(fakePosts, posts)
    );
  }

  @Test public void getPostObservableTest() throws Exception {
    NOApplication app = (NOApplication) RuntimeEnvironment.application;
    DaggerDbModule daggerDbModule = new DaggerDbModule(app);

    SQLiteOpenHelper sqLiteOpenHelper = daggerDbModule.provideSQLiteOpenHelper();
    LocalStorage localStorage = new LocalStorage(app);
    localStorage.mSQLiteOpenHelper = sqLiteOpenHelper;
    localStorage.mStorIOSQLite = daggerDbModule.provideStorioSQLite(sqLiteOpenHelper);

    ArrayList<Post> fakePosts = new FakeNOPostsApi().getFakePosts();
    localStorage.savePosts(fakePosts);

    localStorage.getPostObservable(0).subscribe(
        post -> assertEquals(fakePosts.get(0), post)
    );
  }
}