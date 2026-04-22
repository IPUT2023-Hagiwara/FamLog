package com.example.poilog

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class JournalEntry(
    val feeling: String,
    val health: String,
    val journal: String,
    val createdAt: Long
)

data class DailyMoodSummary(
    val dateMillis: Long,
    val dateLabel: String,
    val avgScore: Float,
    val displayJournal: String
)

object UserPrefs {
    private const val PREF_NAME = "poilog_user_prefs"
    private const val KEY_EMAIL = "email"
    private const val KEY_PASSWORD = "password"
    private const val KEY_NICKNAME = "nickname"
    private const val KEY_AGE = "age"
    private const val KEY_OCCUPATION = "occupation"
    private const val KEY_LOGGED_IN = "logged_in"
    private const val KEY_JOURNAL_ENTRIES = "journal_entries"

    fun saveUser(
        context: Context,
        email: String,
        password: String,
        nickname: String,
        age: String,
        occupation: String
    ) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_EMAIL, email)
            .putString(KEY_PASSWORD, password)
            .putString(KEY_NICKNAME, nickname)
            .putString(KEY_AGE, age)
            .putString(KEY_OCCUPATION, occupation)
            .apply()
    }

    fun isRegistered(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return !prefs.getString(KEY_EMAIL, "").isNullOrEmpty() &&
            !prefs.getString(KEY_PASSWORD, "").isNullOrEmpty()
    }

    fun canLogin(context: Context, userId: String, password: String): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val savedEmail = prefs.getString(KEY_EMAIL, "") ?: ""
        val savedPassword = prefs.getString(KEY_PASSWORD, "") ?: ""
        return userId == savedEmail && password == savedPassword
    }

    fun setLoggedIn(context: Context, value: Boolean) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_LOGGED_IN, value)
            .apply()
    }

    fun isLoggedIn(context: Context): Boolean {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_LOGGED_IN, false)
    }

    fun saveJournalEntry(
        context: Context,
        feeling: String,
        health: String,
        journal: String
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val current = prefs.getString(KEY_JOURNAL_ENTRIES, "[]") ?: "[]"
        val currentArray = JSONArray(current)

        val item = JSONObject().apply {
            put("feeling", feeling)
            put("health", health)
            put("journal", journal)
            put("createdAt", System.currentTimeMillis())
        }
        currentArray.put(item)

        // Keep only latest 200 entries.
        val trimmed = JSONArray()
        val startIndex = (currentArray.length() - 200).coerceAtLeast(0)
        for (i in startIndex until currentArray.length()) {
            trimmed.put(currentArray.getJSONObject(i))
        }

        prefs.edit().putString(KEY_JOURNAL_ENTRIES, trimmed.toString()).apply()
    }

    fun getRecentJournalEntries(context: Context, limit: Int = 7): List<JournalEntry> {
        return readEntries(context)
            .sortedBy { it.createdAt }
            .takeLast(limit)
    }

    fun getDailyMoodSummaries(context: Context, days: Int): List<DailyMoodSummary> {
        val allEntries = readEntries(context).sortedBy { it.createdAt }
        val dateFormat = SimpleDateFormat("M/d", Locale.getDefault())
        val summaries = mutableListOf<DailyMoodSummary>()

        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        for (offset in days - 1 downTo 0) {
            val dayStart = (today.clone() as Calendar).apply {
                add(Calendar.DAY_OF_YEAR, -offset)
            }
            val dayEnd = (dayStart.clone() as Calendar).apply {
                add(Calendar.DAY_OF_YEAR, 1)
            }

            val dayEntries = allEntries.filter {
                it.createdAt >= dayStart.timeInMillis && it.createdAt < dayEnd.timeInMillis
            }

            val avgScore = if (dayEntries.isEmpty()) {
                0f
            } else {
                dayEntries.map { scoreFromFeeling(it.feeling) }.average().toFloat()
            }

            val latest = dayEntries.lastOrNull()
            val baseText = latest?.journal?.takeIf { it.isNotBlank() }
                ?: latest?.let { "${it.feeling} / ${it.health}" }
                ?: "記録なし"
            val displayText = if (dayEntries.size >= 2) {
                "(${dayEntries.size}件) $baseText"
            } else {
                baseText
            }

            summaries.add(
                DailyMoodSummary(
                    dateMillis = dayStart.timeInMillis,
                    dateLabel = dateFormat.format(Date(dayStart.timeInMillis)),
                    avgScore = avgScore,
                    displayJournal = displayText
                )
            )
        }
        return summaries
    }

    private fun readEntries(context: Context): List<JournalEntry> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val current = prefs.getString(KEY_JOURNAL_ENTRIES, "[]") ?: "[]"
        val array = JSONArray(current)
        val entries = mutableListOf<JournalEntry>()

        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            entries.add(
                JournalEntry(
                    feeling = obj.optString("feeling"),
                    health = obj.optString("health"),
                    journal = obj.optString("journal"),
                    createdAt = obj.optLong("createdAt")
                )
            )
        }
        return entries
    }

    private fun scoreFromFeeling(feeling: String): Int {
        return when (feeling) {
            "楽しい" -> 3
            "嬉しい" -> 2
            "ふつう" -> 0
            "穏やか" -> 1
            "怒り" -> -3
            "悲しい" -> -2
            "つらい" -> -3
            "不安" -> -2
            else -> 0
        }
    }
}
