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
import org.ar25.androidno.entities.Section
import org.ar25.androidno.mvp.BaseActivity
import org.ar25.androidno.presenters.MainPresenter
import org.ar25.androidno.presenters.MainPresenter.NavSection.*
import org.ar25.androidno.presenters.MainView
import org.ar25.androidno.util.inflateOptionsMenu
import java.net.SocketTimeoutException
import java.net.UnknownHostException


class MainActivity : BaseActivity<MainPresenter, MainView>(), MainView {

    val postsAdapter = PostsRecyclerViewAdapter(this)
    var currentPage = 0
    var requestingPage = 0

    var shouldScrollUp = false

    val onMoreItems by lazy {
        OnLoadMoreEndlessRecyclerView(postsList.layoutManager as LinearLayoutManager) {
            fetchPage(currentPage + 1)
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
        setupNavDrawer()
    }

    fun bindActionBar() {
        setSupportActionBar(toolbar)
    }

    fun setupNavDrawer() {

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)

        for (i in 0..navigationView.childCount - 1) {
            navigationView.getChildAt(i)?.overScrollMode = View.OVER_SCROLL_NEVER
            navigationView.getChildAt(i)?.isVerticalScrollBarEnabled = false
        }

        toggle.syncState()

        fun loadNavPage(title: String, navSection: MainPresenter.NavSection, section: Section? = null, withoutFetch: Boolean = false): Boolean {

            sectionTitle.text = title
            postsAdapter.removeAllItems()
            postsList.scrollToPosition(0)
            presenter.navSection = navSection
            presenter.section = section
            swipeRefresh.isEnabled = navSection != Favorites
            postsNoItemsPlaceHolder.setText(if (navSection == Favorites) R.string.favorites_no_items else R.string.inet_no_items)

            if(!withoutFetch)
                fetchFirstPage()

            return true
        }

        loadNavPage(getString(R.string.title_latest_posts), LatestPosts)
        navigationView.menu.getItem(0).isChecked = true

        navigationView.setNavigationItemSelectedListener { menuItem ->

            navigationView.setCheckedItem(menuItem.itemId)
            drawerLayout.closeDrawers()

            when(menuItem.itemId) {

                R.id.navLatestPosts -> loadNavPage(getString(R.string.title_latest_posts), LatestPosts)
                R.id.navFavorites -> loadNavPage(getString(R.string.title_favorites), Favorites)

                R.id.sectionCommonSocial -> loadNavPage(getString(R.string.sectionCommonSocial), Section, Section.CommonSocial)
                R.id.sectionSensarProject -> loadNavPage(getString(R.string.sectionSensarProject), Section, Section.SensarProject)
                R.id.sectionEthnonetwork -> loadNavPage(getString(R.string.sectionEthnonetwork), Section, Section.Ethnonetwork)
                R.id.sectionAltPolitics -> loadNavPage(getString(R.string.sectionAltPolitics), Section, Section.AltPolitics)
                R.id.sectionUnknownUkraine -> loadNavPage(getString(R.string.sectionUnknownUkraine), Section, Section.UnknownUkraine)
                R.id.sectionMeetingClub -> loadNavPage(getString(R.string.sectionMeetingClub), Section, Section.MeetingClub)
                R.id.sectionWorldAtWeek -> loadNavPage(getString(R.string.sectionWorldAtWeek), Section, Section.WorldAtWeek)
                R.id.sectionCulture -> loadNavPage(getString(R.string.sectionCulture), Section, Section.Culture)
                R.id.sectionArt -> loadNavPage(getString(R.string.sectionArt), Section, Section.Art)
                R.id.sectionScience -> loadNavPage(getString(R.string.sectionScience), Section, Section.Science)
                R.id.sectionEducation -> loadNavPage(getString(R.string.sectionEducation), Section, Section.Education)
                R.id.sectionEconomicsAndBusiness -> loadNavPage(getString(R.string.sectionEconomicsAndBusiness), Section, Section.EconomicsAndBusiness)
                R.id.sectionLifeAroundUs -> loadNavPage(getString(R.string.sectionLifeAroundUs), Section, Section.LifeAroundUs)

                else -> false
            }
        }
    }

    fun fetchPage(page: Int){

        requestingPage = page
        presenter.fetchPosts(page)
    }
    fun fetchFirstPage(withCached: Boolean = true){

        onMoreItems.reset()

        currentPage = 0
        requestingPage = 0

        presenter.fetchPosts(0, withCached = withCached)
    }

    fun setupRecyclerView() {
        postsList.layoutManager = LinearLayoutManager(this)
        postsList.itemAnimator = DefaultItemAnimator()
        postsList.adapter = postsAdapter
        postsList.addOnScrollListener(onMoreItems)
        postsAdapter.onItemClick = { presenter?.onItemClick(it) }
        postsAdapter.onAddToFavorites = { presenter?.onAddToFavorites(it) }
        postsAdapter.onRemoveFromFavorites = { presenter?.onRemoveFromFavorites(it) }

        swipeRefresh.setOnRefreshListener { fetchFirstPage(withCached = false) }
        postsNoItemsPlaceHolder.setOnClickListener { fetchFirstPage() }
    }

    override fun onResume() {
        super.onResume()

        if(postsAdapter.postsCount == 0) {
            fetchPage(currentPage)
        }
    }

    override fun onGetPosts(posts: List<Post>, page: Int) {

        if(shouldScrollUp) {
            postsAdapter.removeAllItems()
        }

        if (posts.isNotEmpty()) {

            val currentPosts = postsAdapter.getPosts()
            val updated = posts.filter { currentPosts.contains(it) }.size

            if(updated > 0) {

                val visiblePos = (postsList.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                if(!isLoading && visiblePos >= postsAdapter.postsCount - updated)
                    postsList.scrollToPosition(postsAdapter.postsCount - updated)
            }

            postsAdapter.updateItems(posts)
        }

        if(postsAdapter.postsCount > 0) {

            postsList.visibility = VISIBLE
            postsNoItemsPlaceHolder.visibility = GONE
        } else {

            postsList.visibility = GONE
            postsNoItemsPlaceHolder.visibility = VISIBLE
        }

        if(page > currentPage)
            currentPage = page

        if(posts.isNotEmpty() && posts.size < 5) {

            fetchPage(currentPage + 1)
        }

        if(shouldScrollUp) {
            postsList.smoothScrollToPosition(0)
            shouldScrollUp = false
        }

        postsAdapter.enabledBottomLoadedView = posts.isNotEmpty() || isLoading
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

    private var unresolvedRequests = 0
    private var isLoading = false
    override fun setLoading() {

        unresolvedRequests++
        isLoading = true

        postsAdapter.enableLoadingIndicator()

        if(requestingPage == 0)
            swipeRefresh.post { swipeRefresh.isRefreshing = true }
    }
    override fun setLoaded() {

        unresolvedRequests--

        isLoading = unresolvedRequests != 0

        postsAdapter.enableLoadMoreButton { fetchPage(currentPage + 1) }

        swipeRefresh.isRefreshing = false
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.action_settings -> Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
            R.id.action_refresh -> {  fetchFirstPage(withCached = false); shouldScrollUp = true }
            else -> return super.onOptionsItemSelected(item)
        }

        return true
    }
}
