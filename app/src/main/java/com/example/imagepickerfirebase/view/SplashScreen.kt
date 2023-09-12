package com.example.imagepickerfirebase.view

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnticipateInterpolator
import android.window.SplashScreenView
import androidx.core.animation.doOnEnd
import com.example.imagepickerfirebase.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed(Runnable {
            checkUsers()
        }, 3000)
    }

    private fun checkUsers() {
        val currentUser = Firebase.auth.currentUser
        if (currentUser != null) {
            val intent = Intent(baseContext, HomeActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(baseContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}