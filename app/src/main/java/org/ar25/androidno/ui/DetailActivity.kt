package org.ar25.androidno.ui

import android.animation.Animator
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent.ACTION_MOVE
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.webkit.WebSettings.LOAD_NO_CACHE
import android.webkit.WebSettings.LayoutAlgorithm.SINGLE_COLUMN
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*
import kotlinx.android.synthetic.main.content_main.*
import org.ar25.androidno.NOApplication
import org.ar25.androidno.R
import org.ar25.androidno.entities.Post
import org.ar25.androidno.presenters.DetailPresenter
import org.ar25.androidno.presenters.DetailView
import org.ar25.androidno.util.fetch
import java.util.regex.Pattern
import javax.inject.Inject


class DetailActivity : AppCompatActivity(), DetailView {

    companion object {
        const val EXTRA_POST_ID = "EXTRA_POST_ID"
    }

    @Inject lateinit var detailPresenter: DetailPresenter


    val postId: Long by lazy { intent.extras.getLong(EXTRA_POST_ID) }
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

        postText.setOnTouchListener { view, event -> event.action == ACTION_MOVE }
        postText.setBackgroundColor(Color.argb(1, 0, 0, 0))
        postText.settings.layoutAlgorithm = SINGLE_COLUMN
        postText.settings.cacheMode = LOAD_NO_CACHE
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




    @Suppress("DEPRECATION")
    override fun onGetPost(post: Post?) {

        if(post == null || currentPost == post)
            return

        currentPost = post

        publishDate.text = post.publishDate
        header.text = post.header

        Picasso.with(this).load(post.imageUrl).fetch {
            Picasso.with(this@DetailActivity).load(post.imageUrl).into(image)
        }

        if(post.text == null){
            unloadedText.text = post.teaser
            return
        }

        postText.loadData(prepareHtmlForWebView(post.teaser + post.text), "text/html; charset=utf-8", "utf-8")

        unloadedText.animateToTransparent {
            unloadedText.visibility = GONE

            postText.animateToVisible()
        }
    }
    override fun onGetError(error: Throwable) {
        Snackbar.make(swipeRefresh, "Some error happens", Snackbar.LENGTH_LONG).show()
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

    fun prepareHtmlForWebView(html: String): String {

        val p = Pattern.compile("style=\"width: \\d+px; height: \\d+px;\"")
        val m = p.matcher(html)

        return "<style type='text/css'>img{max-width:100%; height:auto;}div,p,span,a{max-width:100%;}</style><body style='margin:0;padding:0;'>${m.replaceAll("")}</body>"

    }

    fun View.animateToTransparent(afterAnimation: () -> Unit = {}){
        this.alpha = 1.0f

        this.animate().alpha(0.0f).setDuration(500L).setListener(
                object: Animator.AnimatorListener{
                    override fun onAnimationRepeat(animation: Animator) {}
                    override fun onAnimationStart(animation: Animator) {}
                    override fun onAnimationCancel(animation: Animator) {}

                    override fun onAnimationEnd(animation: Animator) = afterAnimation()
        })
    }
    fun View.animateToVisible(afterAnimation: () -> Unit = {}){
        this.alpha = 0.0f

        this.animate().alpha(1.0f).setDuration(500L).setListener(
                object: Animator.AnimatorListener{
                    override fun onAnimationRepeat(animation: Animator) {}
                    override fun onAnimationStart(animation: Animator) {}
                    override fun onAnimationCancel(animation: Animator) {}

                    override fun onAnimationEnd(animation: Animator) = afterAnimation()
        })
    }
}
