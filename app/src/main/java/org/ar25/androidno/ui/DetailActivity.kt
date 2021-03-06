package org.ar25.androidno.ui

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*
import org.ar25.androidno.NOApplication
import org.ar25.androidno.R
import org.ar25.androidno.api.ParseErrorException
import org.ar25.androidno.entities.Post
import org.ar25.androidno.mvp.BaseActivity
import org.ar25.androidno.navigation.ScreenRouterManager
import org.ar25.androidno.presenters.DetailPresenter
import org.ar25.androidno.presenters.DetailView
import org.ar25.androidno.util.*
import org.sufficientlysecure.htmltextview.ClickableLocalLinkMovementMethod
import org.sufficientlysecure.htmltextview.HtmlTextView
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import android.view.WindowManager


class DetailActivity : BaseActivity<DetailPresenter, DetailView>(), DetailView {

    companion object {
        const val EXTRA_POST_ID = "EXTRA_POST_ID"
        const val EXTRA_POST_URL = "EXTRA_POST_URL"

        @JvmStatic fun getDetailActivityBuilder(post: Post): ScreenRouterManager.ActivityBuilder {

            val activityBuilder = ScreenRouterManager.ActivityBuilder(DetailActivity::class.java)
            activityBuilder.putArg(EXTRA_POST_ID, post.id)
            activityBuilder.putArg(EXTRA_POST_URL, post.url)

            return activityBuilder
        }
    }

