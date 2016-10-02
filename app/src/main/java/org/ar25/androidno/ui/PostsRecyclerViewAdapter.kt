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
import org.ar25.androidno.ui.PostsRecyclerViewAdapter.PostViewHolder.LoadingViewHolder
import org.ar25.androidno.ui.PostsRecyclerViewAdapter.PostViewHolder.PostContentViewHolder


class PostsRecyclerViewAdapter(
        private val mContext: Context,
        private val mItems: MutableList<Post> = arrayListOf(),
        private val mElementsPositions: MutableMap<Long, Int> = mutableMapOf()) : RecyclerView.Adapter<PostsRecyclerViewAdapter.PostViewHolder>() {

    companion object {
        const val VIEW_TYPE_LOADING = 0
        const val VIEW_TYPE_CONTENT = 1
    }

    sealed class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        class LoadingViewHolder(itemView: View) : PostViewHolder(itemView)
        class PostContentViewHolder(itemView: View) : PostViewHolder(itemView) {
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
    }


    //todo implement add to start
    fun updateItems(items: List<Post>) {
        val insertPosition = mItems.size
        val elementsToUpdate = items.filter { it.id in mElementsPositions}
        val elementsToInsert = items.filterNot { it.id in mElementsPositions}

        var index = insertPosition
        for(item in elementsToInsert){
            mElementsPositions.put(item.id, index++)
        }
        mItems.addAll(elementsToInsert)
        notifyItemRangeInserted(insertPosition, elementsToInsert.size)

        for(item in elementsToUpdate){
            val indexToUpdate = mElementsPositions[item.id]

            if(mItems[indexToUpdate!!] != item){
                mItems[indexToUpdate] = item
                notifyItemChanged(indexToUpdate)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflate: (Int) -> View =
                { LayoutInflater.from(parent.context).inflate(it, parent, false) }

        when(viewType){
            VIEW_TYPE_LOADING -> return LoadingViewHolder(inflate(R.layout.item_post_loading))
            VIEW_TYPE_CONTENT -> return PostContentViewHolder(inflate(R.layout.item_post))
            else -> throw IllegalArgumentException("invalid view type")
        }
    }

    @Suppress("DEPRECATION")
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        if(holder is PostContentViewHolder) {
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
    }

    override fun getItemViewType(position: Int) : Int {
        return if(position >= mItems.size) VIEW_TYPE_LOADING else VIEW_TYPE_CONTENT
    }
    override fun getItemCount(): Int {
        return mItems.size + 1
    }
}
