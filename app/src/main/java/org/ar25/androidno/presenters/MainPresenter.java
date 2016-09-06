package org.ar25.androidno.presenters;

import android.util.Log;

import org.ar25.androidno.api.NOPostsApi;
import org.ar25.androidno.db.LocalStorage;
import org.ar25.androidno.util.Optional;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static java.util.concurrent.TimeUnit.SECONDS;


public class MainPresenter {
  @Inject LocalStorage mLocalStorage;
  @Inject NOPostsApi mNOPostsApi;

  private Optional<MainView> mView = Optional.empty();

  public void setView(MainView view) {
    mView = Optional.of(view);
  }
  public void unsetView() {
    mView = Optional.empty();
  }

  public void fetchPosts(){
    mLocalStorage.getPostsObservable()
        .subscribeOn(Schedulers.io())
        .timeout(2L, SECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            posts -> mView.ifPresent(view -> view.onGetPosts(posts)),
            error -> mView.ifPresent(view -> view.onGetPosts(new ArrayList<>())),
            () -> Log.d("AndroidNO", "Completed: fetchPosts() on mLocalStorage")
        );

    mNOPostsApi.getLastPosts()
        .retry(3)
        .doOnNext(posts -> mLocalStorage.savePosts(posts))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            posts -> mView.ifPresent(view -> view.onGetPosts(posts)),
            error -> Log.d("AndroidNO", "Error on fetchPosts() on mNOPostsApi", error),
            () -> Log.d("AndroidNO", "Completed: fetchPosts() on mNOPostsApi")
        );
  }
}
