package org.ar25.androidno.ui

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.ar25.androidno.NOApplication
import org.ar25.androidno.R
import org.ar25.androidno.api.ParseErrorException
import org.ar25.androidno.entities.Post
import org.ar25.androidno.mvp.BaseActivity
import org.ar25.androidno.presenters.MainPresenter
import org.ar25.androidno.presenters.MainView
import org.ar25.androidno.util.inflateOptionsMenu
import org.ar25.androidno.util.trueAnd
import java.net.SocketTimeoutException
import java.net.UnknownHostException


class MainActivity : BaseActivity<MainPresenter, MainView>(), MainView {

    val postsAdapter = PostsRecyclerViewAdapter(this)
    var currentPage = 0

    val onMoreItems by lazy {
        OnLoadMoreEndlessRecyclerView(postsList.layoutManager as LinearLayoutManager) {
            presenter.fetchPosts(currentPage + 1)
            postsAdapter.enableLoadingIndicator()
        }
    }

    override fun getMvpView() = this
    override fun onCreateOptionsMenu(menu: Menu?) = inflateOptionsMenu(R.menu.menu_main, menu)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        NOApplication.noAppComponent.inject(this)

        bindActionBar()
        setupRecyclerView()
    }

    fun bindActionBar() {
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)

        sectionTitle.text = getString(R.string.title_latest_posts)
        postsAdapter.removeAllItems()
        presenter.section = MainPresenter.Section.LatestPosts
        swipeRefresh.isEnabled = true
        navigationView.menu.getItem(0).isChecked = true

        for (i in 0..navigationView.childCount - 1) {
            navigationView.getChildAt(i).overScrollMode = View.OVER_SCROLL_NEVER
        }

        toggle.syncState()

        navigationView.setNavigationItemSelectedListener { menuItem ->

            menuItem.isChecked = true
            drawerLayout.closeDrawers()

            when(menuItem.itemId) {

                R.id.navLatestPosts -> trueAnd {
                    sectionTitle.text = getString(R.string.title_latest_posts)
                    postsAdapter.removeAllItems()
                    presenter.section = MainPresenter.Section.LatestPosts
                    swipeRefresh.isEnabled = true
                    presenter.fetchPosts(0)
                }

                R.id.navFavorites -> trueAnd {
                    sectionTitle.text = getString(R.string.title_favorites)
                    postsAdapter.removeAllItems()
                    presenter.section = MainPresenter.Section.Favorites
                    swipeRefresh.isEnabled = false
                    presenter.fetchPosts(0)
                }

                else -> false
            }
        }
    }
    fun setupRecyclerView() {
        postsList.layoutManager = LinearLayoutManager(this)
        postsList.itemAnimator = DefaultItemAnimator()
        postsList.adapter = postsAdapter
        postsList.addOnScrollListener(onMoreItems)
        postsAdapter.onItemClick = { presenter?.onItemClick(it) }

        swipeRefresh.setOnRefreshListener { presenter.fetchPosts(0, withCached = false) }
        postsNoItemsPlaceHolder.setOnClickListener { presenter.fetchPosts(0) }
    }

    override fun onResume() {
        super.onResume()

        if(postsAdapter.postsCount == 0) {
            presenter.fetchPosts(currentPage)
        }
    }

    override fun onGetPosts(posts: List<Post>, page: Int) {

        postsAdapter.enabledBottomLoadedView = posts.size == presenter.postsPerPage

        if (posts.isEmpty())
            return

        postsAdapter.updateItems(posts)

        if(page > currentPage)
            currentPage = page

        postsList.visibility = VISIBLE
        postsNoItemsPlaceHolder.visibility = GONE

        if(page == 0)
            postsList.smoothScrollToPosition(0)
    }

    override fun onGetError(error: Throwable) {

        fun showError(text: String){
            Snackbar.make(swipeRefresh, text, Snackbar.LENGTH_LONG).show()
        }

        when(error){
            is SocketTimeoutException -> showError(getString(R.string.timeout_error))
            is UnknownHostException -> showError(getString(R.string.unknownhost_error))
            is ParseErrorException ->  showError(getString(R.string.parse_error))
            else -> showError(getString(R.string.unhandled_error, error.message ?: error.toString()))
        }
    }

    override fun setLoading() {
        swipeRefresh.post { swipeRefresh.isRefreshing = true }
    }
    override fun setLoaded() {
        postsAdapter.enableLoadMoreButton { presenter.fetchPosts(currentPage + 1) }

        swipeRefresh.isRefreshing = false
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.action_settings -> Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
            R.id.action_refresh -> presenter.fetchPosts(0, withCached = false)
            else -> return super.onOptionsItemSelected(item)
        }

        return true
    }
}
