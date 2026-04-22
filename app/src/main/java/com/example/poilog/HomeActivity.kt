package com.example.poilog

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val ivProfile = findViewById<ImageView>(R.id.ivProfile)
        val ivMenu = findViewById<ImageView>(R.id.ivMenu)
        val tvSeeAll = findViewById<TextView>(R.id.tvSeeAll)
        val btnGoInput = findViewById<ImageButton>(R.id.btnGoInput)

        ivProfile.setOnClickListener {
            Toast.makeText(this, "プロフィール機能は準備中です", Toast.LENGTH_SHORT).show()
        }

        ivMenu.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("メニュー")
                .setMessage("ログアウトしますか？")
                .setPositiveButton("ログアウト") { _, _ ->
                    UserPrefs.setLoggedIn(this, false)
                    val intent = Intent(this, LoginActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    }
                    startActivity(intent)
                }
                .setNegativeButton("キャンセル", null)
                .show()
        }

        tvSeeAll.setOnClickListener {
            val intent = Intent(this, MonthlyMoodActivity::class.java)
            startActivity(intent)
        }

        btnGoInput.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        renderHomeData()
    }

    override fun onResume() {
        super.onResume()
        renderHomeData()
    }

    private fun renderHomeData() {
        val moodFlowView = findViewById<MoodFlowView>(R.id.moodFlowView)
        val dateViews = listOf(
            findViewById<TextView>(R.id.tvDate1),
            findViewById<TextView>(R.id.tvDate2),
            findViewById<TextView>(R.id.tvDate3),
            findViewById<TextView>(R.id.tvDate4),
            findViewById<TextView>(R.id.tvDate5),
            findViewById<TextView>(R.id.tvDate6),
            findViewById<TextView>(R.id.tvDate7)
        )
        val logViews = listOf(
            findViewById<TextView>(R.id.tvLog1),
            findViewById<TextView>(R.id.tvLog2),
            findViewById<TextView>(R.id.tvLog3),
            findViewById<TextView>(R.id.tvLog4),
            findViewById<TextView>(R.id.tvLog5),
            findViewById<TextView>(R.id.tvLog6),
            findViewById<TextView>(R.id.tvLog7)
        )

        val summaries = UserPrefs.getDailyMoodSummaries(this, 7)
        val scores = mutableListOf<Float>()

        for (i in summaries.indices) {
            val summary = summaries[i]
            dateViews[i].text = summary.dateLabel
            logViews[i].text = "${summary.dateLabel}  ${summary.displayJournal}"
            scores.add(summary.avgScore)
        }

        moodFlowView.setMoodScores(scores)
    }
}
