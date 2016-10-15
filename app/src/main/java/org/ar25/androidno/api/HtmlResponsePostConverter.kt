package org.ar25.androidno.api

import org.ar25.androidno.entities.Post
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

import java.io.IOException

import okhttp3.ResponseBody
import retrofit2.Converter

class HtmlResponsePostConverter : Converter<ResponseBody, Post> {

    @Throws(IOException::class)
    override fun convert(value: ResponseBody): Post {

        val document = Jsoup.parse(value.string())
        val element = document.select("div .entity-extra-wrapper").first()

        val id = java.lang.Long.valueOf(element.attr("data-entity_id"))
        val header = element.select("h1.title").first().text()
        val publishDate = element.select("div .date-time").first().text().split(" - ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
        var imageUrl = element.select("div .field.field-name-field-img-cover.field-type-image.field-label-hidden").first().child(0).child(0).absUrl("src")

        if (imageUrl == "") {
            imageUrl = element.select("div .field.field-name-field-img-cover.field-type-image.field-label-hidden").first().child(0).child(0).child(0).absUrl("src")
        }

        if (imageUrl == "") {
            imageUrl = element.select("div .field.field-name-field-img-cover.field-type-image.field-label-hidden").first().child(0).child(0).child(0).child(0).absUrl("src")
        }

        val teaser = element.select("div .teaser").first().child(0).outerHtml()
        val text = element.select("div .field.field-name-body.field-type-text-with-summary.field-label-hidden").first().child(0).child(0).html()

        return Post.newPost(id, header, publishDate, imageUrl, teaser, text)
    }
}
