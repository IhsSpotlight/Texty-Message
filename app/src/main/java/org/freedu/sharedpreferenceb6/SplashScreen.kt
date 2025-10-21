package org.freedu.sharedpreferenceb6

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val layout = findViewById<ConstraintLayout>(R.id.splashLayout)

// Make sure the background is actually set
        val background = layout.background
        if (background is AnimationDrawable) {
            val animationDrawable = background
            animationDrawable.setEnterFadeDuration(800)
            animationDrawable.setExitFadeDuration(800)
            animationDrawable.start()
        } else {
            // For debugging
            android.util.Log.e("SplashActivity", "Background is not AnimationDrawable")
        }

        layout.postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 3000) // 3 seconds
    }
}
