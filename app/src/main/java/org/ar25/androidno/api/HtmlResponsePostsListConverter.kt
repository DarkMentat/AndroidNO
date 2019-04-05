package org.ar25.androidno.api

import okhttp3.ResponseBody
import org.ar25.androidno.entities.Post
import org.jsoup.Jsoup
import retrofit2.Converter
import java.util.*

class HtmlResponsePostsListConverter : Converter<ResponseBody, List<Post>> {

    override fun convert(value: ResponseBody): List<Post> {
        try {

            val data = ArrayList<Post>()

            val document = Jsoup.parse(value.string())
            val elements = document.select("div .views-view-grid .node-blog")

            for (element in elements) {

                val url = "https://www.ar25.org" + element.select("div.field-name-title")[0].child(0).child(0).child(0).child(0).attr("href")
                val id = Math.abs(url.hashCode().toLong())
                val header = element.select("div.field-name-title").text()
                val publishDate = element.select("div.field-name-post-date").text()
                val imageUrl: String

                if (element.select("div.field-name-field-img-teaser")[0].child(0).children().size > 0) {
                    imageUrl = element.select("div.field-name-field-img-teaser")[0].child(0).child(0).child(0).child(0).absUrl("src").split("\\?".toRegex()).dropLastWhile(String::isEmpty).toTypedArray()[0]
                } else {
                    imageUrl = "http://ar25.org/sites/all/storage/default_images/styles/teaser280/sonce.jpg"
                }

                val teaser = element.select("div.field-type-text-with-summary").text()

                data.add(Post(id, url, header, publishDate, imageUrl, teaser))
            }

            return data

        } catch (error: Exception) {
            throw ParseErrorException("Can not parse post list response", error)
        } finally {
            value.close()
        }
    }
}
