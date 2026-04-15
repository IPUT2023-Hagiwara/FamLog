package com.example.poilog

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. レイアウトからIDが「button2」のボタンを探す
        val buttonNext = findViewById<Button>(R.id.button2)

        // 2. ボタンが押された時の処理を書く
        buttonNext.setOnClickListener {
            // 3. SecondActivityへ移動するための「Intent」を作成
            val intent = Intent(this, MainActivity2::class.java)
            // 4. 画面遷移を実行
            startActivity(intent)
        }
    }
}