    override fun getMvpView() = this
    override fun onCreateOptionsMenu(menu: Menu?) = inflateOptionsMenu(R.menu.menu_detail, menu)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)

        setContentView(R.layout.activity_detail)

        NOApplication.noAppComponent.inject(this)

        bindActionBar()
    }

    override fun onResume() {
        super.onResume()

        presenter.fetchPost()
    }

    fun bindActionBar() {
        setSupportActionBar(toolbar)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fabAddToFavorite.setOnClickListener {

            presenter.addToFavorites()

            Snackbar
                    .make(parentScrollView, getString(R.string.added_to_favorite), Snackbar.LENGTH_LONG)
                    .setAction(R.string.undo) { presenter.addToFavorites(); fabAddToFavorite.show() }
                    .show()

            fabAddToFavorite.hide()
        }
    }

    private var currentPost: Post? = null
    override fun onGetPost(post: Post?) {

        fun loadPostDetails(post: Post) {

            fillContent(post)

            if(post.imageTitle != null) {
                imageTitle.visibility = VISIBLE
                imageTitle.text = post.imageTitle
            }
            if(post.source != null) {
                source.visibility = VISIBLE
                source.setHtml(getString(R.string.post_source, post.sourceLink, post.source).trim())
            }
            if(post.gamer != null) {
                gamer.visibility = VISIBLE
                gamer.text = getString(R.string.post_gamer, post.gamer).trim()
            }
        }

        if(post == null || post == currentPost)
            return

        currentPost = post

        publishDate.visibility = VISIBLE
        header.visibility = VISIBLE
        image.visibility = VISIBLE
        postMainContent.visibility = VISIBLE

        publishDate.text = post.publishDate
        header.text = post.header

        Picasso.with(this).load(post.imageUrl).fetch {
            Picasso.with(this@DetailActivity).load(post.imageUrl).into(image)
        }

        if(post.text == null) {
            teaserText.setHtml(post.teaser)

        } else if(teaserText.text.isEmpty()) {

            if(!post.isFavorite)
                fabAddToFavorite.show()

            loadPostDetails(post)
        } else {
            postMainContent.animateToTransparent {

                if(!post.isFavorite)
                    Handler().postDelayed({ fabAddToFavorite.show() }, 200L)

                loadPostDetails(post)

                postMainContent.animateToVisible()
            }
        }
    }
    override fun onGetError(error: Throwable) {

        fun showError(text: String){
            Snackbar.make(parentScrollView, text, Snackbar.LENGTH_LONG).show()
        }

        when(error){
            is SocketTimeoutException -> showError(getString(R.string.timeout_error))
            is UnknownHostException -> showError(getString(R.string.unknownhost_error))
            is ParseErrorException ->  showError(getString(R.string.parse_error))
            else -> showError(getString(R.string.unhandled_error, error.message ?: error.toString()))
        }
    }

    override fun setLoading() {
        loadingIndicator.visibility = VISIBLE
    }
    override fun setLoaded() {
        loadingIndicator.visibility = GONE
    }

    override fun showAddedToFavoritesMessage() {
        Toast.makeText(this, presenter.currentPost?.isFavorite?.toString() ?: "", Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.action_settings -> Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
            R.id.action_refresh -> presenter.fetchPost()
            R.id.action_share -> presenter.share()
            R.id.action_open_in_browser -> presenter.openInBrowser()
            android.R.id.home -> finish()
            else -> return super.onOptionsItemSelected(item)
        }

        return true
    }

    private val linkMovementMethod =  ClickableLocalLinkMovementMethod { view, link ->

        val uri = Uri.parse(link.url)

        if( uri.pathSegments.size > 1 &&
                (uri.host == "www.ar25.org" || uri.host == "ar25.org") &&
                (uri.pathSegments[0] == "node" || uri.pathSegments[0] == "article")) {

            startActivity(Intent(this@DetailActivity, DetailActivity::class.java)
                    .setAction(ACTION_VIEW)
                    .setData(uri))

        } else {
            link.onClick(view)
        }
    }
    fun fillContent(post: Post){

        val tokens = post.htmlTokens ?: parseHtmlTextToTokens(post.teaser + post.text + post.comments)

        postMainContent.removeAllViews()
        commentsContent.removeAllViews()

        var firstComment = true

        for (token in tokens){
            when (token) {
                is PostToken.HtmlTextToken -> {
                    val view = layoutInflater.inflate(R.layout.view_html_text_token, postMainContent, false) as HtmlTextView

                    view.setHtml(token.text)

                    if(postMainContent.childCount == 0) {

                        view.setTypeface(null, Typeface.BOLD)
                        view.setTextColor(ActivityCompat.getColor(context, R.color.text_color_black))
                        view.setPadding(0, 0, 0, 24)
                    }

                    view.movementMethod = linkMovementMethod //hack to allow customize behavior on link click

                    postMainContent.addView(view)
                }

                is PostToken.ImageToken -> {
                    val view = layoutInflater.inflate(R.layout.view_image_token, postMainContent, false) as ImageView

                    view.adjustViewBounds = true

                    postMainContent.addView(view)

                    if(token.title.isNotEmpty()) {

                        val title = TextView(this)
                        title.text = token.title
                        title.setPadding(0, 0, 0, 24)
                        postMainContent.addView(title)
                    }

                    Picasso.with(this)
                            .load(token.imageUrl)
                            .placeholder(R.drawable.transparent_placeholder)
                            .error(R.drawable.transparent_imageerror)
                            .fit()
                            .centerInside()
                            .into(view)
                }

                is PostToken.YoutubeVideoToken -> {
                    val view = layoutInflater.inflate(R.layout.view_youtube_token, postMainContent, false)

                    view.setOnClickListener {
                        startActivity(Intent(ACTION_VIEW, Uri.parse(token.youtubeUrl)))
                    }

                    postMainContent.addView(view)
                }

                is PostToken.AudioLinkToken -> {
                    val view = layoutInflater.inflate(R.layout.view_audio_token, postMainContent, false) as TextView

                    view.text = token.title

                    view.setOnClickListener {
                        startActivity(Intent(ACTION_VIEW, Uri.parse(token.audioUrl)))
                    }

                    postMainContent.addView(view)
                }

                is PostToken.Comment -> {
                    val view = layoutInflater.inflate(R.layout.view_comment_token, commentsContent, false)
                    val commentsTitle = view.findViewById<TextView>(R.id.commentsTitle)
                    val authorDate = view.findViewById<TextView>(R.id.authorDate)
                    val contentView = view.findViewById<HtmlTextView>(R.id.content)
                    val avatarView = view.findViewById<ImageView>(R.id.avatar)
                    val divider = view.findViewById<View>(R.id.divider)

                    commentsTitle.visibility = if(firstComment) View.VISIBLE else View.GONE

                    authorDate.setText(token.authorDate)
                    contentView.setHtml(token.htmlContent)

                    contentView.movementMethod = linkMovementMethod //hack to allow customize behavior on link click

                    commentsContent.addView(view)

                    divider.visibility = if(token.last) View.GONE else View.VISIBLE

                    Picasso.with(this)
                        .load(token.avatarUrl)
                        .placeholder(R.drawable.transparent_placeholder)
                        .error(R.drawable.transparent_imageerror)
                        .fit()
                        .into(avatarView)

                    firstComment = false
                }
            }
        }

        val similar = post.similar
        if(similar != null) {
            val view = layoutInflater.inflate(R.layout.view_html_text_token, postMainContent, false) as HtmlTextView
            view.setHtml("<br><b>В тему:</b> $similar")

            if (postMainContent.childCount == 0) {
                view.setTypeface(null, Typeface.BOLD)
                view.setTextColor(ActivityCompat.getColor(context, R.color.text_color_black))
                view.setPadding(0, 0, 0, 24)
            }

            view.movementMethod = linkMovementMethod //hack to allow customize behavior on link click

            postMainContent.addView(view)
        }
    }
}
