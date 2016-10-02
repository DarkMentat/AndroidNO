package org.ar25.androidno.presenters

import org.ar25.androidno.api.NOPostsApi
import org.ar25.androidno.db.LocalStorage
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject


class MainPresenter {
    @Inject lateinit var localStorage: LocalStorage
    @Inject lateinit var noPostsApi: NOPostsApi

    var view: MainView? = null


    fun fetchPosts() {

        val mainView: MainView = view ?: return


        mainView.setLoading()
        mainView.onGetPosts(localStorage.posts)

        noPostsApi
                .lastPosts
                .retry(3)
                .subscribeOn(Schedulers.io())
                .doOnNext { localStorage.savePosts(it) }
                .flatMap { Observable.just(localStorage.posts) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { posts -> mainView.onGetPosts(posts); mainView.setLoaded() },
                        { error -> mainView.setLoaded() }
                )
    }
}
