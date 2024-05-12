package com.example.tasktimerapp.welcome_instructions

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.tasktimerapp.MainActivity
import com.example.tasktimerapp.R

class WelcomeScreen : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome_screen)

        sharedPreferences =
            this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val checkWelcomeStatus = sharedPreferences.getInt("CheckWelcomeStatus", 0)

        val handler = Handler()
        handler.postDelayed({
            if (checkWelcomeStatus == 1) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, InstructionsScreen::class.java))
            }
        }, 20000)

    }
}