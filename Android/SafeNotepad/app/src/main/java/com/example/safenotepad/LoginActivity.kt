package com.example.safenotepad

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val biometricLoginButton =
            findViewById<Button>(R.id.login_button)
        biometricLoginButton.setOnClickListener {
            startActivity(Intent(this@LoginActivity, NoteActivity::class.java));
        }
    }
}