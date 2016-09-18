package org.ar25.androidno.presenters

import org.ar25.androidno.entities.Post

interface DetailView {
    fun onGetPost(post: Post?)
}
