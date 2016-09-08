package org.ar25.androidno.api;

import org.ar25.androidno.entities.Post;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Converter;

import static org.ar25.androidno.entities.Post.newPost;

public class HtmlResponsePostsListConverter implements Converter<ResponseBody, List<Post>> {
  @Override public List<Post> convert(ResponseBody value) throws IOException {

    List<Post> data = new ArrayList<>();

    Document document = Jsoup.parse(value.string());
    Elements elements = document.select("div .entity-extra-wrapper");

    for(Element element : elements){
      Long id = Long.valueOf(element.attr("data-entity_id"));
      String header = element.child(0).select("div .title").get(0).child(0).text();
      String publishDate = element.child(0).select("div .field-item").get(0).text();
      String imageUrl;

      if(element.child(0).select("div .field-item").get(1).child(0).children().size() > 0){
        imageUrl = element.child(0).select("div .field-item").get(1).child(0).child(0).absUrl("src").split("\\?")[0];
      }else{
        imageUrl = "http://ar25.org/sites/all/storage/default_images/styles/teaser280/sonce.jpg";
      }

      String teaser = element.child(0).select("div .field-item").get(3).text();

      data.add(newPost(id, header, publishDate, imageUrl, teaser));
    }

    return data;
  }
}
