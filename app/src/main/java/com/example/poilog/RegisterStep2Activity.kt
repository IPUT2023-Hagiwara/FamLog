package com.example.poilog

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class RegisterStep2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_step2)

        val tvBackToLogin = findViewById<TextView>(R.id.tvBackToLogin)
        val etNickname = findViewById<EditText>(R.id.etNickname)
        val etAge = findViewById<EditText>(R.id.etAge)
        val etOccupation = findViewById<EditText>(R.id.etOccupation)
        val btnComplete = findViewById<Button>(R.id.btnRegisterComplete)

        val email = intent.getStringExtra("register_email").orEmpty()
        val password = intent.getStringExtra("register_password").orEmpty()

        tvBackToLogin.setOnClickListener { finish() }

        btnComplete.setOnClickListener {
            val nickname = etNickname.text.toString().trim()
            val age = etAge.text.toString().trim()
            val occupation = etOccupation.text.toString().trim()

            if (nickname.isEmpty() || age.isEmpty() || occupation.isEmpty()) {
                Toast.makeText(this, "すべて入力してね", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "登録情報が不足しています。最初からやり直してね", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            UserPrefs.saveUser(
                context = this,
                email = email,
                password = password,
                nickname = nickname,
                age = age,
                occupation = occupation
            )

            Toast.makeText(this, "登録しました！ログインしてください", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, LoginActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
            startActivity(intent)
            finish()
        }
    }
}
