package org.ar25.androidno.db

import android.content.ContentValues
import android.database.sqlite.SQLiteOpenHelper
import com.pushtorefresh.storio.sqlite.Changes
import com.pushtorefresh.storio.sqlite.StorIOSQLite
import com.pushtorefresh.storio.sqlite.queries.Query
import org.ar25.androidno.db.DbOpenHelper.Companion.DB_POSTS_HEADER
import org.ar25.androidno.db.DbOpenHelper.Companion.DB_POSTS_ID
import org.ar25.androidno.db.DbOpenHelper.Companion.DB_POSTS_IMAGE_URL
import org.ar25.androidno.db.DbOpenHelper.Companion.DB_POSTS_IS_FAVORITE
import org.ar25.androidno.db.DbOpenHelper.Companion.DB_POSTS_PUBLISH_DATE
import org.ar25.androidno.db.DbOpenHelper.Companion.DB_POSTS_SECTION
import org.ar25.androidno.db.DbOpenHelper.Companion.DB_POSTS_TABLE
import org.ar25.androidno.db.DbOpenHelper.Companion.DB_POSTS_TEASER
import org.ar25.androidno.db.DbOpenHelper.Companion.DB_POSTS_URL
import org.ar25.androidno.db.LocalStorage.Companion.POSTS_PER_PAGE
import org.ar25.androidno.entities.Post
import org.ar25.androidno.entities.PostStorIOSQLitePutResolver
import org.ar25.androidno.entities.Section
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

    override fun getPostsAtSection(section: Section, offset: Int): List<Post> {
        if (offset < 0)
            return emptyList()

        return mStorIOSQLite
                .get()
                .listOfObjects(Post::class.java)
                .withQuery(
                        Query.builder()
                                .table(DB_POSTS_TABLE)
                                .where("$DB_POSTS_SECTION = ?")
                                .whereArgs(section.apiSlug)
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

            val insertSql = "INSERT OR IGNORE INTO $DB_POSTS_TABLE ($DB_POSTS_ID, $DB_POSTS_URL, $DB_POSTS_HEADER, $DB_POSTS_IMAGE_URL, $DB_POSTS_PUBLISH_DATE, $DB_POSTS_TEASER, $DB_POSTS_SECTION) VALUES (?,?,?,?,?,?,?)"
            val updateSql = "UPDATE $DB_POSTS_TABLE SET $DB_POSTS_SECTION=? WHERE $DB_POSTS_ID=?"
            val insertStatement = db.compileStatement(insertSql)
            val updateStatement = db.compileStatement(updateSql)

            for (post in posts) {
                insertStatement.clearBindings()
                insertStatement.bindLong(1, post.id)
                insertStatement.bindString(2, post.url)
                insertStatement.bindString(3, post.header)
                insertStatement.bindString(4, post.imageUrl)
                insertStatement.bindString(5, post.publishDate)
                insertStatement.bindString(6, post.teaser)

                if(post.section != null) {
                    insertStatement.bindString(7, post.section)
                    updateStatement.bindString(1, post.section)
                } else {
                    insertStatement.bindNull(7)
                    updateStatement.bindNull(1)
                }

                updateStatement.bindLong(2, post.id)

                insertStatement.executeInsert()
                updateStatement.executeUpdateDelete()
            }

            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
        lowLevel.notifyAboutChanges(Changes.newInstance(DB_POSTS_TABLE))
    }
}
