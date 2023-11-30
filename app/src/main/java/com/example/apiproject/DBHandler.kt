package com.example.apiproject

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/// <summary>
/// Handles all DB Interactions
/// </summary>
/// <param="context">Current App's Context</param>
class DBHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    /// <summary>
    /// Displays all information about the database.
    /// This data will be used as constant, and a placeholder for some values in the db.
    /// </summary>
    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "PodcastDB"
        const val TABLE_NAME = "favorites"

        // Column names
        const val KEY_ID = "id"
        const val KEY_UUID = "uuid"
        const val KEY_NAME = "name"
        const val KEY_DESCRIPTION = "description"
        const val KEY_IMAGE_URL = "imageUrl"
        const val KEY_EPISODES = "totalEpisodesCount"
        const val KEY_GENRES = "genres"
        const val KEY_LANGUAGE = "language"
        const val KEY_CONTENT_TYPE = "contentType"
        const val KEY_AUTHOR = "authorName"
        const val KEY_WEBSITE_URL = "websiteUrl"
    }

    /// <summary>
    /// Creates the Database and table
    /// </summary>
    /// <param="db">Current DB Object</param>

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME(
                $KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $KEY_UUID TEXT,
                $KEY_NAME TEXT,
                $KEY_DESCRIPTION TEXT,
                $KEY_IMAGE_URL TEXT,
                $KEY_EPISODES INTEGER,
                $KEY_GENRES TEXT,
                $KEY_LANGUAGE TEXT,
                $KEY_CONTENT_TYPE TEXT,
                $KEY_AUTHOR TEXT,
                $KEY_WEBSITE_URL TEXT
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    /// <summary>
    /// Activates once data in the companion table changes.
    /// Will rebuild the database's table, and will remove all records currently in the table
    /// </summary>
    /// <param="db">Current DB Object</param>
    /// <param="oldVersion">Old numeric version of db. Example: 1</param>
    /// <param="newVersion">New numeric version of db. Example: 2</param>
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop older table if it exists and create a new one
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    /// <summary>
    /// This function gets all favourites in descending order
    /// </summary>
    /// <return>All favourites in the database</return>
    fun getAllFavorites(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME ORDER BY $KEY_ID DESC", null)
    }

    /// <summary>
    /// This function will add a podcast to favorites
    /// </summary>
    /// <param="podcast"> The podcast item that will be added to favorites </param>
    fun addFavorite(podcast: SearchForTermQuery.PodcastSeries) {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(KEY_UUID, podcast.uuid)
        values.put(KEY_NAME, podcast.name)
        values.put(KEY_DESCRIPTION, podcast.description ?: "")
        values.put(KEY_IMAGE_URL, podcast.imageUrl ?: "")
        values.put(KEY_EPISODES, podcast.totalEpisodesCount ?: 0)
        values.put(KEY_GENRES, podcast.genres?.joinToString(", ") ?: "")
        values.put(KEY_LANGUAGE, podcast.language?.rawValue ?: "")
        values.put(KEY_CONTENT_TYPE, podcast.contentType?.rawValue ?: "")
        values.put(KEY_AUTHOR, podcast.authorName ?: "")
        values.put(KEY_WEBSITE_URL, podcast.websiteUrl ?: "")

        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    /// <summary>
    /// This function will delete a podcasts from favorites
    /// </summary>
    /// <param="uuid"> The podcasts Id </param>
    fun deleteFavorite(uuid: String) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$KEY_UUID=?", arrayOf(uuid))
        db.close()
    }
}
