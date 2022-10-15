package com.example.deepstylo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_spash.*
import pl.droidsonroids.gif.GifDrawable


class SpashActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spash)


        Handler().postDelayed(Runnable {
            startActivity(Intent(this@SpashActivity , HomeActivity::class.java))
            finish()
        }, 3000)


    }


}