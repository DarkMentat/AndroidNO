package org.ar25.androidno.ui;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.ar25.androidno.NOApplication;
import org.ar25.androidno.R;
import org.ar25.androidno.entities.Post;
import org.ar25.androidno.presenters.DetailPresenter;
import org.ar25.androidno.presenters.DetailView;
import org.ar25.androidno.util.Optional;

import javax.inject.Inject;

@EActivity(R.layout.activity_detail)
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
  }

  @Override protected void onStart() {
    super.onStart();
    mDetailPresenter.setView(this);

    mDetailPresenter.fetchPost(postId);
  }
  @Override protected void onStop() {
    super.onStop();
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
}
