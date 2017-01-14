package org.ar25.androidno.db

import org.ar25.androidno.entities.Post
import org.ar25.androidno.entities.Section


interface LocalStorage {
    companion object {
        const val POSTS_PER_PAGE = 20
    }

    fun savePosts(posts: List<Post>)
    fun savePost(post: Post, updateFavorite:Boolean = false)
    fun getPosts(offset: Int): List<Post>
    fun getPost(id: Long): Post?

    fun getFavoritePosts(offset: Int): List<Post>
    fun getPostsAtSection(section: Section, offset: Int): List<Post>
}