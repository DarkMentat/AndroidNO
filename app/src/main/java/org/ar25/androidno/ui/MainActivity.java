package org.ar25.androidno.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;
import org.ar25.androidno.NOApplication;
import org.ar25.androidno.R;
import org.ar25.androidno.entities.Post;
import org.ar25.androidno.presenters.MainPresenter;
import org.ar25.androidno.presenters.MainView;

import java.util.List;

import javax.inject.Inject;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


@EActivity(R.layout.activity_main) @OptionsMenu(R.menu.menu_main)
public class MainActivity extends AppCompatActivity implements MainView {

  @AfterViews void onInjectDependencies() {
    NOApplication.getNOAppComponent(this).inject(this);
  }

  @ViewById(R.id.toolbar) Toolbar mToolbar;
  @ViewById(R.id.swipe_refresh) SwipeRefreshLayout mSwipeRefreshLayout;

  @ViewById(R.id.recycler_view) RecyclerView mPosts;
  @ViewById(R.id.no_items) TextView mPostsPlaceHolder;

  private PostsRecyclerViewAdapter mPostsAdapter = new PostsRecyclerViewAdapter(this);

  @Inject MainPresenter mMainPresenter;

  @AfterViews void bindActionBar() {
    setSupportActionBar(mToolbar);
  }
  @AfterViews void setupRecyclerView(){
    LinearLayoutManager llm = new LinearLayoutManager(this);
    mPosts.setLayoutManager(llm);
    mPosts.setItemAnimator(new DefaultItemAnimator());
    mPosts.setAdapter(mPostsAdapter);

    mSwipeRefreshLayout.setOnRefreshListener(() -> mMainPresenter.fetchPosts());
    mPostsPlaceHolder.setOnClickListener((view) -> mMainPresenter.fetchPosts());
  }

  @Override protected void onStart() {
    super.onStart();
    mMainPresenter.setView(this);

    mMainPresenter.fetchPosts();
  }
  @Override protected void onStop() {
    super.onStop();
    mMainPresenter.unsetView();
  }


  @Override public void onGetPosts(List<Post> posts) {
    mSwipeRefreshLayout.setRefreshing(false);

    if(!posts.isEmpty()){
      mPostsAdapter.setItems(posts);
    }

    if(mPostsAdapter.isEmpty()){
      mPosts.setVisibility(GONE);
      mPostsPlaceHolder.setVisibility(VISIBLE);
    }else {
      mPosts.setVisibility(VISIBLE);
      mPostsPlaceHolder.setVisibility(GONE);
    }
  }

  @OptionsItem(R.id.action_settings) void onActionSettings(){
    Toast.makeText(MainActivity.this, "Settings", Toast.LENGTH_SHORT).show();
  }
}
