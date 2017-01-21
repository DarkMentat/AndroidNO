package org.ar25.androidno.ui

import android.content.Context
import android.support.v7.util.SortedList
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.squareup.picasso.Picasso
import org.ar25.androidno.R
import org.ar25.androidno.entities.Post
import org.ar25.androidno.ui.PostsRecyclerViewAdapter.PostViewHolder.LoadingViewHolder
import org.ar25.androidno.ui.PostsRecyclerViewAdapter.PostViewHolder.PostContentViewHolder
import org.ar25.androidno.util.trueAnd
import org.sufficientlysecure.htmltextview.HtmlTextView


class PostsRecyclerViewAdapter(
        private val context: Context

    ) : RecyclerView.Adapter<PostsRecyclerViewAdapter.PostViewHolder>() {

    companion object {
        const val VIEW_TYPE_LOADING = 0
        const val VIEW_TYPE_CONTENT = 1
    }

    sealed class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        class LoadingViewHolder(itemView: View, onCreate: (vh: LoadingViewHolder) -> Unit) : PostViewHolder(itemView) {
            val mProgressBarIndicator = itemView.findViewById(R.id.loadingIndicator) as ProgressBar
            val mLoadMoreButton = itemView.findViewById(R.id.loadMoreButton) as Button

            init {
                onCreate(this)
            }
        }
        class PostContentViewHolder(itemView: View) : PostViewHolder(itemView) {
            val mCard = itemView.findViewById(R.id.card) as ViewGroup
            val mPublishDate = itemView.findViewById(R.id.publishDate) as TextView
            val mHeader = itemView.findViewById(R.id.header) as TextView
            val mImage = itemView.findViewById(R.id.image) as ImageView
            val mTeaser = itemView.findViewById(R.id.teaser) as HtmlTextView
            val mOverflowMenu = itemView.findViewById(R.id.overflow_menu) as View
        }
    }

    private var loadingViewHolder: LoadingViewHolder? = null
    private val sortedList = SortedList<Post>(Post::class.java, object: SortedList.Callback<Post>() {

        override fun onMoved(fromPosition: Int, toPosition: Int) = notifyItemMoved(fromPosition, toPosition)
        override fun onRemoved(position: Int, count: Int) = notifyItemRangeRemoved(position, count)
        override fun onInserted(position: Int, count: Int) = notifyItemRangeInserted(position, count)
        override fun onChanged(position: Int, count: Int) = notifyItemRangeChanged(position, count)

        override fun areItemsTheSame(item1: Post, item2: Post) = item1.id == item2.id
        override fun areContentsTheSame(oldItem: Post, newItem: Post) = newItem == oldItem
        override fun compare(o1: Post, o2: Post): Int {

            val o1Dates = o1.publishDate.split('.')
            val o2Dates = o2.publishDate.split('.')

            if(o1Dates[2] > o2Dates[2]) return -1
            if(o1Dates[2] < o2Dates[2]) return +1

            if(o1Dates[1] > o2Dates[1]) return -1
            if(o1Dates[1] < o2Dates[1]) return +1

            if(o1Dates[0] > o2Dates[0]) return -1
            if(o1Dates[0] < o2Dates[0]) return +1

            if(o1.id > o2.id) return -1
            if(o1.id < o2.id) return +1

            return 0
        }
    })

    fun getPosts() = if(sortedList.size() != 0) (0..sortedList.size()-1).map { i -> sortedList[i] } else emptyList()
    fun updateItems(items: List<Post>) {

        sortedList.addAll(items)
    }

    fun removeAllItems() {

        sortedList.clear()
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflate: (Int) -> View =
                { LayoutInflater.from(parent.context).inflate(it, parent, false) }

        when(viewType){
            VIEW_TYPE_LOADING -> return LoadingViewHolder(inflate(R.layout.item_post_loading), onCreateLoadingViewHolder)
            VIEW_TYPE_CONTENT -> return PostContentViewHolder(inflate(R.layout.item_post))
            else -> throw IllegalArgumentException("invalid view type")
        }
    }


    var onItemClick: (Post) -> Unit = {}
    var onAddToFavorites: (Post) -> Unit = {}
    var onRemoveFromFavorites: (Post) -> Unit = {}

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        if(holder is PostContentViewHolder) {
            val post = sortedList[position]

            holder.mHeader.text = post.header
            holder.mPublishDate.text = post.publishDate
            holder.mTeaser.setHtml(post.teaser)
            holder.mCard.setOnClickListener { onItemClick(post) }

            holder.mOverflowMenu.setOnClickListener {

                val popup = PopupMenu(context, holder.mOverflowMenu, Gravity.END)
                popup.menuInflater.inflate(R.menu.popup_menu_post, popup.menu)

                if(post.isFavorite)
                    popup.menu.removeItem(R.id.action_add_to_favorites)
                else
                    popup.menu.removeItem(R.id.action_remove_from_favorites)

                popup.setOnMenuItemClickListener { menuItem ->

                    when(menuItem.itemId) {

                        R.id.action_add_to_favorites -> trueAnd { onAddToFavorites(post) }
                        R.id.action_remove_from_favorites -> trueAnd { onRemoveFromFavorites(post) }

                        else -> false
                    }
                }

                popup.show()
            }

            Picasso.with(context).load(post.imageUrl).into(holder.mImage)
        }

        if(holder is LoadingViewHolder) {
            loadingViewHolder = holder
        }
    }

    override fun getItemViewType(position: Int) : Int {
        return if(position >= sortedList.size()) VIEW_TYPE_LOADING else VIEW_TYPE_CONTENT
    }

    private var _enabledBottomLoadedView = true
    var enabledBottomLoadedView: Boolean
        get() = _enabledBottomLoadedView
        set(value) {

            if(_enabledBottomLoadedView == value) return

            _enabledBottomLoadedView = value
            if (_enabledBottomLoadedView) {

                notifyItemInserted(sortedList.size())
            } else {

                notifyItemRemoved(sortedList.size())
            }
        }

    override fun getItemCount() = sortedList.size() + if(enabledBottomLoadedView) 1 else 0

    var postsCount: Int = sortedList.size()
        get() = sortedList.size()


    var onCreateLoadingViewHolder: (vh: LoadingViewHolder) -> Unit = {}

    fun enableLoadingIndicator(){

        if(loadingViewHolder == null){
            onCreateLoadingViewHolder = {
                loadingViewHolder = it
                enableLoadingIndicator()
                onCreateLoadingViewHolder = {}
            }
            return
        }

        loadingViewHolder?.mProgressBarIndicator?.visibility = VISIBLE
        loadingViewHolder?.mLoadMoreButton?.visibility = GONE
    }
    fun enableLoadMoreButton(onClick: () -> Unit ){

        if(loadingViewHolder == null){
            onCreateLoadingViewHolder = {
                loadingViewHolder = it
                enableLoadMoreButton(onClick)
            }
            return
        }

        loadingViewHolder?.mProgressBarIndicator?.visibility = GONE
        loadingViewHolder?.mLoadMoreButton?.visibility = VISIBLE

        loadingViewHolder?.mLoadMoreButton?.setOnClickListener {
            onClick()
            enableLoadingIndicator()
        }
    }
}
