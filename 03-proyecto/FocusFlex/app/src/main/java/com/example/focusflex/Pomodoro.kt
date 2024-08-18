package com.example.focusflex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView

class Pomodoro : AppCompatActivity() {
    // Duración inicial del Pomodoro en milisegundos (25 minutos)
    private var pomodoroDuration: Long = 25 * 60 * 1000

    // Duración del descanso en milisegundos (5 minutos)
    private var restDuration: Long = 5 * 60 * 1000

    // Temporizador
    private var countDownTimer: CountDownTimer? = null

    // Variable para saber si el temporizador está en pausa
    private var isPaused: Boolean = false

    // Tiempo restante cuando se pausa
    private var timeLeftInMillis: Long = pomodoroDuration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pomodoro)

        // Inicializar las vistas aquí, después de setContentView
        val btnStart = findViewById<Button>(R.id.btnStart)
        val btnPause = findViewById<Button>(R.id.btnPause)
        val btnContinue = findViewById<Button>(R.id.btnContinue)
        val btnRestart = findViewById<Button>(R.id.btnRestart)
        val btnRest = findViewById<Button>(R.id.btnRest)
        val tvTimer = findViewById<TextView>(R.id.tvTimer)

        btnStart.setOnClickListener {
            startTimer(pomodoroDuration)
            btnStart.visibility = View.GONE
            btnPause.visibility = View.VISIBLE
            btnRestart.visibility = View.VISIBLE
        }

        btnPause.setOnClickListener {
            pauseTimer()
            btnPause.visibility = View.GONE
            btnContinue.visibility = View.VISIBLE
            btnRestart.visibility = View.VISIBLE
            btnRest.visibility = View.VISIBLE
        }

        btnContinue.setOnClickListener {
            startTimer(timeLeftInMillis)
            btnContinue.visibility = View.GONE
            btnRestart.visibility = View.GONE
            btnRest.visibility = View.GONE
            btnPause.visibility = View.VISIBLE
        }

        btnRestart.setOnClickListener {
            resetTimer()
            btnContinue.visibility = View.GONE
            btnRestart.visibility = View.GONE
            btnRest.visibility = View.GONE
            btnStart.visibility = View.VISIBLE
        }

        btnRest.setOnClickListener {
            startTimer(restDuration)
            btnContinue.visibility = View.GONE
            btnRestart.visibility = View.VISIBLE
            btnRest.visibility = View.GONE
            btnPause.visibility = View.VISIBLE
        }
    }

    // Función para iniciar o reiniciar el temporizador
    private fun startTimer(duration: Long) {
        countDownTimer = object : CountDownTimer(duration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimerText()
            }

            override fun onFinish() {
                resetTimer()
            }
        }.start()
    }

    // Función para pausar el temporizador
    private fun pauseTimer() {
        countDownTimer?.cancel()
        isPaused = true
    }

    // Función para reiniciar el temporizador al valor original
    private fun resetTimer() {
        timeLeftInMillis = pomodoroDuration
        updateTimerText()
        findViewById<Button>(R.id.btnPause).visibility = View.GONE
        findViewById<Button>(R.id.btnStart).visibility = View.VISIBLE
    }

    // Actualizar el texto del temporizador en la pantalla
    private fun updateTimerText() {
        val minutes = (timeLeftInMillis / 1000) / 60
        val seconds = (timeLeftInMillis / 1000) % 60
        val timeFormatted = String.format("%02d:%02d", minutes, seconds)
        findViewById<TextView>(R.id.tvTimer).text = timeFormatted
    }
}
