package com.example.deber02etap

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
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

class Categoria : AppCompatActivity() {
    private val arregloCategorias = BaseDatosMemoria.arregloCategorias
    private lateinit var adaptador: ArrayAdapter<EntrenadorCategoria>

    companion object {
        const val REQUEST_CODE_ADD_OR_EDIT = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categoria)

        configurarVista()
        configurarListView()
        configurarBotonCrear()
        registerForContextMenu(findViewById(R.id.lv_categorias))
    }

    // Configura la vista principal con ajustes para las barras de sistema
    private fun configurarVista() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl_categoria)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Configura el ListView y su adaptador
    private fun configurarListView() {
        val listView = findViewById<ListView>(R.id.lv_categorias)
        adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            arregloCategorias
        )
        listView.adapter = adaptador
    }

    // Configura el botón para crear nuevas categorías
    private fun configurarBotonCrear() {
        val botonCrear = findViewById<Button>(R.id.btn_crearCategoria)
        botonCrear.setOnClickListener {
            irActividad(CrearCategoria::class.java, REQUEST_CODE_ADD_OR_EDIT)
        }
    }

    // Inicia una nueva actividad para crear o editar una categoría
    private fun irActividad(clase: Class<*>, requestCode: Int) {
        val intent = Intent(this, clase)
        startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_OR_EDIT && resultCode == RESULT_OK) {
            adaptador.notifyDataSetChanged()
        }
    }

    private var posicionItemSeleccionado = -1

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_categoria, menu)
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        posicionItemSeleccionado = info.position
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_editarCategoria -> {
                editarCategoria()
                true
            }
            R.id.mi_eliminarCategoria -> {
                abrirDialogoEliminar()
                true
            }
            R.id.mi_visualizarProducto -> {
                visualizarProducto()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    // Lanza la actividad para editar la categoría seleccionada
    private fun editarCategoria() {
        val categoriaSeleccionada = arregloCategorias[posicionItemSeleccionado]
        val intent = Intent(this, CrearCategoria::class.java).apply {
            putExtra("categoria", categoriaSeleccionada)
        }
        startActivityForResult(intent, REQUEST_CODE_ADD_OR_EDIT)
    }

    // Lanza la actividad para visualizar productos asociados a la categoría seleccionada
    private fun visualizarProducto() {
        val categoriaSeleccionada = arregloCategorias[posicionItemSeleccionado]
        val intent = Intent(this, Producto::class.java).apply {
            putExtra("categoria", categoriaSeleccionada)
        }
        startActivity(intent)
    }

    // Abre un diálogo para confirmar la eliminación de la categoría seleccionada
    private fun abrirDialogoEliminar() {
        AlertDialog.Builder(this)
            .setTitle("Está seguro de eliminar?")
            .setPositiveButton("Sí") { _, _ ->
                eliminarCategoria()
            }
            .setNegativeButton("No", null)
            .create()
            .show()
    }

    // Elimina la categoría seleccionada y actualiza el adaptador
    private fun eliminarCategoria() {
        BaseDatosMemoria.arregloCategorias.removeAt(posicionItemSeleccionado)
        adaptador.notifyDataSetChanged()
        mostrarSnackbar("Se eliminó la categoría")
    }

    // Muestra un Snackbar con un mensaje específico
    private fun mostrarSnackbar(mensaje: String) {
        Snackbar.make(findViewById(R.id.cl_categoria), mensaje, Snackbar.LENGTH_LONG).show()
    }
}
