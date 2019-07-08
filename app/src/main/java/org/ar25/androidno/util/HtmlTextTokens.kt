package org.ar25.androidno.util

import org.jsoup.Jsoup
import org.jsoup.helper.StringUtil
import org.jsoup.parser.Parser

fun parseHtmlTextToTokens(html: String): List<PostToken>{

    val tokens = mutableListOf<PostToken>()
    val comments = mutableListOf<PostToken.Comment>()
    val allTags = Jsoup.parseBodyFragment(html).child(0).child(1).children()



    var currentHtmlTextTokenBody = ""

    fun addTextToken(){
        if(!currentHtmlTextTokenBody.isEmpty()){
            tokens.add(PostToken.HtmlTextToken(currentHtmlTextTokenBody))
            currentHtmlTextTokenBody = ""
        }
    }

    for (tag in allTags){

        tag.setBaseUri("http://www.ar25.org")

        if(!tag.getElementsByClass("file").isEmpty()){
            addTextToken()

            for(element in tag.getElementsByAttribute("type")){
                if(element.attr("type").split("/")[0] == "audio") //todo validate link
                    tokens.add(PostToken.AudioLinkToken(element.absUrl("href"), element.text()))
            }

            continue
        }

        if(!tag.getElementsByTag("iframe").isEmpty()) {
            addTextToken()

            for(element in tag.getElementsByTag("iframe")){
                if(!element.attr("src").isEmpty()) //todo validate link
                    tokens.add(PostToken.YoutubeVideoToken(element.attr("src")))
            }

            continue
        }

        if(!tag.getElementsByTag("img").isEmpty()) {
            addTextToken()

            for(node in tag.children()) {
                when {
                    node.tag().name == "img" -> tokens.add(PostToken.ImageToken(node.absUrl("src"), node.attr("title")))
                    !node.text().trim().isEmpty() -> currentHtmlTextTokenBody += node.toString()
                }
            }

            continue
        }

        if(tag.tagName() == "comment"){
            val authorDate = tag.child(0).text().substring(13) //strip "Опубліковано"
            val avatarUrl = tag.child(1).text()
            var htmlContent = tag.child(2).html()

            val startInnerQuote = htmlContent.indexOf("<blockquote class=\"bb-quote-body\">") + 34

            if(startInnerQuote >= 34)
                htmlContent = htmlContent.substring(0, startInnerQuote) + Jsoup.parse(Parser.unescapeEntities(htmlContent.substring(startInnerQuote), true)).text()

            comments.add(PostToken.Comment(authorDate, avatarUrl, htmlContent))
            continue
        }

        if(tokens.size == 0) { //add teaser as separete token
            addTextToken()
        }

        if(!tag.text().trim().isEmpty()) {
            currentHtmlTextTokenBody += tag.toString()
        }
    }

    if(comments.isNotEmpty()){
        currentHtmlTextTokenBody += "<p><b>Коментарі:</b></p>"
    }

    addTextToken()

    comments.lastOrNull()?.last = true

    tokens.addAll(comments)

    return tokens
}

sealed class PostToken(){

    class HtmlTextToken(val text: String) : PostToken()
    class ImageToken(val imageUrl: String, val title: String = "") : PostToken()
    class YoutubeVideoToken(val youtubeUrl: String) : PostToken()
    class AudioLinkToken(val audioUrl: String, val title: String) : PostToken()
    class Comment(val authorDate: String, val avatarUrl: String, val htmlContent: String, var last: Boolean = false) : PostToken()

}