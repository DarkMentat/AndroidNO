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

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class DetailPresenterTest {

  @Rule
  public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

  @Test public void fetchingPost() throws Exception {
    DetailPresenter presenter = new DetailPresenter(){{
      mNOPostsApi = new FakeNOPostsApi();
      mLocalStorage = mock(LocalStorage.class);
    }};

    ArrayList<Post> fakePosts = ((FakeNOPostsApi) presenter.mNOPostsApi).getFakePosts();
    when(presenter.mLocalStorage.getPostObservable(4L)).thenReturn(Observable.just(fakePosts.get(4)));

    DetailView view = mock(DetailView.class);

    presenter.setView(view);
    presenter.fetchPost(4);

    InOrder inOrder = inOrder(view, presenter.mLocalStorage);

    inOrder.verify(presenter.mLocalStorage).getPostObservable(4L);
    inOrder.verify(view).onGetPost(fakePosts.get(4));
    inOrder.verify(view).onGetPost(((FakeNOPostsApi) presenter.mNOPostsApi).getFakeFullPost());

    verify(presenter.mLocalStorage).savePost(((FakeNOPostsApi) presenter.mNOPostsApi).getFakeFullPost());

  }
}