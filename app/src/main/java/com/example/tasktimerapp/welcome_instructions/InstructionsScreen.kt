package com.example.tasktimerapp.welcome_instructions

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.tasktimerapp.MainActivity
import com.example.tasktimerapp.R
import com.example.tasktimerapp.databinding.InstructionsScreenBinding

class InstructionsScreen : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: InstructionsScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.instructions_screen)

        binding = InstructionsScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences =
            this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
    }

    fun welcomePage(view: View) {
        binding.welcomeLay.isVisible = false
        binding.timerLay.isVisible = true
    }

    fun timerNext(view: View) {
        binding.timerLay.isVisible = false
        binding.taskLay.isVisible = true
    }

    fun tasksShow(view: View) {
        binding.taskLay.isVisible = false
        binding.startTimeLay.isVisible = true
    }

    fun starTask(view: View) {
        binding.startTimeLay.isVisible = false
        binding.editSwipe.isVisible = true
    }

    fun buttonsShow(view: View) {
        binding.buttonsLay.isVisible = false
        binding.addPage.isVisible = true
    }

    fun updateWay(view: View) {
        binding.editSwipe.isVisible = false
        binding.deleteSwipe.isVisible = true
    }

    fun deleteWay(view: View) {
        binding.deleteSwipe.isVisible = false
        binding.buttonsLay.isVisible = true
    }

    fun addPage(view: View) {
        binding.addPage.isVisible = false
        binding.updatePage.isVisible = true
    }

    fun updatePage(view: View) {
        binding.updatePage.isVisible = false
        binding.swipeBack.isVisible = true
    }

    fun piePage(view: View) {
        binding.piePage.isVisible = false
        binding.finallyEnd.isVisible = true
    }

    fun backWay(view: View) {
        binding.swipeBack.isVisible = false
        binding.piePage.isVisible = true
    }

    fun finishAndStart(view: View) {
        if (binding.checkBox.isChecked)
            with(sharedPreferences.edit()) {
                putInt("CheckWelcomeStatus", 1)
                apply()
            }
        startActivity(Intent(this, MainActivity::class.java))
    }
}