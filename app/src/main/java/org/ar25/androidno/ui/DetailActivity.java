package org.ar25.androidno.ui;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;
import org.ar25.androidno.NOApplication;
import org.ar25.androidno.R;
import org.ar25.androidno.entities.Post;
import org.ar25.androidno.presenters.DetailPresenter;
import org.ar25.androidno.presenters.DetailView;
import org.ar25.androidno.util.Optional;

import javax.inject.Inject;

@EActivity(R.layout.activity_detail) @OptionsMenu(R.menu.menu_main)
public class DetailActivity extends AppCompatActivity implements DetailView {

  public static final String EXTRA_POST_ID = "EXTRA_POST_ID";

  @AfterViews void onInjectDependencies() {
    NOApplication.getNOAppComponent(this).inject(this);
  }

  @ViewById(R.id.toolbar) Toolbar mToolbar;

  @ViewById(R.id.publish_date) TextView mPublishDate;
  @ViewById(R.id.header) TextView mHeader;
  @ViewById(R.id.image) ImageView mImage;
  @ViewById(R.id.teaser) TextView mTeaser;
  @ViewById(R.id.text) TextView mText;

  @Inject DetailPresenter mDetailPresenter;

  @Extra(EXTRA_POST_ID) long postId = -1;

  @AfterViews void bindActionBar() {
    setSupportActionBar(mToolbar);
    ActionBar actionBar = getSupportActionBar();

    if(actionBar != null){
      actionBar.setHomeButtonEnabled(true);
      actionBar.setDisplayHomeAsUpEnabled(true);
    }
  }

  @AfterViews void afterCreate() {
    mDetailPresenter.setView(this);

    mDetailPresenter.fetchPost(postId);
  }
  @Override protected void onDestroy() {
    super.onDestroy();
    mDetailPresenter.unsetView();
  }

  @Override public void onGetPost(Optional<Post> post) {
    post.ifPresent(p -> {
      mPublishDate.setText(p.getPublishDate());
      mHeader.setText(p.getHeader());
      mTeaser.setText(Html.fromHtml(p.getTeaser()));

      p.getText().ifPresent(
          text -> mText.setText(Html.fromHtml(text))
      );

      Picasso.with(this).load(p.getImageUrl()).into(mImage);
    });
  }

  @OptionsItem(android.R.id.home) void onActionHome(){
    finish();
  }
  @OptionsItem(R.id.action_settings) void onActionSettings(){
    Toast.makeText(DetailActivity.this, "Settings", Toast.LENGTH_SHORT).show();
  }
}
