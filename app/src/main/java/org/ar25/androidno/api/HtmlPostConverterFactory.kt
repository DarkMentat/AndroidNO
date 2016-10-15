package org.ar25.androidno.api

import org.ar25.androidno.entities.Post
import java.lang.reflect.Type

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit

class HtmlPostConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(type: Type?, annotations: Array<Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *> {

        if (type == Post::class.java) {
            return HtmlResponsePostConverter()
        } else {
            return HtmlResponsePostsListConverter()
        }
    }
}
