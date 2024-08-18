package com.example.examen

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SqliteHelperProducto(context: Context) : SQLiteOpenHelper(context, "productos.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE Producto (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT,
                precio REAL,
                fechaCreacion TEXT,
                enStock INTEGER,
                categoriaID INTEGER,
                FOREIGN KEY (categoriaID) REFERENCES Categoria(id)
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Producto")
        onCreate(db)
    }

    fun insertarProducto(producto: EntrenadorProducto): Long {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put("nombre", producto.nombre)
            put("precio", producto.precio)
            put("fechaCreacion", producto.fechaCreacion)
            put("enStock", if (producto.enStock) 1 else 0)
            put("categoriaID", producto.categoriaID)
        }
        return db.insert("Producto", null, contentValues)
    }

    fun actualizarProducto(producto: EntrenadorProducto): Int {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put("nombre", producto.nombre)
            put("precio", producto.precio)
            put("fechaCreacion", producto.fechaCreacion)
            put("enStock", if (producto.enStock) 1 else 0)
        }
        return db.update("Producto", contentValues, "id = ?", arrayOf(producto.id.toString()))
    }

    fun eliminarProducto(id: Int): Int {
        val db = writableDatabase
        return db.delete("Producto", "id = ?", arrayOf(id.toString()))
    }

    fun obtenerProductosPorCategoria(categoriaID: Int): List<EntrenadorProducto> {
        val db = readableDatabase
        val cursor = db.query(
            "Producto", null, "categoriaID = ?", arrayOf(categoriaID.toString()),
            null, null, null
        )
        val listaProductos = mutableListOf<EntrenadorProducto>()

        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow("id"))
                val nombre = getString(getColumnIndexOrThrow("nombre"))
                val precio = getDouble(getColumnIndexOrThrow("precio"))
                val fechaCreacion = getString(getColumnIndexOrThrow("fechaCreacion"))
                val enStock = getInt(getColumnIndexOrThrow("enStock")) == 1
                listaProductos.add(EntrenadorProducto(id, nombre, precio, fechaCreacion, enStock, categoriaID))
            }
        }
        cursor.close()
        return listaProductos
    }
}