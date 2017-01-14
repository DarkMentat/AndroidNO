package org.ar25.androidno.presenters

import org.ar25.androidno.api.NOPostsApi
import org.ar25.androidno.api.getLastPosts
import org.ar25.androidno.api.getPostsAtSection
import org.ar25.androidno.db.LocalStorage
import org.ar25.androidno.entities.Post
import org.ar25.androidno.entities.Section
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

    enum class NavSection {
        LatestPosts,
        Favorites,
        Section
    }

    var navSection = NavSection.LatestPosts
    var section: Section? = null



    fun fetchPosts(page: Int, withCached: Boolean = true) {

        val mainView: MainView = view ?: return


        when(navSection) {

            NavSection.LatestPosts -> fetchLatestPosts(mainView, page, withCached)

            NavSection.Favorites -> fetchFavorites(mainView, page)

            NavSection.Section -> section?.let { fetchSectionPosts(mainView, it, page, withCached) }
        }

    }

    private fun fetchFavorites(mainView: MainView, page: Int) {

        mainView.onGetPosts(localStorage.getFavoritePosts(page), page)
    }

    private fun fetchSectionPosts(mainView: MainView, section: Section, page: Int, withCached: Boolean) {

        if(page == 0)
            mainView.setLoading()

        if (withCached)
            mainView.onGetPosts(localStorage.getPostsAtSection(section, page), page)

        noPostsApi
                .getPostsAtSection(section, page)
                .subscribeOn(Schedulers.io())
                .doOnNext { it.forEach { post -> post.section = section.apiSlug } }
                .doOnNext { localStorage.savePosts(it) }
                .switchMap { Observable.just(localStorage.getPostsAtSection(section, page)) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { posts -> mainView.onGetPosts(posts, page); mainView.setLoaded() },
                        { error -> mainView.onGetError(error); mainView.setLoaded() }
                )
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

    fun onAddToFavorites(post: Post) {

        post.isFavorite = true

        localStorage.savePost(post, updateFavorite = true)
    }

    fun  onRemoveFromFavorites(post: Post) {

        post.isFavorite = false

        localStorage.savePost(post, updateFavorite = true)
    }
}
