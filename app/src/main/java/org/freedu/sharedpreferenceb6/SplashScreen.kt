package org.freedu.sharedpreferenceb6

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)

      android.os.Handler().postDelayed({
          startActivity(Intent(this, MainActivity::class.java))
          finish()
      }, 2000)
    }
}