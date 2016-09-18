package org.ar25.androidno.ui

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.squareup.picasso.Picasso

import org.ar25.androidno.R
import org.ar25.androidno.entities.Post


class PostsRecyclerViewAdapter(
        private val mContext: Context,
        private var mItems: List<Post> = emptyList()) : RecyclerView.Adapter<PostsRecyclerViewAdapter.PostViewHolder>() {

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mCard: View
        val mPublishDate: TextView
        val mHeader: TextView
        val mImage: ImageView
        val mTeaser: TextView

        init {
            mHeader = itemView.findViewById(R.id.header) as TextView
            mPublishDate = itemView.findViewById(R.id.publishDate) as TextView
            mImage = itemView.findViewById(R.id.image) as ImageView
            mTeaser = itemView.findViewById(R.id.teaser) as TextView

            mCard = itemView.findViewById(R.id.card)
        }
    }


    fun setItems(items: List<Post>) {

        if (items == mItems)
            return

        mItems = items
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)

        return PostViewHolder(itemView)
    }

    @Suppress("DEPRECATION")
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = mItems[position]
        holder.mHeader.text = post.header
        holder.mPublishDate.text = post.publishDate
        holder.mTeaser.text = Html.fromHtml(post.teaser)
        holder.mCard.setOnClickListener {
            mContext.startActivity(Intent(mContext, DetailActivity::class.java)
                    .putExtra(DetailActivity.EXTRA_POST_ID, post.id))
        }

        Picasso.with(mContext).load(post.imageUrl).into(holder.mImage)
    }

    override fun getItemCount(): Int {
        return mItems.size
    }
}
