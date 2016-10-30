package org.ar25.androidno.presenters

import org.ar25.androidno.api.NOPostsApi
import org.ar25.androidno.api.getLastPosts
import org.ar25.androidno.db.LocalStorage
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject


class MainPresenter {
    @Inject lateinit var localStorage: LocalStorage
    @Inject lateinit var noPostsApi: NOPostsApi

    var view: MainView? = null


    fun fetchPosts(page: Int, withCached: Boolean = true) {

        val mainView: MainView = view ?: return


        if(page == 0)
            mainView.setLoading()

        if(withCached)
            mainView.onGetPosts(localStorage.getPosts(page), page)

        noPostsApi
                .getLastPosts(page)
                .subscribeOn(Schedulers.io())
                .doOnNext { localStorage.savePosts(it) }
                .switchMap { Observable.just(localStorage.getPosts(page)) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { posts -> mainView.onGetPosts(posts, page); mainView.setLoaded() },
                        { error -> mainView.onGetError(error); mainView.setLoaded() }
                )
    }
}
