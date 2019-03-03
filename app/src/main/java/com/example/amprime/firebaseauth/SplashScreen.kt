package com.example.amprime.firebaseauth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        setContentView(R.layout.activity_splash)
        scheduleSplashScreen()
        super.onCreate(savedInstanceState)
    }

    private fun scheduleSplashScreen() {
        val splashScreenDuration = getSplashDuration()
        Handler().postDelayed({
            val imageView: ImageView = findViewById(R.id.imageView)
            val animRotate: Animation = AnimationUtils.loadAnimation(this.applicationContext, R.anim.rotate)
            val animAntiRotate: Animation = AnimationUtils.loadAnimation(this.applicationContext, R.anim.antirotate)
            val animation3: Animation = AnimationUtils.loadAnimation(this, R.anim.abc_fade_out)
            imageView.startAnimation(animAntiRotate)
            animAntiRotate.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(p0: Animation?) {

                }

                override fun onAnimationEnd(p0: Animation?) {
                    imageView.startAnimation(animAntiRotate)
                }

                override fun onAnimationStart(p0: Animation?) {

                }

            })
            animRotate.setAnimationListener(object : Animation.AnimationListener{
                override fun onAnimationRepeat(p0: Animation?) {
                }

                override fun onAnimationEnd(p0: Animation?) {
                    imageView.startAnimation(animation3)
                    finish()
                }

                override fun onAnimationStart(p0: Animation?) {
                }
            })

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