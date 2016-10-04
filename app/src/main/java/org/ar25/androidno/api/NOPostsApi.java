package org.ar25.androidno.api;

import org.ar25.androidno.entities.Post;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;


public interface NOPostsApi {
  public static final int API_RESULTS_PER_PAGE = 20;

  @GET("/svity?timestamp=0&created=All&sort_by=created")
  Observable<List<Post>> getLastPosts(@Query(value = "page", encoded = true) String page);

  @GET("print/{id}")
  Observable<Post> getPost(@Path("id") long id);
}
