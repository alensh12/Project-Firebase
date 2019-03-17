package com.example.amprime.firebaseauth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.TextDelegate

class SplashScreen : AppCompatActivity() {
    var imageView: ImageView? = null
    var lotteAnimationView: LottieAnimationView? = null
    private var textDelegate: TextDelegate? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        setContentView(R.layout.activity_splash)
        scheduleSplashScreen()
        super.onCreate(savedInstanceState)
    }


    private fun scheduleSplashScreen() {


        imageView = findViewById(R.id.imageView)
        lotteAnimationView = findViewById(R.id.animationView)

        val splashScreenDuration = getSplashDuration()
        Handler().postDelayed({
            //            imageView.clearAnimation()
            startActivity(Intent(this@SplashScreen, EmailAndPasswordActivity::class.java))
        }, splashScreenDuration)

    }

    private fun getSplashDuration(): Long {
        val sp = getPreferences(Context.MODE_PRIVATE)
        val prefKeyFirstLauch = "prefirstlauch"
        return when (sp.getBoolean(prefKeyFirstLauch, true)) {
            true -> {
                sp.edit().putBoolean(prefKeyFirstLauch, false).apply()
                4000
            }
            false -> {
                1000
            }
        }
    }
}