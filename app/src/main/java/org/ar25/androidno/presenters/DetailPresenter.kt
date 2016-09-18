package org.ar25.androidno.presenters

import org.ar25.androidno.api.NOPostsApi
import org.ar25.androidno.db.LocalStorage
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

open class DetailPresenter {
    @Inject lateinit var mLocalStorage: LocalStorage
    @Inject lateinit var mNOPostsApi: NOPostsApi

    var view: DetailView? = null

    var localStorageSubscription: Subscription? = null
    var NOPostsApiSubscription: Subscription? = null

    fun fetchPost(id: Long) {

        localStorageSubscription?.unsubscribe()
        NOPostsApiSubscription?.unsubscribe()


        localStorageSubscription = mLocalStorage
                .getPostObservable(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { post -> view?.onGetPost(post) },
                    { error -> view?.onGetPost(null) }
                )

        NOPostsApiSubscription = mNOPostsApi
                .getPost(id)
                .retry(3)
                .subscribeOn(Schedulers.io())
                .subscribe (
                    { post -> mLocalStorage.savePost(post) },
                    { error ->  }
                )
    }
}
