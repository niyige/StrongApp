package com.oyy.strong.ui

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.oyy.strong.R
import org.jetbrains.anko.startActivity

class WelcomeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        Handler().postDelayed({
            startActivity<MainActivity>()
            finish()
        }, 2000)
    }
}
