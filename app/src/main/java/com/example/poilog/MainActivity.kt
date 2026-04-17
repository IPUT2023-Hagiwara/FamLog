package com.example.poilog

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
// --- ここから下の3行を追加（日付を使うための準備） ---
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textDate = findViewById<TextView>(R.id.textDate)
        val dateFormat = SimpleDateFormat("yyyy/MM/dd(E)",java.util.Locale.JAPAN)
        val today = dateFormat.format(Date())
        textDate.text = today

        // button2
        val buttonNext = findViewById<Button>(R.id.button2)

        buttonNext.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }

        // button3
        val buttonOther = findViewById<Button>(R.id.button3)
        buttonOther.setOnClickListener {
            // 移動先を MainActivi3.xml
            val intent = Intent(this, MainActivity3::class.java)
            startActivity(intent)
        }
    }
}