package org.ar25.androidno.db

import android.content.ContentValues
import android.database.sqlite.SQLiteOpenHelper
import com.pushtorefresh.storio.sqlite.Changes
import com.pushtorefresh.storio.sqlite.StorIOSQLite
import com.pushtorefresh.storio.sqlite.queries.Query
import org.ar25.androidno.db.DbOpenHelper.Companion.DB_POSTS_ID
import org.ar25.androidno.db.DbOpenHelper.Companion.DB_POSTS_IMAGE_URL
import org.ar25.androidno.db.DbOpenHelper.Companion.DB_POSTS_IS_FAVORITE
import org.ar25.androidno.db.DbOpenHelper.Companion.DB_POSTS_PUBLISH_DATE
import org.ar25.androidno.db.DbOpenHelper.Companion.DB_POSTS_TABLE
import org.ar25.androidno.db.LocalStorage.Companion.POSTS_PER_PAGE
import org.ar25.androidno.entities.Post
import org.ar25.androidno.entities.PostStorIOSQLitePutResolver
import org.ar25.androidno.util.someObject
import javax.inject.Inject


class LocalStorageImpl @Inject constructor(

        val mStorIOSQLite: StorIOSQLite,
        val mSQLiteOpenHelper: SQLiteOpenHelper

) : LocalStorage {

    override fun savePosts(posts: List<Post>) {
        insertManyPostPreviews(mStorIOSQLite.lowLevel(), mSQLiteOpenHelper, posts)
    }

    override fun savePost(post: Post, updateFavorite: Boolean) {
        mStorIOSQLite
                .put()
                .someObject(post)
                .withPutResolver(
                        object : PostStorIOSQLitePutResolver() {
                            override fun mapToContentValues(`object`: Post): ContentValues {
                                val values = super.mapToContentValues(`object`)

                                if (values.getAsString(DB_POSTS_IMAGE_URL) == "") {
                                    values.remove(DB_POSTS_IMAGE_URL)
                                }

                                if(!updateFavorite)
                                    values.remove(DB_POSTS_IS_FAVORITE)

                                return values
                            }
                        })
                .prepare()
                .executeAsBlocking()
    }

    override fun getPosts(offset: Int): List<Post> {
        if (offset < 0)
            return emptyList()

        return mStorIOSQLite
                .get()
                .listOfObjects(Post::class.java)
                .withQuery(
                        Query.builder()
                                .table(DB_POSTS_TABLE)
                                .orderBy("substr($DB_POSTS_PUBLISH_DATE, 7, 10) DESC, " +
                                         "substr($DB_POSTS_PUBLISH_DATE, 4, 5) DESC, " +
                                         "substr($DB_POSTS_PUBLISH_DATE, 1, 2) DESC, " +
                                         "$DB_POSTS_ID DESC")
                                .limit(offset * POSTS_PER_PAGE, POSTS_PER_PAGE)
                                .build())
                .prepare()
                .executeAsBlocking()
    }

    override fun getPost(id: Long): Post? {
        return mStorIOSQLite
                .get()
                .`object`(Post::class.java)
                .withQuery(
                        Query.builder()
                                .table(DB_POSTS_TABLE)
                                .where("$DB_POSTS_ID = ?")
                                .whereArgs(id)
                                .build())
                .prepare()
                .executeAsBlocking()
    }

    override fun getFavoritePosts(offset: Int): List<Post> {
        if (offset < 0)
            return emptyList()

        return mStorIOSQLite
                .get()
                .listOfObjects(Post::class.java)
                .withQuery(
                        Query.builder()
                                .table(DB_POSTS_TABLE)
                                .where("$DB_POSTS_IS_FAVORITE = 1")
                                .orderBy("substr($DB_POSTS_PUBLISH_DATE, 7, 10) DESC, " +
                                         "substr($DB_POSTS_PUBLISH_DATE, 4, 5) DESC, " +
                                         "substr($DB_POSTS_PUBLISH_DATE, 1, 2) DESC, " +
                                         "$DB_POSTS_ID DESC")
                                .limit(offset * POSTS_PER_PAGE, POSTS_PER_PAGE)
                                .build())
                .prepare()
                .executeAsBlocking()
    }

    private fun insertManyPostPreviews(lowLevel: StorIOSQLite.LowLevel, helper: SQLiteOpenHelper, posts: List<Post>) {
        val db = helper.writableDatabase
        try {
            db.beginTransaction()

            val sql = "INSERT OR IGNORE INTO $DB_POSTS_TABLE (id, header, image_url, publish_date, teaser) VALUES (?,?,?,?,?)"
            val statement = db.compileStatement(sql)

            for (post in posts) {
                statement.clearBindings()
                statement.bindLong(1, post.id)
                statement.bindString(2, post.header)
                statement.bindString(3, post.imageUrl)
                statement.bindString(4, post.publishDate)
                statement.bindString(5, post.teaser)
                statement.executeInsert()
            }

            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
        lowLevel.notifyAboutChanges(Changes.newInstance(DB_POSTS_TABLE))
    }
}
