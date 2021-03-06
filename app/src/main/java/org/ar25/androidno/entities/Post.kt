package org.ar25.androidno.entities

import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType
import org.ar25.androidno.db.DbOpenHelper.Companion.DB_POSTS_COMMENTS
import org.ar25.androidno.db.DbOpenHelper.Companion.DB_POSTS_GAMER
import org.ar25.androidno.db.DbOpenHelper.Companion.DB_POSTS_HEADER
import org.ar25.androidno.db.DbOpenHelper.Companion.DB_POSTS_ID
import org.ar25.androidno.db.DbOpenHelper.Companion.DB_POSTS_IMAGE_TITLE
import org.ar25.androidno.db.DbOpenHelper.Companion.DB_POSTS_IMAGE_URL
import org.ar25.androidno.db.DbOpenHelper.Companion.DB_POSTS_IS_FAVORITE
import org.ar25.androidno.db.DbOpenHelper.Companion.DB_POSTS_PUBLISH_DATE
import org.ar25.androidno.db.DbOpenHelper.Companion.DB_POSTS_SECTION
import org.ar25.androidno.db.DbOpenHelper.Companion.DB_POSTS_SIMILAR
import org.ar25.androidno.db.DbOpenHelper.Companion.DB_POSTS_SOURCE
import org.ar25.androidno.db.DbOpenHelper.Companion.DB_POSTS_SOURCE_LINK
import org.ar25.androidno.db.DbOpenHelper.Companion.DB_POSTS_TABLE
import org.ar25.androidno.db.DbOpenHelper.Companion.DB_POSTS_TEASER
import org.ar25.androidno.db.DbOpenHelper.Companion.DB_POSTS_TEXT
import org.ar25.androidno.db.DbOpenHelper.Companion.DB_POSTS_URL
import org.ar25.androidno.util.PostToken


@StorIOSQLiteType(table = DB_POSTS_TABLE)
data class Post(

        @JvmField @StorIOSQLiteColumn(name = DB_POSTS_ID, key = true)
        var id: Long,

        @JvmField @StorIOSQLiteColumn(name = DB_POSTS_URL)
        var url: String,

        @JvmField @StorIOSQLiteColumn(name = DB_POSTS_HEADER)
        var header: String,

        @JvmField @StorIOSQLiteColumn(name = DB_POSTS_PUBLISH_DATE)
        var publishDate: String,

        @JvmField @StorIOSQLiteColumn(name = DB_POSTS_IMAGE_URL)
        var imageUrl: String,

        @JvmField @StorIOSQLiteColumn(name = DB_POSTS_TEASER)
        var teaser: String,

        @JvmField @StorIOSQLiteColumn(name = DB_POSTS_IS_FAVORITE)
        var isFavorite: Boolean = false,

        @JvmField @StorIOSQLiteColumn(name = DB_POSTS_TEXT)
        var text: String? = null,

        @JvmField @StorIOSQLiteColumn(name = DB_POSTS_GAMER)
        var gamer: String? = null,

        @JvmField @StorIOSQLiteColumn(name = DB_POSTS_IMAGE_TITLE)
        var imageTitle: String? = null,

        @JvmField @StorIOSQLiteColumn(name = DB_POSTS_SOURCE)
        var source: String? = null,

        @JvmField @StorIOSQLiteColumn(name = DB_POSTS_SOURCE_LINK)
        var sourceLink: String? = null,

        @JvmField @StorIOSQLiteColumn(name = DB_POSTS_SECTION)
        var section: String? = null,

        @JvmField @StorIOSQLiteColumn(name = DB_POSTS_SIMILAR)
        var similar: String? = null,

        @JvmField @StorIOSQLiteColumn(name = DB_POSTS_COMMENTS)
        var comments: String? = null){

    internal constructor() : this(-1, "", "", "", "", "") // leave default constructor for AutoGenerated code


    var htmlTokens: List<PostToken>? = null
}