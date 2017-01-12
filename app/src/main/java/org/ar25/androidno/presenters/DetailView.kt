package org.ar25.androidno.presenters

import org.ar25.androidno.entities.Post
import org.ar25.androidno.mvp.MvpView


interface DetailView: MvpView {
    fun onGetPost(post: Post?)
    fun onGetError(error: Throwable)

    fun setLoading(): Unit
    fun setLoaded(): Unit

    fun showAddedToFavoritesMessage()
}
