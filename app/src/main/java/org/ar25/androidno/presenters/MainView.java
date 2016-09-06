package org.ar25.androidno.presenters;

import org.ar25.androidno.entities.Post;

import java.util.List;

public interface MainView {
  void onGetPosts(List<Post> posts);
}
