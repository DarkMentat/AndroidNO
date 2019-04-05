package org.ar25.androidno.api

import okhttp3.ResponseBody
import org.ar25.androidno.entities.Post
import org.jsoup.Jsoup
import retrofit2.Converter

class HtmlResponsePostConverter : Converter<ResponseBody, Post> {

    override fun convert(value: ResponseBody): Post {

        try {
            val document = Jsoup.parse(value.string())
            val element = document.select("div .node-full").first()

            val url = document.select("link[rel=canonical]").first().attr("href")
            val id = Math.abs(url.hashCode().toLong())
            val header = element.select("div .field.field-name-title-field").first().text()
            val publishDate = element.select("div .field.field-name-post-date").first().child(0).text().split(" - ".toRegex())[0]
            val imageUrl = element.select("div .field.field-name-field-img-cover")?.first()?.getElementsByTag("img")?.first()?.absUrl("src") ?: ""
            val imageTitle = element.select("div .field.field-name-field-file-image-title-text.field-type-text").first()?.text()
            val teaser = element.select("div .field-type-text-with-summary").first().html()
            val text = element.select("div .field.field-name-body.field-type-text-with-summary.field-label-hidden").first().child(0).child(0).html()
            val gamer = element.select("span.username").first().text()

            try {
                val source = element.select("div .field.field-name-field-link.field-type-link-field.field-label-hidden").first().child(0).child(0).child(0).text()
                val sourceLink = element.select("div .field.field-name-field-link.field-type-link-field.field-label-hidden").first().child(0).child(0).child(0).attr("href")

                return Post(id, url, header, publishDate, imageUrl, teaser, false, text, gamer, imageTitle, source, sourceLink)
            } catch (error: Exception) {
                return Post(id, url, header, publishDate, imageUrl, teaser, false, text, gamer)
            }

        } catch (error: Exception){
            throw ParseErrorException("Can not parse post response", error)
        } finally {
            value.close()
        }
    }
}
