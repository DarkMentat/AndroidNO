package org.ar25.androidno.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
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


open class MainActivity : AppCompatActivity(), MainView {

    val postsAdapter = PostsRecyclerViewAdapter(this)

    @Inject lateinit var mainPresenter: MainPresenter

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

    fun bindActionBar() {
        setSupportActionBar(toolbar)
    }
    fun setupRecyclerView() {
        postsList.layoutManager = LinearLayoutManager(this)
        postsList.itemAnimator = DefaultItemAnimator()
        postsList.adapter = postsAdapter

        swipeRefresh.setOnRefreshListener { mainPresenter.fetchPosts() }
        postsNoItemsPlaceHolder.setOnClickListener { fetchPosts() }
    }
    fun bindPresenter() {
        mainPresenter.view = this

        fetchPosts()
    }


    override fun onDestroy() {
        super.onDestroy()
        mainPresenter.view = null
    }


    override fun onGetPosts(posts: List<Post>) {
        postsFetched()

        if (posts.isEmpty())
            return

        postsAdapter.setItems(posts)

        postsList.visibility = VISIBLE
        postsNoItemsPlaceHolder.visibility = GONE
    }


    fun fetchPosts(){
        mainPresenter.fetchPosts()
        swipeRefresh.post { swipeRefresh.isRefreshing = true }
    }
    fun postsFetched(){
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
