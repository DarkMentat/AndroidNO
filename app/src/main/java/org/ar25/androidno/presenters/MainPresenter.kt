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


    fun fetchPosts(page: Int) {

        val mainView: MainView = view ?: return


        if(page == 1)
            mainView.setLoading()

        mainView.onGetPosts(localStorage.getPosts(page))

        noPostsApi
                .getLastPosts("0%2C" + page)
                .startWith(if(page == 1) noPostsApi.getLastPosts("0%2C0") else Observable.empty())
                .subscribeOn(Schedulers.io())
                .doOnNext { localStorage.savePosts(it) }
                .flatMap { Observable.just(localStorage.getPosts(page)) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { posts -> mainView.onGetPosts(posts); mainView.setLoaded() },
                        { error -> mainView.onGetError(error); mainView.setLoaded() }
                )
    }
}
