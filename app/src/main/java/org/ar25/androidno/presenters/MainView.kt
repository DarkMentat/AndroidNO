package org.ar25.androidno.presenters

import org.ar25.androidno.entities.Post

interface MainView {
    fun onGetPosts(posts: List<Post>)
    fun onGetError(error: Throwable)

    fun setLoading(): Unit
    fun setLoaded(): Unit
}
