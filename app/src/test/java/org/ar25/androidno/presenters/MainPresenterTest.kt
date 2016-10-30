package org.ar25.androidno.presenters

import org.ar25.androidno.RxSchedulersOverrideRule
import org.ar25.androidno.api.NOPostsApi
import org.ar25.androidno.api.getLastPosts
import org.ar25.androidno.db.LocalStorage
import org.ar25.androidno.entities.Post
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.inOrder
import org.mockito.MockitoAnnotations
import rx.Observable
import org.mockito.Mockito.`when` as whenever

class MainPresenterTest {

    @JvmField @Rule val rxSchedulers = RxSchedulersOverrideRule()

    @Mock lateinit var view: MainView
    @Mock lateinit var api: NOPostsApi
    @Mock lateinit var local: LocalStorage

    val presenter = MainPresenter()

    val localPostsPage0 = listOf(
            Post(0,"header0","0","0","00","000"),
            Post(1,"header1","1","1","11","111"),
            Post(2,"header2","2","2","22"),
            Post(3,"header3","3","3","33"),
            Post(4,"header4","4","4","44")
    )

    val apiPostsPage0 = listOf(
            Post(0,"header0","0","0","0"),
            Post(1,"header1","1","1","1"),
            Post(2,"header2","2","2","2"),
            Post(3,"header3","3","3","3"),
            Post(4,"header4","4","4","4")
    )

    val localPostsPage1 = listOf(
            Post(5,"header5","5","5","55"),
            Post(6,"header6","6","6","66","666"),
            Post(7,"header7","7","7","77","777"),
            Post(8,"header8","8","8","88"),
            Post(9,"header9","9","9","99")
    )

    val apiPostsPage1 = listOf(
            Post(5,"header5","5","5","5"),
            Post(6,"header6","6","6","6"),
            Post(7,"header7","7","7","7"),
            Post(8,"header8","8","8","8"),
            Post(9,"header9","9","9","9")
    )


    @Before fun setUp() {
        MockitoAnnotations.initMocks(this)

        presenter.localStorage = local
        presenter.noPostsApi = api
    }

    @Test fun testFetchPosts_WithoutView() {
        presenter.fetchPosts(0)
        presenter.fetchPosts(1)
    }

    @Test fun testFetchPosts_Page0() {
        presenter.view = view

        whenever(local.getPosts(0)).thenReturn(localPostsPage0).thenReturn(apiPostsPage0)
        whenever(api.getLastPosts(0)).thenReturn(Observable.just(apiPostsPage0))

        val inOrder = inOrder(view, local, api)

        presenter.fetchPosts(0)

        inOrder.verify(view).setLoading()

        inOrder.verify(local).getPosts(0)
        inOrder.verify(view).onGetPosts(localPostsPage0, 0)

        inOrder.verify(api).getLastPosts(0)
        inOrder.verify(local).savePosts(apiPostsPage0)
        inOrder.verify(view).onGetPosts(apiPostsPage0, 0)

        inOrder.verify(view).setLoaded()
    }

    @Test fun testFetchPosts_Page0_WithoutCache() {
        presenter.view = view

        whenever(local.getPosts(0)).thenReturn(apiPostsPage0)
        whenever(api.getLastPosts(0)).thenReturn(Observable.just(apiPostsPage0))

        val inOrder = inOrder(view, local, api)

        presenter.fetchPosts(0, withCached = false)

        inOrder.verify(view).setLoading()

        inOrder.verify(api).getLastPosts(0)
        inOrder.verify(local).savePosts(apiPostsPage0)
        inOrder.verify(view).onGetPosts(apiPostsPage0, 0)

        inOrder.verify(view).setLoaded()
    }

    @Test fun testFetchPosts_Page1() {
        presenter.view = view

        whenever(local.getPosts(1)).thenReturn(localPostsPage1).thenReturn(apiPostsPage1)
        whenever(api.getLastPosts(1)).thenReturn(Observable.just(apiPostsPage1))

        val inOrder = inOrder(view, local, api)

        presenter.fetchPosts(1)

        inOrder.verify(local).getPosts(1)
        inOrder.verify(view).onGetPosts(localPostsPage1, 1)

        inOrder.verify(api).getLastPosts(1)
        inOrder.verify(local).savePosts(apiPostsPage1)
        inOrder.verify(view).onGetPosts(apiPostsPage1, 1)

        inOrder.verify(view).setLoaded()
    }

    @Test fun testFetchPosts_Page0_NoLocal() {
        presenter.view = view

        whenever(local.getPosts(0)).thenReturn(listOf()).thenReturn(apiPostsPage0)
        whenever(api.getLastPosts(0)).thenReturn(Observable.just(apiPostsPage0))

        val inOrder = inOrder(view, local, api)

        presenter.fetchPosts(0)

        inOrder.verify(view).setLoading()

        inOrder.verify(local).getPosts(0)
        inOrder.verify(view).onGetPosts(listOf(), 0)

        inOrder.verify(api).getLastPosts(0)
        inOrder.verify(local).savePosts(apiPostsPage0)
        inOrder.verify(view).onGetPosts(apiPostsPage0, 0)

        inOrder.verify(view).setLoaded()
    }

    @Test fun testFetchPosts_Page0_Error() {
        presenter.view = view

        val error = RuntimeException("Test error")

        whenever(local.getPosts(0)).thenReturn(localPostsPage0)
        whenever(api.getLastPosts(0)).thenReturn(Observable.error(error))

        val inOrder = inOrder(view, local, api)

        presenter.fetchPosts(0)

        inOrder.verify(view).setLoading()

        inOrder.verify(local).getPosts(0)
        inOrder.verify(view).onGetPosts(localPostsPage0, 0)

        inOrder.verify(api).getLastPosts(0)
        inOrder.verify(view).onGetError(error)
        inOrder.verify(view).setLoaded()
    }
}