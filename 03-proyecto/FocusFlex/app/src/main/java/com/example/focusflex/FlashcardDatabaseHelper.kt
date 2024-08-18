package com.example.focusflex

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class FlashcardDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "flashcards.db"
        private const val DATABASE_VERSION = 1

        // Nombre de la tabla y columnas
        const val TABLE_NAME = "flashcards"
        const val COLUMN_ID = "id"
        const val COLUMN_TOPIC = "topic"
        const val COLUMN_QUESTION = "question"
        const val COLUMN_ANSWER = "answer"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_TOPIC TEXT," +
                "$COLUMN_QUESTION TEXT," +
                "$COLUMN_ANSWER TEXT)")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Función para agregar una nueva flashcard
    fun addFlashcard(topic: String, question: String, answer: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_TOPIC, topic)
        contentValues.put(COLUMN_QUESTION, question)
        contentValues.put(COLUMN_ANSWER, answer)

        return db.insert(TABLE_NAME, null, contentValues)
    }

    // Función para eliminar flashcards por tema
    fun deleteFlashcardByTopic(topic: String) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_TOPIC = ?", arrayOf(topic))
    }

    // Función para obtener flashcards por tema
    fun getFlashcardsByTopic(topic: String): List<EntrenadorFlashcard> {
        val flashcards = mutableListOf<EntrenadorFlashcard>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COLUMN_TOPIC = ?", arrayOf(topic))

        if (cursor.moveToFirst()) {
            do {
                val flashcard = EntrenadorFlashcard(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TOPIC)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QUESTION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ANSWER))
                )
                flashcards.add(flashcard)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return flashcards
    }
    // Función para obtener los temas únicos de las flashcards
    fun getUniqueTopics(): List<String> {
        val topics = mutableListOf<String>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT DISTINCT $COLUMN_TOPIC FROM $TABLE_NAME", null)

        if (cursor.moveToFirst()) {
            do {
                val topic = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TOPIC))
                topics.add(topic)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return topics
    }

}