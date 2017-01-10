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

        if(tokens.size == 0) { //add teaser as separete token
            addTextToken()
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
    class YoutubeVideoToken(val youtubeUrl: String) : PostToken()
    class AudioLinkToken(val audioUrl: String, val title: String) : PostToken()

}