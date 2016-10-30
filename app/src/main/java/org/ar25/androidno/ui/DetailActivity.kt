package org.ar25.androidno.ui

import android.content.Intent.ACTION_VIEW
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*
import org.ar25.androidno.NOApplication
import org.ar25.androidno.R
import org.ar25.androidno.entities.Post
import org.ar25.androidno.presenters.DetailPresenter
import org.ar25.androidno.presenters.DetailView
import org.ar25.androidno.util.*
import org.sufficientlysecure.htmltextview.HtmlTextView
import javax.inject.Inject


class DetailActivity : AppCompatActivity(), DetailView {

    companion object {
        const val EXTRA_POST_ID = "EXTRA_POST_ID"
    }

    @Inject lateinit var detailPresenter: DetailPresenter


    val postId: Long by lazy {

        if (intent.action == ACTION_VIEW) {
            if (intent.data.pathSegments.size > 1) {
                try {
                    return@lazy intent.data.pathSegments[1].toLong()
                } catch (e: NumberFormatException){
                    return@lazy -1L
                }
            }
        }

        return@lazy intent.extras.getLong(EXTRA_POST_ID, -1L)
    }
    var currentPost: Post? = null

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_detail)

        NOApplication.noAppComponent.inject(this)

        bindActionBar()
        bindPresenter()
    }

    fun bindActionBar() {
        setSupportActionBar(toolbar)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    fun bindPresenter() {
        detailPresenter.view = this

        detailPresenter.fetchPost(postId)
    }

    override fun onDestroy() {
        super.onDestroy()

        detailPresenter.view = null
    }


    override fun onGetPost(post: Post?) {

        if(post == null || currentPost == post)
            return

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
        } else if(currentPost == null || currentPost?.text != null) {
            fillContent(post.teaser + post.text)
        } else {
            postMainContent.animateToTransparent {
                fillContent(post.teaser + post.text)
                postMainContent.animateToVisible()
            }
        }

        currentPost = post
    }
    override fun onGetError(error: Throwable) {
        Snackbar.make(parentScrollView, "Some error happens\n${error.message}", Snackbar.LENGTH_LONG).show()
    }

    override fun setLoading() {
        loadingIndicator.visibility = VISIBLE
    }
    override fun setLoaded() {
        loadingIndicator.visibility = GONE
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.action_settings -> Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
            android.R.id.home -> finish()
            else -> return super.onOptionsItemSelected(item)
        }

        return true
    }

    fun fillContent(html: String?){
        if(html == null) return

        val tokens = parseHtmlTextToTokens(html)

        postMainContent.removeAllViews()

        for (token in tokens){
            when (token) {
                is PostToken.HtmlTextToken -> {
                    val view = layoutInflater.inflate(R.layout.view_html_text_token, postMainContent, false) as HtmlTextView

                    view.setHtml(token.text)

                    postMainContent.addView(view)
                }

                is PostToken.ImageToken -> {
                    val view = layoutInflater.inflate(R.layout.view_image_token, postMainContent, false) as ImageView

                    view.adjustViewBounds = true

                    postMainContent.addView(view)

                    Picasso.with(this)
                            .load(token.imageUrl)
                            .placeholder(R.drawable.transparent_placeholder)
                            .error(R.drawable.transparent_imageerror)
                            .fit()
                            .centerInside()
                            .into(view)
                }
            }
        }
    }
}
