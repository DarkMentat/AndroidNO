package org.ar25.androidno.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.ar25.androidno.NOApplication
import org.ar25.androidno.R
import org.ar25.androidno.entities.Post
import org.ar25.androidno.presenters.MainPresenter
import org.ar25.androidno.presenters.MainView
import javax.inject.Inject


class MainActivity : AppCompatActivity(), MainView {

    @Inject lateinit var mainPresenter: MainPresenter


    val postsAdapter = PostsRecyclerViewAdapter(this)

    var scrollListenerAlreadyAdded = false
    var currentPage = 1

    val onMoreItems: RecyclerView.OnScrollListener by lazy {
        object : OnLoadMoreEndlessRecyclerView(postsList.layoutManager as LinearLayoutManager) {
            override fun onLoadMore() {
                mainPresenter.fetchPosts(++currentPage)
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        NOApplication.getNOAppComponent(this).inject(this)

        bindActionBar()
        setupRecyclerView()

        bindPresenter()
    }

    fun bindPresenter() {
        mainPresenter.view = this

        mainPresenter.fetchPosts(currentPage)
    }
    fun bindActionBar() {
        setSupportActionBar(toolbar)
    }
    fun setupRecyclerView() {
        postsList.layoutManager = LinearLayoutManager(this)
        postsList.itemAnimator = DefaultItemAnimator()
        postsList.adapter = postsAdapter

        swipeRefresh.setOnRefreshListener { mainPresenter.fetchPosts(1) }
        postsNoItemsPlaceHolder.setOnClickListener { mainPresenter.fetchPosts(1) }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainPresenter.view = null
    }


    override fun onGetPosts(posts: List<Post>) {
        if (posts.isEmpty())
            return

        postsAdapter.updateItems(posts)

        postsList.visibility = VISIBLE
        postsNoItemsPlaceHolder.visibility = GONE
    }

    override fun onGetError(error: Throwable) {
        Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
    }

    override fun setLoading() {
        swipeRefresh.post { swipeRefresh.isRefreshing = true }
    }
    override fun setLoaded() {
        swipeRefresh.isRefreshing = false

        if(!scrollListenerAlreadyAdded) {
            postsList.addOnScrollListener(onMoreItems)
            scrollListenerAlreadyAdded = true
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.action_settings -> Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
            else -> return super.onOptionsItemSelected(item)
        }

        return true
    }
}
