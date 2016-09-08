package org.ar25.androidno.api;

import org.ar25.androidno.entities.Post;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class HtmlPostConverterFactory extends Converter.Factory {
  @Override public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {

    if(type.equals(Post.class)){
      return new HtmlResponsePostConverter();
    }

    else {
      return new HtmlResponsePostsListConverter();
    }
  }
}
