package com.example.focusflex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout

class AddFlashcard : AppCompatActivity() {
    private lateinit var dbHelper: FlashcardDatabaseHelper
    private val questionAnswerPairs = mutableListOf<Pair<EditText, EditText>>()
    private lateinit var additionalQuestionsContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_flashcard)

        dbHelper = FlashcardDatabaseHelper(this)
        var pregunta1 = findViewById<EditText>(R.id.etQuestion1)
        var respuesta1= findViewById<EditText>(R.id.etAnswer1)
        questionAnswerPairs.add(Pair(pregunta1, respuesta1))

        additionalQuestionsContainer = findViewById(R.id.additionalQuestionsContainer)
        var btnAddMore = findViewById<Button>(R.id.btnAddMore)
        btnAddMore.setOnClickListener {
            addQuestionAnswerField()
        }

        var btnSave = findViewById<Button>(R.id.btnSave)
        btnSave.setOnClickListener {
            saveFlashcard()
            finish()  // Cerrar la actividad después de guardar
        }
    }

    // Añadir campos adicionales para preguntas y respuestas
    private fun addQuestionAnswerField() {
        val questionView = EditText(this).apply { hint = "Pregunta" }
        val answerView = EditText(this).apply { hint = "Respuesta" }

        additionalQuestionsContainer.addView(questionView)
        additionalQuestionsContainer.addView(answerView)

        questionAnswerPairs.add(Pair(questionView, answerView))
    }

    // Guardar la flashcard en la base de datos
    private fun saveFlashcard() {
        var topicPrincipal = findViewById<EditText>(R.id.etTopic)
        val topic = topicPrincipal.text.toString()

        questionAnswerPairs.forEach { pair ->
            val question = pair.first.text.toString()
            val answer = pair.second.text.toString()

            if (question.isNotEmpty() && answer.isNotEmpty()) {
                dbHelper.addFlashcard(topic, question, answer)
            }
        }
    }
}