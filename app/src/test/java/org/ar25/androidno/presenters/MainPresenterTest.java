package org.ar25.androidno.presenters;

import org.ar25.androidno.BuildConfig;
import org.ar25.androidno.RxSchedulersOverrideRule;
import org.ar25.androidno.api.FakeNOPostsApi;
import org.ar25.androidno.db.LocalStorage;
import org.ar25.androidno.entities.Post;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import rx.Observable;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class MainPresenterTest {

  @Rule
  public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

  @Test public void fetchingPosts() throws Exception {
    MainPresenter presenter = new MainPresenter(){{
      mNOPostsApi = new FakeNOPostsApi();
      mLocalStorage = mock(LocalStorage.class);
    }};

    ArrayList<Post> fakePosts = ((FakeNOPostsApi) presenter.mNOPostsApi).getFakePosts();
    ArrayList<Post> oldFakePosts = new ArrayList<>(fakePosts);
    oldFakePosts.remove(oldFakePosts.size()-1);
    when(presenter.mLocalStorage.getPostsObservable()).thenReturn(Observable.just(oldFakePosts));

    MainView view = mock(MainView.class);

    presenter.setView(view);
    presenter.fetchPosts();

    InOrder inOrder = inOrder(view, presenter.mLocalStorage);

    inOrder.verify(presenter.mLocalStorage).getPostsObservable();
    inOrder.verify(view).onGetPosts(oldFakePosts);
    inOrder.verify(view).onGetPosts(fakePosts);

    verify(presenter.mLocalStorage).savePosts(fakePosts);
  }
}
