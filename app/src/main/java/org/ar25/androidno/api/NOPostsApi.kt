package org.ar25.androidno.api

import org.ar25.androidno.entities.Post

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import rx.Observable


interface NOPostsApi {

    companion object {
        const val API_RESULTS_PER_PAGE = 20
    }

    @GET("/svity?timestamp=0&created=All&sort_by=created")
    fun getLastPosts(@Query(value = "page", encoded = true) page: String): Observable<List<Post>>

    @GET("/node/{id}")
    fun getPost(@Path("id") id: Long): Observable<Post>

    @GET("/article/{slug}")
    fun getPost(@Path("slug") slug: String): Observable<Post>
}


fun NOPostsApi.getLastPosts(page: Int): Observable<List<Post>>{
    return getLastPosts("0%2C" + page)
}