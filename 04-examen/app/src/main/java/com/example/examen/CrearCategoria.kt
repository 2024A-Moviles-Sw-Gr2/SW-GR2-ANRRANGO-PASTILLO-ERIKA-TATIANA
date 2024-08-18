package com.example.examen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

class CrearCategoria : AppCompatActivity() {
    private lateinit var sqliteHelperCategoria: SqliteHelperCategoria

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_categoria)

        // Inicializa el helper de SQLite
        sqliteHelperCategoria = SqliteHelperCategoria(this)

        configurarVista()
        configurarBotonGuardar()
    }

    // Configura la vista principal con ajustes para las barras de sistema
    private fun configurarVista() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl_crearCategoria)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Configura el botón de guardar, ya sea para crear o actualizar una categoría
    private fun configurarBotonGuardar() {
        val botonGuardar = findViewById<Button>(R.id.btn_guardarCategoria)
        val categoria = intent.getParcelableExtra<EntrenadorCategoria>("categoria")

        if (categoria != null) {
            configurarVistaEdicion(categoria)
            botonGuardar.setOnClickListener { actualizarCategoria(categoria) }
        } else {
            botonGuardar.setOnClickListener { crearCategoria() }
        }
    }

    // Configura la vista para editar una categoría existente
    private fun configurarVistaEdicion(categoria: EntrenadorCategoria) {
        findViewById<EditText>(R.id.input_nombreCategoria).setText(categoria.nombre)
        findViewById<EditText>(R.id.input_descripcionCategoria).setText(categoria.descripcion)
        val rbtnSiCategoria = findViewById<RadioButton>(R.id.rbtn_siCategoria)
        val rbtnNoCategoria = findViewById<RadioButton>(R.id.rbtn_noCategoria)
        if (categoria.estaVisible) {
            rbtnSiCategoria.isChecked = true
        } else {
            rbtnNoCategoria.isChecked = true
        }
        findViewById<Button>(R.id.btn_guardarCategoria).text = "Actualizar"
    }

    // Actualiza una categoría existente en la base de datos SQLite
    private fun actualizarCategoria(categoria: EntrenadorCategoria) {
        val nombre = findViewById<EditText>(R.id.input_nombreCategoria).text.toString()
        val descripcion = findViewById<EditText>(R.id.input_descripcionCategoria).text.toString()
        val estaVisible = findViewById<RadioButton>(R.id.rbtn_siCategoria).isChecked

        // Actualiza los atributos de la categoría
        categoria.nombre = nombre
        categoria.descripcion = descripcion
        categoria.estaVisible = estaVisible

        // Actualiza la categoría en la base de datos SQLite
        val filasActualizadas = sqliteHelperCategoria.actualizarCategoria(categoria)
        if (filasActualizadas > 0) {
            mostrarSnackbar("Categoría actualizada exitosamente")
            setResult(RESULT_OK)
            finish()
        } else {
            mostrarSnackbar("Error al actualizar la categoría")
        }
    }

    // Crea una nueva categoría y la agrega a la base de datos SQLite
    private fun crearCategoria() {
        val nombre = findViewById<EditText>(R.id.input_nombreCategoria).text.toString()
        val descripcion = findViewById<EditText>(R.id.input_descripcionCategoria).text.toString()
        val estaVisible = findViewById<RadioButton>(R.id.rbtn_siCategoria).isChecked

        // Crea una nueva categoría y la inserta en la base de datos SQLite
        val nuevaCategoria = EntrenadorCategoria(0, nombre, descripcion, estaVisible) // ID se generará automáticamente
        val idCategoria = sqliteHelperCategoria.insertarCategoria(nuevaCategoria)

        if (idCategoria > 0) {
            mostrarSnackbar("Categoría creada exitosamente")
            setResult(RESULT_OK)
            finish()
        } else {
            mostrarSnackbar("Error al crear la categoría")
        }
    }

    // Muestra un Snackbar con un mensaje específico
    private fun mostrarSnackbar(mensaje: String) {
        Snackbar.make(
            findViewById(R.id.cl_crearCategoria),
            mensaje,
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            show()
            Handler(Looper.getMainLooper()).postDelayed({ dismiss() }, 4000)
        }
    }
}
