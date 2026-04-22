package com.example.poilog

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (UserPrefs.isLoggedIn(this)) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        setContentView(R.layout.activity_login)

        val etUserId = findViewById<EditText>(R.id.etUserId)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val tvForgot = findViewById<TextView>(R.id.tvForgotPassword)
        val tvRegister = findViewById<TextView>(R.id.tvRegister)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        etPassword.inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        tvForgot.setOnClickListener {
            Toast.makeText(this, "パスワード再設定機能は準備中です", Toast.LENGTH_SHORT).show()
        }

        tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterStep1Activity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            val userId = etUserId.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (userId.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "ユーザーIDとパスワードを入力してね", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!UserPrefs.isRegistered(this)) {
                Toast.makeText(this, "先に会員登録をしてください", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!UserPrefs.canLogin(this, userId, password)) {
                Toast.makeText(this, "ユーザーIDまたはパスワードが違います", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            UserPrefs.setLoggedIn(this, true)
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
        }
    }
}
