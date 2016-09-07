package org.ar25.androidno.presenters;

import org.ar25.androidno.util.Optional;

public class DetailPresenter {
  private Optional<DetailView> mView = Optional.empty();

  public void setView(DetailView view) {
    mView = Optional.of(view);
  }
  public void unsetView() {
    mView = Optional.empty();
  }
}
