package org.ar25.androidno.presenters

import org.ar25.androidno.api.NOPostsApi
import org.ar25.androidno.db.LocalStorage
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

open class DetailPresenter {
    @Inject lateinit var localStorage: LocalStorage
    @Inject lateinit var noPostsApi: NOPostsApi

    var view: DetailView? = null

    fun fetchPost(id: Long) {

        val detailView : DetailView = view ?: return

        detailView.setLoading()
        detailView.onGetPost(localStorage.getPost(id))

        noPostsApi
                .getPost(id)
                .retry(3)
                .subscribeOn(Schedulers.io())
                .doOnNext { localStorage.savePost(it)}
                .flatMap { Observable.just(localStorage.getPost(id)) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { post -> detailView.onGetPost(post); detailView.setLoaded() },
                        { error -> detailView.onGetError(error); detailView.setLoaded() }
                )
    }
}
