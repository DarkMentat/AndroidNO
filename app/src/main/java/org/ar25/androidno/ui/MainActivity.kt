package org.ar25.androidno.ui

import android.os.Bundle
import android.support.design.widget.Snackbar
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
    var currentPage = 0

    val onMoreItems: RecyclerView.OnScrollListener by lazy {
        OnLoadMoreEndlessRecyclerView(postsList.layoutManager as LinearLayoutManager) {
            mainPresenter.fetchPosts(currentPage + 1)
            postsAdapter.enableLoadingIndicator()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        NOApplication.noAppComponent.inject(this)

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
        postsList.addOnScrollListener(onMoreItems)

        swipeRefresh.setOnRefreshListener { mainPresenter.fetchPosts(0) }
        postsNoItemsPlaceHolder.setOnClickListener { mainPresenter.fetchPosts(0) }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainPresenter.view = null
    }


    override fun onGetPosts(posts: List<Post>, page: Int) {
        if (posts.isEmpty())
            return

        postsAdapter.updateItems(posts)

        if(page > currentPage)
            currentPage = page

        postsList.visibility = VISIBLE
        postsNoItemsPlaceHolder.visibility = GONE
    }

    override fun onGetError(error: Throwable) {
        Snackbar.make(swipeRefresh, "Some error happens\n${error.message}", Snackbar.LENGTH_LONG).show()
    }

    override fun setLoading() {
        swipeRefresh.post { swipeRefresh.isRefreshing = true }
    }
    override fun setLoaded() {
        postsAdapter.enableLoadMoreButton { mainPresenter.fetchPosts(currentPage + 1) }

        swipeRefresh.isRefreshing = false
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.action_settings -> Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
            else -> return super.onOptionsItemSelected(item)
        }

        return true
    }
}
