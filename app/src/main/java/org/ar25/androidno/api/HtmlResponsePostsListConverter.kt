package org.ar25.androidno.api

import org.ar25.androidno.entities.Post
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

import java.io.IOException
import java.util.ArrayList

import okhttp3.ResponseBody
import retrofit2.Converter

import org.ar25.androidno.entities.Post.newPost

class HtmlResponsePostsListConverter : Converter<ResponseBody, List<Post>> {
    @Throws(IOException::class)
    override fun convert(value: ResponseBody): List<Post> {

        val data = ArrayList<Post>()

        val document = Jsoup.parse(value.string())
        val elements = document.select("div .entity-extra-wrapper")

        for (element in elements) {
            val id = java.lang.Long.valueOf(element.attr("data-entity_id"))
            val header = element.child(0).select("div .title")[0].child(0).text()
            val publishDate = element.child(0).select("div .field-item")[0].text()
            val imageUrl: String

            if (element.child(0).select("div .field-item")[1].child(0).children().size > 0) {
                imageUrl = element.child(0).select("div .field-item")[1].child(0).child(0).absUrl("src").split("\\?".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
            } else {
                imageUrl = "http://ar25.org/sites/all/storage/default_images/styles/teaser280/sonce.jpg"
            }

            val teaser = element.child(0).select("div .field-item")[3].text()

            data.add(newPost(id, header, publishDate, imageUrl, teaser))
        }

        return data
    }
}