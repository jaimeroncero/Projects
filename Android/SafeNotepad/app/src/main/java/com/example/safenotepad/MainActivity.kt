package com.example.safenotepad

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs = getSharedPreferences("my_prefs", MODE_PRIVATE)
        val editor = prefs.edit()
        editor.clear().apply()




        startActivity(Intent(this@MainActivity, LoginActivity::class.java));
    }
}