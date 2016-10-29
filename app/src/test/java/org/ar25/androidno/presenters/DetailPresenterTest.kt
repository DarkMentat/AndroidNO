package org.ar25.androidno.presenters

import org.ar25.androidno.RxSchedulersOverrideRule
import org.ar25.androidno.api.NOPostsApi
import org.ar25.androidno.db.LocalStorage
import org.ar25.androidno.entities.Post.newPost
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import rx.Observable
import org.mockito.Mockito.`when` as whenever

class DetailPresenterTest {

    @JvmField @Rule val rxSchedulers = RxSchedulersOverrideRule()

    @Mock lateinit var view: DetailView
    @Mock lateinit var api: NOPostsApi
    @Mock lateinit var local: LocalStorage

    val presenter = DetailPresenter()


    @Before fun setUp() {
        MockitoAnnotations.initMocks(this)

        presenter.localStorage = local
        presenter.noPostsApi = api
    }

    @Test fun testFetchPost_WithoutView() {
        presenter.fetchPost(1239)
        presenter.fetchPost(1231)
        presenter.fetchPost(1249)
    }

    @Test fun testFetchPost_Success() {
        presenter.view = view

        val postId = 1239L
        
        val localPost = newPost(postId, "", "", "", "")
        val apiPost = newPost(postId, "", "", "", "", "")
        
        whenever(local.getPost(postId)).thenReturn(localPost).thenReturn(apiPost)
        whenever(api.getPost(postId)).thenReturn(Observable.just(apiPost))

        val inOrder = Mockito.inOrder(view, local, api)

        presenter.fetchPost(postId)

        inOrder.verify(local).getPost(postId)

        inOrder.verify(view).onGetPost(localPost)
        inOrder.verify(view).setLoading()

        inOrder.verify(api).getPost(postId)
        inOrder.verify(local).savePost(apiPost)
        inOrder.verify(local).getPost(postId)
        inOrder.verify(view).onGetPost(apiPost)

        inOrder.verify(view).setLoaded()
    }

    @Test fun testFetchPost_Success_NoLocal() {
        presenter.view = view

        val postId = 1239L

        val apiPost = newPost(postId, "", "", "", "", "")

        whenever(local.getPost(postId)).thenReturn(null).thenReturn(apiPost)
        whenever(api.getPost(postId)).thenReturn(Observable.just(apiPost))

        val inOrder = Mockito.inOrder(view, local, api)

        presenter.fetchPost(postId)

        inOrder.verify(local).getPost(postId)

        inOrder.verify(view).onGetPost(null)
        inOrder.verify(view).setLoading()

        inOrder.verify(api).getPost(postId)
        inOrder.verify(local).savePost(apiPost)
        inOrder.verify(local).getPost(postId)
        inOrder.verify(view).onGetPost(apiPost)

        inOrder.verify(view).setLoaded()
    }

    @Test fun testFetchPost_Success_AlreadyFullLocal() {
        presenter.view = view

        val postId = 1239L

        val localPost = newPost(postId, "", "", "", "", "")
        val apiPost = newPost(postId, "", "", "", "", "")

        whenever(local.getPost(postId)).thenReturn(localPost).thenReturn(apiPost)
        whenever(api.getPost(postId)).thenReturn(Observable.just(apiPost))

        val inOrder = Mockito.inOrder(view, local, api)

        presenter.fetchPost(postId)

        inOrder.verify(local).getPost(postId)

        inOrder.verify(view).onGetPost(localPost)

        inOrder.verify(api).getPost(postId)
        inOrder.verify(local).savePost(apiPost)
        inOrder.verify(local).getPost(postId)
        inOrder.verify(view).onGetPost(apiPost)

        inOrder.verify(view).setLoaded()
    }

    @Test fun testFetchPost_Error() {
        presenter.view = view

        val postId = 1239L

        val localPost = newPost(postId, "", "", "", "")
        val error = RuntimeException("Test error")

        whenever(local.getPost(postId)).thenReturn(localPost)
        whenever(api.getPost(postId)).thenReturn(Observable.error(error))

        val inOrder = Mockito.inOrder(view, local, api)

        presenter.fetchPost(postId)

        inOrder.verify(local).getPost(postId)

        inOrder.verify(view).onGetPost(localPost)
        inOrder.verify(view).setLoading()

        inOrder.verify(api).getPost(postId)
        inOrder.verify(view).onGetError(error)

        inOrder.verify(view).setLoaded()
    }
}