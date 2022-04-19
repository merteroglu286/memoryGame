package com.example.hafizaoyunu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .add(R.id.main_container, BaslangicEkrani())
            .commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}