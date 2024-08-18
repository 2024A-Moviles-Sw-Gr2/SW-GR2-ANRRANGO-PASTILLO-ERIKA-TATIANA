package com.example.focusflex

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast

class Flashcard : AppCompatActivity() {
    private lateinit var dbHelper: FlashcardDatabaseHelper
    private lateinit var linearLayout: LinearLayout
    private var selectedTopic: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flashcard)

        // Inicializar la base de datos y el LinearLayout
        dbHelper = FlashcardDatabaseHelper(this)
        linearLayout = findViewById(R.id.lyContainerFlashcard)

        // Cargar y mostrar los botones de los temas
        loadFlashcardButtons()

        // Botón para agregar un nuevo tema de flashcard
        val btnAdd = findViewById<Button>(R.id.btnAdd)
        btnAdd.setOnClickListener {
            val intent = Intent(this, AddFlashcard::class.java)
            startActivity(intent)
        }
    }

    // Cargar los temas desde la base de datos y crear botones dinámicos
    private fun loadFlashcardButtons() {
        val topics = dbHelper.getUniqueTopics()
        linearLayout.removeAllViews()

        topics.forEach { topic ->
            val button = Button(this).apply {
                text = topic
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                setOnLongClickListener {
                    selectedTopic = topic
                    registerForContextMenu(it)
                    openContextMenu(it)
                    unregisterForContextMenu(it)
                    true
                }
            }

            // Listener para abrir la lista de preguntas y respuestas asociadas al tema
            button.setOnClickListener {
                val intent = Intent(this, ViewFlashcard::class.java)
                intent.putExtra("TOPIC", topic)
                startActivity(intent)
            }

            linearLayout.addView(button)
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_flashcard, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_eliminar -> {
                abrirDialogoEliminar()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    // Abre un diálogo para confirmar la eliminación de la flashcard seleccionada
    private fun abrirDialogoEliminar() {
        AlertDialog.Builder(this)
            .setTitle("¿Está seguro de eliminar?")
            .setPositiveButton("Sí") { _, _ ->
                eliminarFlashcard()
            }
            .setNegativeButton("No", null)
            .create()
            .show()
    }

    // Elimina la flashcard seleccionada de la base de datos y actualiza la UI
    private fun eliminarFlashcard() {
        selectedTopic?.let {
            dbHelper.deleteFlashcardByTopic(it)
            loadFlashcardButtons()
            Toast.makeText(this, "Flashcard eliminada", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        // Refrescar los botones cuando se regresa a la actividad
        loadFlashcardButtons()
    }
}