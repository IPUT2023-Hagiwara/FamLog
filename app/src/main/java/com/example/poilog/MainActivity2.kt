package com.example.poilog

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        //Select Feeling
        val feelingButtons = listOf(
            findViewById<Button>(R.id.button8),
            findViewById<Button>(R.id.button5),
            findViewById<Button>(R.id.button10),
            findViewById<Button>(R.id.button11),
            findViewById<Button>(R.id.button12),
            findViewById<Button>(R.id.button13),
            findViewById<Button>(R.id.button14),
            findViewById<Button>(R.id.button15)
        )

        feelingButtons.forEach { selectedButton ->
            selectedButton.setOnClickListener {
                feelingButtons.forEach { it.setBackgroundColor(Color.LTGRAY) }

                selectedButton.setBackgroundColor(Color.CYAN)
            }
        }

        //Health
        val healthButtons = listOf(
            findViewById<Button>(R.id.button), // XMLのIDを確認してね！
            findViewById<Button>(R.id.button4),
            findViewById<Button>(R.id.button6),
            findViewById<Button>(R.id.button7),
            findViewById<Button>(R.id.button16),
            findViewById<Button>(R.id.button17),
            findViewById<Button>(R.id.button18),
            findViewById<Button>(R.id.button19)
        )

        healthButtons.forEach { selectedButton ->
            selectedButton.setOnClickListener {
                // healthのリスト内だけでリセットがかかる
                healthButtons.forEach { it.setBackgroundColor(Color.LTGRAY) }
                selectedButton.setBackgroundColor(Color.GREEN) // Healthは緑にするなど色を変えてもOK
            }
        }
        val buttonNext = findViewById<Button>(R.id.button21)

        buttonNext.setOnClickListener {
            val intent = Intent(this, MainActivity4::class.java)
            startActivity(intent)
        }
        val buttonOther = findViewById<Button>(R.id.button20)
        buttonOther.setOnClickListener {
            // 移動先を MainActivi3.xml
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}