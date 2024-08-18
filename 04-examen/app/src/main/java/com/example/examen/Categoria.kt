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
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

class Categoria : AppCompatActivity() {
    private lateinit var adaptador: ArrayAdapter<EntrenadorCategoria>
    private lateinit var sqliteHelperCategoria: SqliteHelperCategoria
    private lateinit var listaCategorias: MutableList<EntrenadorCategoria>

    companion object {
        const val REQUEST_CODE_ADD_OR_EDIT = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categoria)

        sqliteHelperCategoria = SqliteHelperCategoria(this)
        listaCategorias = sqliteHelperCategoria.obtenerCategorias().toMutableList()

        configurarVista()
        configurarListView()
        configurarBotonCrear()
        registerForContextMenu(findViewById(R.id.lv_categorias))
    }

    private fun configurarVista() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl_categoria)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun configurarListView() {
        val listView = findViewById<ListView>(R.id.lv_categorias)
        adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            listaCategorias
        )
        listView.adapter = adaptador
    }

    private fun configurarBotonCrear() {
        val botonCrear = findViewById<Button>(R.id.btn_crearCategoria)
        botonCrear.setOnClickListener {
            irActividad(CrearCategoria::class.java, REQUEST_CODE_ADD_OR_EDIT)
        }
    }

    private fun irActividad(clase: Class<*>, requestCode: Int) {
        val intent = Intent(this, clase)
        startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_OR_EDIT && resultCode == RESULT_OK) {
            actualizarListaCategorias()
        }
    }

    private fun actualizarListaCategorias() {
        listaCategorias.clear()
        listaCategorias.addAll(sqliteHelperCategoria.obtenerCategorias())
        adaptador.notifyDataSetChanged()
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

    private fun editarCategoria() {
        val categoriaSeleccionada = listaCategorias[posicionItemSeleccionado]
        val intent = Intent(this, CrearCategoria::class.java).apply {
            putExtra("categoria", categoriaSeleccionada)
        }
        startActivityForResult(intent, REQUEST_CODE_ADD_OR_EDIT)
    }

    private fun visualizarProducto() {
        val categoriaSeleccionada = listaCategorias[posicionItemSeleccionado]
        val intent = Intent(this, Producto::class.java).apply {
            putExtra("categoria", categoriaSeleccionada)
        }
        startActivity(intent)
    }

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

    private fun eliminarCategoria() {
        val categoriaSeleccionada = listaCategorias[posicionItemSeleccionado]
        sqliteHelperCategoria.eliminarCategoria(categoriaSeleccionada.id)
        actualizarListaCategorias()
        mostrarSnackbar("Se eliminó la categoría")
    }

    private fun mostrarSnackbar(mensaje: String) {
        Snackbar.make(findViewById(R.id.cl_categoria), mensaje, Snackbar.LENGTH_LONG).show()
    }
}
