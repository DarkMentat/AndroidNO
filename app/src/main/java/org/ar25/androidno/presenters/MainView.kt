package org.ar25.androidno.presenters

import org.ar25.androidno.entities.Post

interface MainView {
    fun onGetPosts(posts: List<Post>)

    fun setLoading(): Unit
    fun setLoaded(): Unit
}
