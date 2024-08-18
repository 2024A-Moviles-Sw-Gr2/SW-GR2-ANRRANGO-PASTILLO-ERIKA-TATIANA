package com.example.examen

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SqliteHelperCategoria(context: Context) : SQLiteOpenHelper(context, "categorias.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE Categoria (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT,
                descripcion TEXT,
                estaVisible INTEGER
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Categoria")
        onCreate(db)
    }

    fun insertarCategoria(categoria: EntrenadorCategoria): Long {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put("nombre", categoria.nombre)
            put("descripcion", categoria.descripcion)
            put("estaVisible", if (categoria.estaVisible) 1 else 0)
        }
        return db.insert("Categoria", null, contentValues)
    }

    fun actualizarCategoria(categoria: EntrenadorCategoria): Int {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put("nombre", categoria.nombre)
            put("descripcion", categoria.descripcion)
            put("estaVisible", if (categoria.estaVisible) 1 else 0)
        }
        return db.update("Categoria", contentValues, "id = ?", arrayOf(categoria.id.toString()))
    }

    fun eliminarCategoria(id: Int): Int {
        val db = writableDatabase
        return db.delete("Categoria", "id = ?", arrayOf(id.toString()))
    }

    fun obtenerCategorias(): List<EntrenadorCategoria> {
        val db = readableDatabase
        val cursor = db.query("Categoria", null, null, null, null, null, null)
        val listaCategorias = mutableListOf<EntrenadorCategoria>()

        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow("id"))
                val nombre = getString(getColumnIndexOrThrow("nombre"))
                val descripcion = getString(getColumnIndexOrThrow("descripcion"))
                val estaVisible = getInt(getColumnIndexOrThrow("estaVisible")) == 1
                listaCategorias.add(EntrenadorCategoria(id, nombre, descripcion, estaVisible))
            }
        }
        cursor.close()
        return listaCategorias
    }
}