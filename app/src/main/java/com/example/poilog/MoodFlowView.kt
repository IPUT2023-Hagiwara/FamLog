package com.example.poilog

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import kotlin.math.max
import kotlin.math.min

class MoodFlowView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private var moodScores: List<Float> = listOf(0f, 0f, 0f, 0f, 0f, 0f, 0f)

    private val baselinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#DCE3F3")
        strokeWidth = 3.2f
        style = Paint.Style.STROKE
    }

    private val positivePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val negativePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#EAEFF9")
        strokeWidth = 2f
        style = Paint.Style.STROKE
        pathEffect = DashPathEffect(floatArrayOf(7f, 9f), 0f)
    }

    private val stemPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#99AFCB")
        strokeWidth = 2f
        style = Paint.Style.STROKE
    }

    private val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }

    private val dotStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#E5ECFA")
        strokeWidth = 2f
        style = Paint.Style.STROKE
    }

    fun setMoodScores(scores: List<Float>) {
        moodScores = if (scores.isEmpty()) listOf(0f, 0f, 0f, 0f, 0f, 0f, 0f) else scores
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val widthF = width.toFloat()
        val heightF = height.toFloat()
        if (widthF <= 0f || heightF <= 0f) return

        val left = 0f
        val right = widthF
        val top = heightF * 0.10f
        val bottom = heightF * 0.92f
        val baselineY = heightF * 0.50f
        val topAmp = heightF * 0.36f
        val bottomAmp = heightF * 0.33f
        val maxScore = max(1f, moodScores.maxOf { kotlin.math.abs(it) })

        val count = moodScores.size
        val stepX = if (count > 1) (right - left) / (count - 1) else right - left

        val anchorX = FloatArray(count)
        val anchorY = FloatArray(count)
        for (i in 0 until count) {
            val score = moodScores[i]
            anchorX[i] = left + i * stepX
            anchorY[i] = if (score >= 0f) {
                baselineY - (score / maxScore) * topAmp
            } else {
                baselineY + ((-score) / maxScore) * bottomAmp
            }
        }

        for (i in 0 until count) {
            val x = anchorX[i]
            canvas.drawLine(x, top, x, bottom, gridPaint)
        }

        val sampleCountPerSegment = 28
        val sampleX = mutableListOf<Float>()
        val sampleY = mutableListOf<Float>()

        if (count == 1) {
            sampleX.add(anchorX[0])
            sampleY.add(anchorY[0])
        } else {
            for (seg in 0 until count - 1) {
                val p0 = if (seg == 0) anchorY[seg] else anchorY[seg - 1]
                val p1 = anchorY[seg]
                val p2 = anchorY[seg + 1]
                val p3 = if (seg + 2 < count) anchorY[seg + 2] else anchorY[seg + 1]
                val x1 = anchorX[seg]
                val x2 = anchorX[seg + 1]

                for (j in 0 until sampleCountPerSegment) {
                    val t = j / sampleCountPerSegment.toFloat()
                    val tt = t * t
                    val ttt = tt * t
                    val y = 0.5f * (
                        (2f * p1) +
                            (-p0 + p2) * t +
                            (2f * p0 - 5f * p1 + 4f * p2 - p3) * tt +
                            (-p0 + 3f * p1 - 3f * p2 + p3) * ttt
                        )
                    val x = x1 + (x2 - x1) * t
                    sampleX.add(x)
                    sampleY.add(y)
                }
            }
            sampleX.add(anchorX.last())
            sampleY.add(anchorY.last())
        }

        val positivePath = Path().apply {
            moveTo(sampleX.firstOrNull() ?: 0f, baselineY)
        }
        val negativePath = Path().apply {
            moveTo(sampleX.firstOrNull() ?: 0f, baselineY)
        }

        for (i in sampleX.indices) {
            val x = sampleX[i]
            val y = sampleY[i]
            positivePath.lineTo(x, min(y, baselineY))
            negativePath.lineTo(x, max(y, baselineY))
        }

        positivePath.lineTo(sampleX.lastOrNull() ?: widthF, baselineY)
        positivePath.close()
        negativePath.lineTo(sampleX.lastOrNull() ?: widthF, baselineY)
        negativePath.close()

        // Upper area: vivid orange -> warm yellow
        positivePaint.shader = LinearGradient(
            0f, 0f, 0f, baselineY,
            intArrayOf(
                Color.parseColor("#FF5A1A"),
                Color.parseColor("#FF9A2A"),
                Color.parseColor("#FFCF45")
            ),
            floatArrayOf(0f, 0.52f, 1f),
            Shader.TileMode.CLAMP
        )

        // Lower area: light cyan -> deep blue
        negativePaint.shader = LinearGradient(
            0f, baselineY, 0f, heightF,
            intArrayOf(
                Color.parseColor("#A8DCF2"),
                Color.parseColor("#73B6EA"),
                Color.parseColor("#4A8BE3")
            ),
            floatArrayOf(0f, 0.50f, 1f),
            Shader.TileMode.CLAMP
        )

        canvas.drawPath(positivePath, positivePaint)
        canvas.drawPath(negativePath, negativePaint)
        canvas.drawLine(0f, baselineY, widthF, baselineY, baselinePaint)

        // Draw stems and white dots like the reference
        val dotRadius = heightF * 0.022f
        for (i in anchorX.indices) {
            val x = anchorX[i]
            val y = anchorY[i]
            canvas.drawLine(x, baselineY, x, y, stemPaint)
            canvas.drawCircle(x, y, dotRadius, dotPaint)
            canvas.drawCircle(x, y, dotRadius, dotStrokePaint)
        }
    }
}
