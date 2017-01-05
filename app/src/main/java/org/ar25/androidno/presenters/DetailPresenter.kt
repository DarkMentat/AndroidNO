package org.ar25.androidno.presenters

import org.ar25.androidno.api.NOPostsApi
import org.ar25.androidno.db.LocalStorage
import org.ar25.androidno.mvp.BasePresenter
import org.ar25.androidno.permission.PermissionManager
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton


@Singleton open class DetailPresenter @Inject constructor(

        permissionManager: PermissionManager,
        val localStorage: LocalStorage,
        val noPostsApi: NOPostsApi

): BasePresenter<DetailView>(permissionManager) {

    fun fetchPost(slug: String) {

        val detailView : DetailView = view ?: return

        detailView.setLoading()

        noPostsApi
                .getPost(slug)
                .retry(3)
                .subscribeOn(Schedulers.io())
                .doOnNext { localStorage.savePost(it)}
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { post -> detailView.onGetPost(post); detailView.setLoaded() },
                        { error -> detailView.onGetError(error); detailView.setLoaded() }
                )

    }
    fun fetchPost(id: Long) {

        val detailView : DetailView = view ?: return

        localStorage.getPost(id).let {
            detailView.onGetPost(it)

            if(it?.text == null)
                detailView.setLoading()
        }

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
