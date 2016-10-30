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

        if(!tag.text().trim().isEmpty()) {
            currentHtmlTextTokenBody += tag.toString()
        }
    }

    addTextToken()

    return tokens
}

sealed class PostToken(){

    class HtmlTextToken(val text: String) : PostToken()
    class ImageToken(val imageUrl: String, val title: String = "") : PostToken()

}