package me.proton.jobforandroid.material_gb_nasaapi.ui

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import me.proton.jobforandroid.material_gb_nasaapi.databinding.ActivitySplashBinding


class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.imageView.animate().rotationBy(550f)
            .setInterpolator(AccelerateDecelerateInterpolator()).setDuration(2750)
            .setListener(object : Animator.AnimatorListener {

                override fun onAnimationEnd(animation: Animator) {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                }

                override fun onAnimationRepeat(animation: Animator) {}
                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationStart(animation: Animator) {}

            })
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
