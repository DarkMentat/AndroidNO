package org.ar25.androidno.util

import org.jsoup.Jsoup

fun parseHtmlTextToTokens(html: String): List<PostToken>{

    val tokens = mutableListOf<PostToken>()
    val allTags = Jsoup.parseBodyFragment(html).child(0).child(1).children()

    var currentHtmlTextTokenBody = ""

    fun addTextToken(){
        if(!currentHtmlTextTokenBody.isEmpty()){
            tokens.add(PostToken.HtmlTextToken(currentHtmlTextTokenBody))
            currentHtmlTextTokenBody = ""
        }
    }

    for (tag in allTags){

        if(!tag.text().isEmpty()) {
            currentHtmlTextTokenBody += tag.toString()
        }

        if(!tag.getElementsByTag("img").isEmpty())
        {
            addTextToken()

            val img = tag.getElementsByTag("img")[0]
            tokens.add(PostToken.ImageToken(img.absUrl("src")))
        }
    }

    addTextToken()

    return tokens
}

sealed class PostToken(){

    class HtmlTextToken(val text: String) : PostToken()
    class ImageToken(val imageUrl: String) : PostToken()

}