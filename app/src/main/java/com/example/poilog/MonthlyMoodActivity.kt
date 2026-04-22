package com.example.poilog

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class MonthlyMoodActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monthly_mood)

        findViewById<ImageView>(R.id.ivBack).setOnClickListener {
            finish()
        }

        renderMonthlyData()
    }

    override fun onResume() {
        super.onResume()
        renderMonthlyData()
    }

    private fun renderMonthlyData() {
        val moodFlowView = findViewById<MoodFlowView>(R.id.moodFlowMonthView)
        val dateStrip = findViewById<LinearLayout>(R.id.llMonthDates)
        val monthLogs = findViewById<LinearLayout>(R.id.llMonthLogs)

        val summaries = UserPrefs.getDailyMoodSummaries(this, 30)
        moodFlowView.setMoodScores(summaries.map { it.avgScore })

        dateStrip.removeAllViews()
        summaries.forEach { summary ->
            val tv = TextView(this).apply {
                text = summary.dateLabel
                textSize = 10f
                setPadding(16, 10, 16, 10)
                setTextColor(android.graphics.Color.parseColor("#4A5260"))
                setBackgroundResource(R.drawable.bg_date_circle)
            }
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            lp.marginEnd = 8
            dateStrip.addView(tv, lp)
        }

        monthLogs.removeAllViews()
        summaries.reversed().forEach { summary ->
            val tv = TextView(this).apply {
                text = "${summary.dateLabel}  ${summary.displayJournal}"
                textSize = 15f
                setPadding(20, 16, 20, 16)
                setTextColor(android.graphics.Color.parseColor("#444A5A"))
                setBackgroundResource(R.drawable.bg_input_box)
            }
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            lp.bottomMargin = 10
            monthLogs.addView(tv, lp)
        }
    }
}
