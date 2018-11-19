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
            val elements = document.select("div .entity-extra-wrapper")

            for (element in elements) {
                if(element.children().getOrNull(0)?.hasClass("node") != true)
                    continue

                val id = java.lang.Long.valueOf(element.attr("data-entity_id"))
                val header = element.child(0).select("div .title")[0].child(0).text()
                val publishDate = element.child(0).select("div .field-item")[0].text()
                val imageUrl: String

                if (element.child(0).select("div .field-item")[1].child(0).children().size > 0) {
                    imageUrl = element.child(0).select("div .field-item")[1].child(0).child(0).absUrl("src").split("\\?".toRegex()).dropLastWhile(String::isEmpty).toTypedArray()[0]
                } else {
                    imageUrl = "http://ar25.org/sites/all/storage/default_images/styles/teaser280/sonce.jpg"
                }

                val teaser = element.child(0).select("div .field-item")[3].text()

                data.add(Post(id, header, publishDate, imageUrl, teaser))
            }

            return data

        } catch (error: Exception) {
            throw ParseErrorException("Can not parse post list response", error)
        } finally {
            value.close()
        }
    }
}
