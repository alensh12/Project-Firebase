package com.example.amprime.firebaseauth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_animation.*

class AnimationActivity : AppCompatActivity() {
    private var speed = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_animation)
        speedButton.setOnClickListener {
            speed = ++speed % 4
            updateButtonText()
        }
        super.onCreate(savedInstanceState)


    }

    private fun updateButtonText() {
        speedButton.text = "Wave: ${speed}x Speed"
    }

}
