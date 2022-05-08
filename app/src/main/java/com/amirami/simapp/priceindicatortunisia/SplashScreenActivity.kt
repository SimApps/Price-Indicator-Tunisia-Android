package com.amirami.simapp.priceindicatortunisia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
        finish()
    }
}