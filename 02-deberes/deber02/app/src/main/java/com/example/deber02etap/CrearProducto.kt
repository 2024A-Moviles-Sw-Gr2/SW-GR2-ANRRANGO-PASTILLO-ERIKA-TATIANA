package com.example.deber02etap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CrearProducto : AppCompatActivity() {
    private lateinit var categoria: EntrenadorCategoria

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_producto)

        setupWindowInsets()
        obtenerDatosIntent()
        configurarVista()
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl_crearProducto)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Obtiene los datos del intent y establece la categoría
    private fun obtenerDatosIntent() {
        categoria = intent.getParcelableExtra("categoria") ?: run {
            mostrarSnackbar("Error al obtener la categoría")
            finish()
            return
        }
    }

    // Configura la vista dependiendo de si es para crear o actualizar un producto
    private fun configurarVista() {
        val producto = intent.getParcelableExtra<EntrenadorProducto>("producto")
        val botonGuardar = findViewById<Button>(R.id.btn_guardarProducto)
        val nombre = findViewById<EditText>(R.id.input_nombreProducto)
        val precio = findViewById<EditText>(R.id.input_precioProducto)
        val rbtnSi = findViewById<RadioButton>(R.id.rbtn_siProducto)
        val rbtnNo = findViewById<RadioButton>(R.id.rbtn_noProducto)

        if (producto != null) {
            llenarCamposProducto(producto, nombre, precio, rbtnSi, rbtnNo)
            botonGuardar.text = "Actualizar"
            botonGuardar.setOnClickListener { actualizarProducto(producto) }
        } else {
            botonGuardar.setOnClickListener { crearProducto() }
        }
    }

    // Llena los campos de la vista con los datos del producto a actualizar
    private fun llenarCamposProducto(
        producto: EntrenadorProducto,
        nombre: EditText,
        precio: EditText,
        rbtnSi: RadioButton,
        rbtnNo: RadioButton
    ) {
        nombre.setText(producto.nombre)
        precio.setText(producto.precio.toString())
        if (producto.enStock) {
            rbtnSi.isChecked = true
        } else {
            rbtnNo.isChecked = true
        }
    }

    // Actualiza un producto existente en la base de datos
    private fun actualizarProducto(producto: EntrenadorProducto) {
        producto.nombre = obtenerTexto(R.id.input_nombreProducto)
        producto.precio = obtenerTexto(R.id.input_precioProducto).toDouble()
        producto.enStock = estaEnStock(R.id.rbtn_siProducto)

        val index = BaseDatosMemoria.arregloProductos.indexOfFirst { it.id == producto.id }
        if (index != -1) {
            BaseDatosMemoria.arregloProductos[index] = producto
            mostrarSnackbar("Producto actualizado exitosamente")
            enviarResultado(producto, "productoActualizado")
        }
    }

    // Crea un nuevo producto y lo agrega a la base de datos
    private fun crearProducto() {
        val nuevoProducto = EntrenadorProducto(
            id = generarNuevoId(),
            nombre = obtenerTexto(R.id.input_nombreProducto),
            precio = obtenerTexto(R.id.input_precioProducto).toDouble(),
            fechaCreacion = obtenerFechaActual(),
            enStock = estaEnStock(R.id.rbtn_siProducto),
            categoriaID = categoria.id
        )

        BaseDatosMemoria.arregloProductos.add(nuevoProducto)
        mostrarSnackbar("Producto creado exitosamente")
        enviarResultado(nuevoProducto, "nuevoProducto")
    }

    // Muestra un Snackbar con un mensaje
    private fun mostrarSnackbar(texto: String) {
        val snack = Snackbar.make(
            findViewById(R.id.cl_crearProducto), texto, Snackbar.LENGTH_LONG
        )
        snack.show()
    }

    // Envía el resultado de la operación y finaliza la actividad
    private fun enviarResultado(producto: EntrenadorProducto, clave: String) {
        val intent = Intent().apply {
            putExtra(clave, producto)
        }
        setResult(RESULT_OK, intent)
        finish()
    }

    // Obtiene el texto de un EditText por su ID
    private fun obtenerTexto(editTextId: Int): String {
        return findViewById<EditText>(editTextId).text.toString()
    }

    // Verifica si el producto está en stock basado en el estado de un RadioButton
    private fun estaEnStock(radioButtonId: Int): Boolean {
        return findViewById<RadioButton>(radioButtonId).isChecked
    }

    // Genera un nuevo ID para un producto
    private fun generarNuevoId(): Int {
        return (BaseDatosMemoria.arregloProductos.maxOfOrNull { it.id } ?: 0) + 1
    }

    // Obtiene la fecha actual en formato "yyyy-MM-dd"
    private fun obtenerFechaActual(): String {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }
}




