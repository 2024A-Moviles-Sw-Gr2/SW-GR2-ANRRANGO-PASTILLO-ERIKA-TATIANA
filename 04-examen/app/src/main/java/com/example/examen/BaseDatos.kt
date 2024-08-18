package com.example.examen

import android.content.Context

object BaseDatos {
    private lateinit var sqliteHelperCategoria: SqliteHelperCategoria
    private lateinit var sqliteHelperProducto: SqliteHelperProducto

    fun inicializar(context: Context) {
        sqliteHelperCategoria = SqliteHelperCategoria(context)
        sqliteHelperProducto = SqliteHelperProducto(context)
    }

    fun obtenerCategorias(): List<EntrenadorCategoria> {
        return sqliteHelperCategoria.obtenerCategorias()
    }

    fun obtenerProductosPorCategoria(categoriaID: Int): List<EntrenadorProducto> {
        return sqliteHelperProducto.obtenerProductosPorCategoria(categoriaID)
    }

    fun insertarCategoria(categoria: EntrenadorCategoria): Long {
        return sqliteHelperCategoria.insertarCategoria(categoria)
    }

    fun actualizarCategoria(categoria: EntrenadorCategoria): Int {
        return sqliteHelperCategoria.actualizarCategoria(categoria)
    }

    fun eliminarCategoria(id: Int): Int {
        return sqliteHelperCategoria.eliminarCategoria(id)
    }

    fun insertarProducto(producto: EntrenadorProducto): Long {
        return sqliteHelperProducto.insertarProducto(producto)
    }

    fun actualizarProducto(producto: EntrenadorProducto): Int {
        return sqliteHelperProducto.actualizarProducto(producto)
    }

    fun eliminarProducto(id: Int): Int {
        return sqliteHelperProducto.eliminarProducto(id)
    }
}