package org.ar25.androidno.api;

import org.ar25.androidno.entities.Post;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;


public interface NOPostsApi {

  @GET("/svity?timestamp=0&created=All&sort_by=created&page=0%2C0")
  Observable<List<Post>> getLastPosts();
}
