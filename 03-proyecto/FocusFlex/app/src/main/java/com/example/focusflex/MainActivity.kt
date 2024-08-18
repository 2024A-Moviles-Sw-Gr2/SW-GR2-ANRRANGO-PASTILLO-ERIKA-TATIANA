package com.example.focusflex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnPomodoro = findViewById<Button>(R.id.btnPomodoro)
        btnPomodoro.setOnClickListener {
            val intent = Intent(this, Pomodoro::class.java)
            startActivity(intent)
        }
        val btnFlashcard = findViewById<Button>(R.id.btnFlashcard)
        btnFlashcard.setOnClickListener {
            val intent = Intent(this, Flashcard::class.java)
            startActivity(intent)
        }

    }
}