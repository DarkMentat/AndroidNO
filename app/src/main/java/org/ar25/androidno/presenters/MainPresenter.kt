package org.ar25.androidno.presenters

import org.ar25.androidno.api.NOPostsApi
import org.ar25.androidno.api.getLastPosts
import org.ar25.androidno.db.LocalStorage
import org.ar25.androidno.entities.Post
import org.ar25.androidno.mvp.BasePresenter
import org.ar25.androidno.navigation.ScreenRouterManager
import org.ar25.androidno.permission.PermissionManager
import org.ar25.androidno.ui.DetailActivity
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton


@Singleton open class MainPresenter @Inject constructor(

        val screenRouterManager: ScreenRouterManager,
        permissionManager: PermissionManager,
        val localStorage: LocalStorage,
        val noPostsApi: NOPostsApi

): BasePresenter<MainView>(permissionManager) {

    val postsPerPage = LocalStorage.POSTS_PER_PAGE

    enum class Section{
        LatestPosts,
        Favorites
    }

    var section = Section.LatestPosts



    fun fetchPosts(page: Int, withCached: Boolean = true) {

        val mainView: MainView = view ?: return


        when(section) {

            Section.LatestPosts -> fetchLatestPosts(mainView, page, withCached)

            Section.Favorites -> fetchFavorites(mainView, page)
        }

    }

    private fun fetchFavorites(mainView: MainView, page: Int) {

        mainView.onGetPosts(localStorage.getFavoritePosts(page), page)
    }

    private fun fetchLatestPosts(mainView: MainView, page: Int, withCached: Boolean) {

        if(page == 0)
            mainView.setLoading()

        if (withCached)
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

    fun onItemClick(post: Post) {

        screenRouterManager.openScreen(DetailActivity.getDetailActivityBuilder(post))
    }
}
