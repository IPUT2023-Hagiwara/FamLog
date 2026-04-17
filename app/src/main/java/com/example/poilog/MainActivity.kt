package com.example.poilog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textDate = findViewById<TextView>(R.id.textDate)
        val dateFormat = SimpleDateFormat("yyyy/MM/dd(E)", Locale.JAPAN)
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
            val intent = Intent(this, MainActivity3::class.java)
            startActivity(intent)
        }

        // BottomNavigation
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.selectedItemId = R.id.nav_home

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    true
                }

                R.id.nav_graph -> {
                    startActivity(Intent(this, GraphActivity::class.java))
                    true
                }

                R.id.nav_calendar -> {
                    startActivity(Intent(this, CalendarActivity::class.java))
                    true
                }

                R.id.nav_sleep -> {
                    startActivity(Intent(this, SleepActivity::class.java))
                    true
                }

                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }

                else -> false
            }
        }
    }
}