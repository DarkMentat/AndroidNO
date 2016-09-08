package org.ar25.androidno.presenters;

import org.ar25.androidno.entities.Post;
import org.ar25.androidno.util.Optional;

public interface DetailView {
  void onGetPost(Optional<Post> post);
}
