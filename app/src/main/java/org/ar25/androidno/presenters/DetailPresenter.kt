package org.ar25.androidno.presenters

import android.app.Activity
import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import org.ar25.androidno.R
import org.ar25.androidno.api.NOPostsApi
import org.ar25.androidno.db.LocalStorage
import org.ar25.androidno.entities.Post
import org.ar25.androidno.mvp.BasePresenter
import org.ar25.androidno.navigation.ScreenRouterManager
import org.ar25.androidno.permission.PermissionManager
import org.ar25.androidno.ui.DetailActivity
import org.ar25.androidno.util.parseHtmlTextToTokens
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton


@Singleton open class DetailPresenter @Inject constructor(

        val screenRouterManager: ScreenRouterManager,
        permissionManager: PermissionManager,
        val localStorage: LocalStorage,
        val noPostsApi: NOPostsApi

): BasePresenter<DetailView>(permissionManager) {

    var currentPost: Post? = null

    var postId: Long = -1L
    var postSlug: String? = null
    var postUrl: String? = null

    override fun setIntent(intent: Intent) {

        postId = intent.extras?.getLong(DetailActivity.EXTRA_POST_ID, -1L) ?: -1L
        postSlug = null
        postUrl = intent.extras?.getString(DetailActivity.EXTRA_POST_URL)

        if (intent.action == ACTION_VIEW) {
            if (intent.data.pathSegments.size > 1) {
                try {
                    postId = intent.data.pathSegments[1].toLong()
                } catch (e: NumberFormatException){}
            }
        }

        if(intent.data?.pathSegments?.size ?: -1 > 1)
            postSlug = intent.data?.pathSegments?.getOrNull(1)?.takeIf { it.toIntOrNull() == null }

        if(postUrl == null){
            postUrl = if(postId > 0) "http://ar25.org/node/$postId" else "http://ar25.org/article/$postSlug"
        }else{
            postSlug = postUrl?.split("/")?.last()
        }
    }

    fun share() {

        val post = currentPost ?: return

        screenRouterManager.openScreen(Intent(ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, post.header)
            putExtra(Intent.EXTRA_TEXT, postUrl)
        })
    }

    fun openInBrowser() {

        if(view==null)
            return

        val context = view as Activity

        val commonIntent = Intent(ACTION_VIEW, Uri.parse(postUrl))
        val targetIntents = context.packageManager.queryIntentActivities(commonIntent, 0)
                .filter { !it.activityInfo.packageName.contains("org.ar25.androidno") }
                .map { Intent(ACTION_VIEW, Uri.parse(postUrl)).apply { setPackage(it.activityInfo.packageName) } }


        if(targetIntents.size > 1) {
            val chooserIntent = Intent.createChooser(targetIntents[0], context.getString(R.string.open_with))
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetIntents.subList(1, targetIntents.size).toTypedArray())

            screenRouterManager.openScreen(chooserIntent)

        } else if(targetIntents.size == 1) {
            screenRouterManager.openScreen(targetIntents[0])

        } else {
            screenRouterManager.openScreen(commonIntent)
        }
    }

    fun addToFavorites() {

        val post = currentPost ?: return

        post.isFavorite = !post.isFavorite

        localStorage.savePost(post, updateFavorite = true)
    }

    fun fetchPost() {

        val postSlug = postSlug
        if(postSlug == null){
            fetchPost(postId)
            return
        }

        if(postId > 0){
            fetchPost(postId, postSlug)
        }else{
            fetchPost(postSlug)
        }
    }

    private fun fetchPost(slug: String) {

        val detailView : DetailView = view ?: return

        detailView.setLoading()

        noPostsApi
                .getPost(slug)
                .retry(3)
                .subscribeOn(Schedulers.io())
                .doOnNext { localStorage.savePost(it)}
                .doOnNext { currentPost = it }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { post -> detailView.onGetPost(post); detailView.setLoaded() },
                        { error -> detailView.onGetError(error); detailView.setLoaded() }
                )

    }
    private fun fetchPost(id: Long, slug: String) {

        val detailView : DetailView = view ?: return

        localStorage.getPost(id).let {
            detailView.onGetPost(it)

            if(it?.text == null)
                detailView.setLoading()
        }

        noPostsApi
                .getPost(slug)
                .retry(3)
                .subscribeOn(Schedulers.io())
                .doOnNext { localStorage.savePost(it)}
                .flatMap { Observable.just(localStorage.getPost(id)) }
                .map { post -> post?.htmlTokens = parseHtmlTextToTokens(post?.teaser + post?.text); post}
                .doOnNext { currentPost = it }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { detailView.setLoaded() }
                .subscribe(
                        { post -> detailView.onGetPost(post) },
                        { error -> detailView.onGetError(error) }
                )
    }
    private fun fetchPost(id: Long) {

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
            .doOnNext { it.id = id }
            .doOnNext { localStorage.savePost(it)}
            .flatMap { Observable.just(localStorage.getPost(id)) }
            .map { post -> post?.htmlTokens = parseHtmlTextToTokens(post?.teaser + post?.text); post}
            .doOnNext { currentPost = it }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { detailView.setLoaded() }
            .subscribe(
                { post -> detailView.onGetPost(post) },
                { error -> detailView.onGetError(error) }
            )
    }
}
