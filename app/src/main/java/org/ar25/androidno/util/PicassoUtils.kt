package org.ar25.androidno.util

import com.squareup.picasso.Callback
import com.squareup.picasso.RequestCreator

fun RequestCreator.fetch(success: () -> Unit) {
    fetch(object : Callback {
        override fun onSuccess() = success()
        override fun onError() = {}()
    })
}