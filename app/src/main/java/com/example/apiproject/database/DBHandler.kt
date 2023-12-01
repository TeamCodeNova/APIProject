package com.example.apiproject.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Base64
import com.example.apiproject.SearchForTermQuery

class DBHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "PodcastDB"
        const val TABLE_FAVORITES = "favorites"
        const val TABLE_USERS = "users"

        // Favorites table columns
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

        // Users table columns
        const val KEY_USER_ID = "id"
        const val KEY_USERNAME = "username"
        const val KEY_PASSWORD = "password"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createFavoritesTableQuery = """
            CREATE TABLE $TABLE_FAVORITES(
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
        db.execSQL(createFavoritesTableQuery)

        val createUsersTableQuery = """
            CREATE TABLE $TABLE_USERS(
                $KEY_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $KEY_USERNAME TEXT UNIQUE,
                $KEY_PASSWORD TEXT
            )
        """.trimIndent()
        db.execSQL(createUsersTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FAVORITES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    // Function to add a podcast to favorites
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

        db.insert(TABLE_FAVORITES, null, values)
        db.close()
    }

    // Function to get all favorites in descending order
    fun getAllFavorites(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_FAVORITES ORDER BY $KEY_ID DESC", null)
    }

    // Function to delete a podcast from favorites
    fun deleteFavorite(uuid: String) {
        val db = this.writableDatabase
        db.delete(TABLE_FAVORITES, "$KEY_UUID=?", arrayOf(uuid))
        db.close()
    }

    // User Management functions

    // Function to register a new user
    fun registerUser(username: String, password: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_USERNAME, username)
            put(KEY_PASSWORD, hashPassword(password)) // Implement password hashing
        }
        val userId = db.insert(TABLE_USERS, null, values)
        db.close()
        return userId
    }

    // Function to check if a user exists and the password is correct
    fun checkUser(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_USERS,
            arrayOf(KEY_USER_ID),
            "$KEY_USERNAME = ? AND $KEY_PASSWORD = ?",
            arrayOf(username, hashPassword(password)), // Implement password hashing
            null, null, null
        )
        val userExists = cursor.count > 0
        cursor.close()
        db.close()
        return userExists
    }

    fun getUserDetails(): Pair<String, String>? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_USERS,
            arrayOf(KEY_USERNAME, KEY_PASSWORD),
            null, null, null, null, null
        )
        var userDetails: Pair<String, String>? = null
        if (cursor.moveToFirst()) {
            val usernameIndex = cursor.getColumnIndex(KEY_USERNAME)
            val passwordIndex = cursor.getColumnIndex(KEY_PASSWORD)
            if (usernameIndex != -1 && passwordIndex != -1) {
                val username = cursor.getString(usernameIndex)
                val password = cursor.getString(passwordIndex) // This is hashed
                userDetails = Pair(username, Base64.decode(password, Base64.DEFAULT).toString(Charsets.UTF_8)) // Decode if needed
            }
        }
        cursor.close()
        db.close()
        return userDetails
    }


    // Helper function to hash passwords
    private fun hashPassword(password: String): String {
        // Password hashing
        return Base64.encodeToString(password.toByteArray(), Base64.DEFAULT)
    }
}