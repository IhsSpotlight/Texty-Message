package org.freedu.sharedpreferenceb6

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val layout = findViewById<ConstraintLayout>(R.id.splashLayout)
        val colors = intArrayOf(0xFFFF512F.toInt(), 0xFF24C6DC.toInt(), 0xFFF7971E.toInt())

        val gradient = GradientDrawable(GradientDrawable.Orientation.BL_TR, intArrayOf(colors[0], colors[1]))
        layout.background = gradient

        val colorAnimator = ValueAnimator.ofFloat(0f, 1f)
        colorAnimator.duration = 3000
        colorAnimator.repeatCount = ValueAnimator.INFINITE
        colorAnimator.repeatMode = ValueAnimator.REVERSE

        colorAnimator.addUpdateListener { animator ->
            val fraction = animator.animatedFraction
            val evaluator = ArgbEvaluator()
            val start = evaluator.evaluate(fraction, colors[0], colors[1]) as Int
            val end = evaluator.evaluate(fraction, colors[1], colors[2]) as Int
            gradient.colors = intArrayOf(start, end)
        }

        colorAnimator.start()

        layout.postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 4000)
    }
}
