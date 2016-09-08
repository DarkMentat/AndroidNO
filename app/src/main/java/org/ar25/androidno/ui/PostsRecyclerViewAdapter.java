package org.ar25.androidno.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.ar25.androidno.R;
import org.ar25.androidno.entities.Post;

import java.util.ArrayList;
import java.util.List;

import static org.ar25.androidno.ui.DetailActivity.EXTRA_POST_ID;

public class PostsRecyclerViewAdapter extends RecyclerView.Adapter<PostsRecyclerViewAdapter.PostViewHolder>{
  public static class PostViewHolder extends RecyclerView.ViewHolder {

    View mCard;
    TextView mPublishDate;
    TextView mHeader;
    ImageView mImage;
    TextView mTeaser;

    PostViewHolder(View itemView) {
      super(itemView);
      mHeader = (TextView)itemView.findViewById(R.id.header);
      mPublishDate = (TextView)itemView.findViewById(R.id.publish_date);
      mImage = ((ImageView) itemView.findViewById(R.id.image));
      mTeaser = (TextView)itemView.findViewById(R.id.teaser);

      mCard = itemView.findViewById(R.id.card);
    }
  }

  private static boolean listsEquals(List<Post> list1, List<Post> list2){
    return list1.containsAll(list2) && list2.containsAll(list1);
  }

  private Context mContext;
  private List<Post> mItems;

  public PostsRecyclerViewAdapter(Context context) {
    this(context, new ArrayList<>());
  }
  public PostsRecyclerViewAdapter(Context context, List<Post> items) {
    mContext = context;
    mItems = items;
  }

  public void setItems(List<Post> items){

    if(listsEquals(mItems, items))
      return;

    mItems = items;
    notifyDataSetChanged();
  }
  public boolean isEmpty(){
    return mItems.isEmpty();
  }

  @Override public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_post, parent, false);

    return new PostViewHolder(itemView);
  }

  @Override public void onBindViewHolder(PostViewHolder holder, int position) {
    Post post = mItems.get(position);
    holder.mHeader.setText(post.getHeader());
    holder.mPublishDate.setText(post.getPublishDate());
    holder.mTeaser.setText(Html.fromHtml(post.getTeaser()));
    holder.mCard.setOnClickListener(v ->
        DetailActivity_.intent(mContext).extra(EXTRA_POST_ID, post.getId()).start()
    );

    Picasso.with(mContext).load(post.getImageUrl()).into(holder.mImage);
  }

  @Override public int getItemCount() {
    return mItems.size();
  }
}
