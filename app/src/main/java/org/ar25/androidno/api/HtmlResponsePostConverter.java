package org.ar25.androidno.api;

import org.ar25.androidno.entities.Post;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class HtmlResponsePostConverter implements Converter<ResponseBody, Post> {

  @Override public Post convert(ResponseBody value) throws IOException {

    Document document = Jsoup.parse(value.string());
    Element element = document.select("div .entity-extra-wrapper").first();

    Long id = Long.valueOf(element.attr("data-entity_id"));
    String header = element.select("h1.title").first().text();
    String publishDate = element.select("div .date-time").first().text().split(" - ")[0];
    String imageUrl = element.select("div .field.field-name-field-img-cover.field-type-image.field-label-hidden")
        .first().child(0).child(0).absUrl("src");

    if(imageUrl.equals("")){
      imageUrl = element.select("div .field.field-name-field-img-cover.field-type-image.field-label-hidden")
          .first().child(0).child(0).child(0).absUrl("src");
    }

    if(imageUrl.equals("")){
      imageUrl = element.select("div .field.field-name-field-img-cover.field-type-image.field-label-hidden")
          .first().child(0).child(0).child(0).child(0).absUrl("src");
    }

    String teaser = element.select("div .teaser").first().child(0).outerHtml();
    String text = element.select("div .field.field-name-body.field-type-text-with-summary.field-label-hidden")
        .first().child(0).child(0).html();

    return Post.newPost(id,header,publishDate,imageUrl,teaser,text);
  }
}
