package org.ar25.androidno.ui


import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView


//
// Thanks to: https://gist.github.com/ssinss/e06f12ef66c51252563e
//
open class OnLoadMoreEndlessRecyclerView(
        private val linearLayoutManager: LinearLayoutManager,
        private val onLoadMore: () -> Unit

    ) : RecyclerView.OnScrollListener() {

    private var previousTotal = 0 // The total number of items in the dataset after the last load
    private var loading = true // True if we are still waiting for the last set of data to load.
    private val visibleThreshold = 1 // The minimum amount of items to have below your current scroll position before loading more.


    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = recyclerView.childCount
        val totalItemCount = linearLayoutManager.itemCount
        val firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition()

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false
                previousTotal = totalItemCount
            }
        }
        if (!loading && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {
            // End has been reached

            onLoadMore()

            loading = true
        }
    }
}
