package ru.elnorte.tfs_spring_2024_reshetnikov.ui.topic

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.text.TextPaint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import ru.elnorte.tfs_spring_2024_reshetnikov.R

class EmojiView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0,
    defTheme: Int = 0,
) : View(context, attributeSet, defStyle, defTheme) {

    var emoji: String = ""
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }
    var count: Int = 0
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }
    private val textToDraw
        get() = "$emoji $count"

    private var textPaint = TextPaint().apply {
        color = context.getColor(R.color.onContainer)
        textSize = 18f.sp(context)
    }

    private val textRect = Rect()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        textPaint.getTextBounds(textToDraw, 0, textToDraw.length, textRect)
        val actualWidth =
            resolveSize(textRect.width() + paddingStart + paddingEnd, widthMeasureSpec)
        val actualHeight =
            resolveSize(textRect.height() + paddingTop + paddingBottom, heightMeasureSpec)
        setMeasuredDimension(actualWidth, actualHeight)
    }

    override fun onDraw(canvas: Canvas) {
        val topOffset = height / 2 - textRect.exactCenterY()
        canvas.drawText(textToDraw, paddingStart.toFloat(), topOffset, textPaint)
    }

    private fun Float.sp(context: Context) = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP, this, context.resources.displayMetrics
    )
}
