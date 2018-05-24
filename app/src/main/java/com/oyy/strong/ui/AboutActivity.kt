package com.oyy.strong.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.oyy.strong.R
import kotlinx.android.synthetic.main.activity_about.*
import org.jetbrains.anko.onClick

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        backText.onClick {
            finish()
        }
    }
}
