package org.ar25.androidno.ui;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.ar25.androidno.NOApplication;
import org.ar25.androidno.R;
import org.ar25.androidno.presenters.DetailPresenter;
import org.ar25.androidno.presenters.DetailView;

import javax.inject.Inject;

@EActivity(R.layout.activity_detail)
public class DetailActivity extends AppCompatActivity implements DetailView {

  @AfterViews void onInjectDependencies() {
    NOApplication.getNOAppComponent(this).inject(this);
  }

  @ViewById(R.id.toolbar) Toolbar mToolbar;

  @Inject DetailPresenter mDetailPresenter;

  @AfterViews void bindActionBar() {
    setSupportActionBar(mToolbar);
  }

  @Override protected void onStart() {
    super.onStart();
    mDetailPresenter.setView(this);
  }
  @Override protected void onStop() {
    super.onStop();
    mDetailPresenter.unsetView();
  }
}
