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

public class DetailPresenter {
  @Inject LocalStorage mLocalStorage;
  @Inject NOPostsApi mNOPostsApi;

  private Optional<DetailView> mView = Optional.empty();

  public void setView(DetailView view) {
    mView = Optional.of(view);
  }
  public void unsetView() {
    mView = Optional.empty();
  }

  public void fetchPost(long id){
    mLocalStorage.getPostObservable(id)
        .subscribeOn(Schedulers.io())
        .timeout(2L, SECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            post -> mView.ifPresent(view -> view.onGetPost(Optional.of(post))),
            error -> mView.ifPresent(view -> view.onGetPost(Optional.empty())),
            () -> Log.d("AndroidNO", "Completed: fetchPost() on mLocalStorage")
        );

    mNOPostsApi.getPost(id)
        .retry(3)
        .doOnNext(post -> mLocalStorage.savePost(post))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            post -> mView.ifPresent(view -> view.onGetPost(Optional.of(post))),
            error -> Log.d("AndroidNO", "Error on fetchPost() on mNOPostsApi", error),
            () -> Log.d("AndroidNO", "Completed: fetchPost() on mNOPostsApi")
        );
  }
}
