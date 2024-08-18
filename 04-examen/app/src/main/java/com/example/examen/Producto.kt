package com.example.examen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

class Producto : AppCompatActivity() {
    private lateinit var adaptador: ArrayAdapter<EntrenadorProducto>
    private lateinit var categoriaActual: EntrenadorCategoria
    private var posicionItemSeleccionado = -1

    // SQLite Helper para manejar operaciones en la base de datos.
    private lateinit var sqliteHelperProducto: SqliteHelperProducto

    // Inicializa la actividad, configurando la UI y adaptador, y manejando los eventos de clic.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_producto)

        // Inicializa el SQLite Helper
        sqliteHelperProducto = SqliteHelperProducto(this)

        configurarInsets()
        inicializarCategoriaActual()
        configurarListaProductos()
        configurarBotonMapa()
        configurarBotonCrearProducto()

        registerForContextMenu(findViewById(R.id.lv_productos))
    }

    private fun configurarBotonMapa() {
        findViewById<Button>(R.id.btn_verEnMapa).setOnClickListener {
            val intent = Intent(this, GoogleMaps::class.java)
            intent.putExtra("categoria", categoriaActual)
            startActivityForResult(intent, Categoria.REQUEST_CODE_ADD_OR_EDIT)
        }
    }

    // Configura los insets de la ventana para gestionar la UI.
    private fun configurarInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl_producto)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Inicializa la categoría actual a partir del Intent recibido.
    private fun inicializarCategoriaActual() {
        categoriaActual = intent.getParcelableExtra("categoria") ?: run {
            finish()
            return
        }
        findViewById<TextView>(R.id.tv_categoria).text = categoriaActual.nombre
    }

    // Configura la lista de productos y el adaptador que maneja la visualización.
    private fun configurarListaProductos() {
        val listView = findViewById<ListView>(R.id.lv_productos)
        val productosFiltrados = obtenerProductosFiltrados()
        adaptador = ArrayAdapter(this, android.R.layout.simple_list_item_1, productosFiltrados)
        listView.adapter = adaptador
    }

    // Configura el botón para crear un nuevo producto.
    private fun configurarBotonCrearProducto() {
        findViewById<Button>(R.id.btn_crearProducto).setOnClickListener {
            val intent = Intent(this, CrearProducto::class.java)
            intent.putExtra("categoria", categoriaActual)
            startActivityForResult(intent, Categoria.REQUEST_CODE_ADD_OR_EDIT)
        }
    }

    // Maneja el resultado de actividades iniciadas para crear o editar productos.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Categoria.REQUEST_CODE_ADD_OR_EDIT && resultCode == RESULT_OK) {
            actualizarListaProductos()
        }
    }

    // Actualiza la lista de productos filtrada por la categoría actual.
    private fun actualizarListaProductos() {
        val productosFiltrados = obtenerProductosFiltrados()
        adaptador.clear()
        adaptador.addAll(productosFiltrados)
        adaptador.notifyDataSetChanged()
    }

    // Crea el menú contextual para las opciones de producto (editar, eliminar).
    override fun onCreateContextMenu(
        menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_producto, menu)
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        posicionItemSeleccionado = info.position
    }

    // Maneja la selección de un ítem del menú contextual.
    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_editarProducto -> {
                editarProductoSeleccionado()
                true
            }

            R.id.mi_eliminarProducto -> {
                abrirDialogoEliminarProducto()
                true
            }

            else -> super.onContextItemSelected(item)
        }
    }

    // Inicia la actividad para editar el producto seleccionado.
    private fun editarProductoSeleccionado() {
        val productoSeleccionado = adaptador.getItem(posicionItemSeleccionado)
        val intent = Intent(this, CrearProducto::class.java).apply {
            putExtra("producto", productoSeleccionado)
            putExtra("categoria", categoriaActual)
        }
        startActivityForResult(intent, Categoria.REQUEST_CODE_ADD_OR_EDIT)
    }

    // Abre un diálogo para confirmar la eliminación del producto seleccionado.
    private fun abrirDialogoEliminarProducto() {
        AlertDialog.Builder(this).setTitle("¿Está seguro de eliminar?")
            .setPositiveButton("Sí") { _, _ -> eliminarProductoSeleccionado() }
            .setNegativeButton("No", null).create().show()
    }

    // Elimina el producto seleccionado de la base de datos y actualiza la lista.
    private fun eliminarProductoSeleccionado() {
        val productoSeleccionado = adaptador.getItem(posicionItemSeleccionado)
        productoSeleccionado?.let {
            sqliteHelperProducto.eliminarProducto(it.id)
            actualizarListaProductos()
            mostrarSnackbar("Se eliminó el producto")
        }
    }

    // Muestra un Snackbar con un mensaje y lo cierra después de un tiempo.
    private fun mostrarSnackbar(texto: String) {
        Snackbar.make(findViewById(R.id.cl_producto), texto, Snackbar.LENGTH_INDEFINITE).apply {
            show()
            Handler(Looper.getMainLooper()).postDelayed({ dismiss() }, 4000)
        }
    }

    // Filtra los productos por la categoría actual utilizando SQLite.
    private fun obtenerProductosFiltrados(): List<EntrenadorProducto> {
        return sqliteHelperProducto.obtenerProductosPorCategoria(categoriaActual.id)
    }

    // Implementa la lógica para obtener productos de la base de datos
    fun obtenerProductos(): List<EntrenadorProducto> {
        return sqliteHelperProducto.obtenerTodosLosProductos() // Suponiendo que tienes este método
    }
}
