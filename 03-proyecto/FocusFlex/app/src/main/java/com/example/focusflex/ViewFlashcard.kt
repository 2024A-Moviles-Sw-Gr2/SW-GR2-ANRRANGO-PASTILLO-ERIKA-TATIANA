package com.example.focusflex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView

class ViewFlashcard : AppCompatActivity() {
    private lateinit var dbHelper: FlashcardDatabaseHelper
    private lateinit var linearLayout: LinearLayout
    private lateinit var topic: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_flashcard)

        // Obtener el tema desde la intención
        topic = intent.getStringExtra("TOPIC") ?: ""

        // Inicializar la base de datos y el contenedor de layout
        dbHelper = FlashcardDatabaseHelper(this)
        linearLayout = findViewById(R.id.linearLayoutView)

        // Cargar y mostrar las preguntas y respuestas asociadas al tema
        loadFlashcardDetails()
    }

    // Cargar los detalles de las flashcards desde la base de datos
    private fun loadFlashcardDetails() {
        // Obtener las flashcards que pertenecen al tema específico
        val flashcards = dbHelper.getFlashcardsByTopic(topic)

        flashcards.forEach { flashcard ->
            val questionTextView = TextView(this).apply {
                text = "Pregunta: ${flashcard.question}"
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                textSize = 18f
                setPadding(0, 8, 0, 8)
            }

            val answerTextView = TextView(this).apply {
                text = "Respuesta: ${flashcard.answer}"
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                textSize = 16f
                setPadding(0, 8, 0, 16)
            }

            // Añadir las preguntas y respuestas al layout
            linearLayout.addView(questionTextView)
            linearLayout.addView(answerTextView)
        }
    }
}