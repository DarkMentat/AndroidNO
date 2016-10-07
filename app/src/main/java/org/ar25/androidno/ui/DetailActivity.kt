package org.ar25.androidno.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*
import org.ar25.androidno.NOApplication
import org.ar25.androidno.R
import org.ar25.androidno.entities.Post
import org.ar25.androidno.presenters.DetailPresenter
import org.ar25.androidno.presenters.DetailView
import javax.inject.Inject


class DetailActivity : AppCompatActivity(), DetailView {

    companion object {
        const val EXTRA_POST_ID = "EXTRA_POST_ID"
    }

    @Inject lateinit var detailPresenter: DetailPresenter


    val postId: Long by lazy { intent.extras.getLong(EXTRA_POST_ID) }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_detail)

        NOApplication.getNOAppComponent(this).inject(this)

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


    @Suppress("DEPRECATION")
    override fun onGetPost(post: Post?) {

        if(post == null)
            return

        publishDate.text = post.publishDate
        header.text = post.header
        teaser.text = Html.fromHtml(post.teaser)

        post.text?.let { text.text = Html.fromHtml(it) }

        Picasso.with(this).load(post.imageUrl).fetch(object: Callback.EmptyCallback(){
            override fun onSuccess() {
                Picasso.with(this@DetailActivity).load(post.imageUrl).into(image)
            }
        })
    }
    override fun onGetError(error: Throwable) {
        Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
    }

    override fun setLoading() {
        text.visibility = GONE
        loadingIndicator.visibility = VISIBLE
    }
    override fun setLoaded() {
        text.visibility = VISIBLE
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
}
