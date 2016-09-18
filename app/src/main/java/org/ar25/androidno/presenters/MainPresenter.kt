package org.ar25.androidno.presenters

import org.ar25.androidno.api.NOPostsApi
import org.ar25.androidno.db.LocalStorage
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject


class MainPresenter {
    @Inject lateinit var mLocalStorage: LocalStorage
    @Inject lateinit var mNOPostsApi: NOPostsApi

    var view: MainView? = null


    fun fetchPosts() {
        mLocalStorage
                .postsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { posts -> view?.onGetPosts(posts) },
                    { error -> view?.onGetPosts(emptyList()) }
                )

        mNOPostsApi
                .lastPosts
                .retry(3)
                .subscribeOn(Schedulers.io())
                .subscribe(
                        { posts -> mLocalStorage.savePosts(posts) },
                        { error -> })
    }
}
