package ru.elnorte.tfs_spring_2024_reshetnikov.ui.topic

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.elnorte.tfs_spring_2024_reshetnikov.asDateString
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.MessageUiModel

class CustomItemDecoration : RecyclerView.ItemDecoration() {

    private var list = listOf<MessageUiModel>()
    fun updateList(input: List<MessageUiModel>) {
        list = input
    }

    private val paintDate: Paint = Paint().apply {
        color = Color.WHITE
        textSize = 50f
        textAlign = Paint.Align.CENTER
    }

    private val dateBounds = Rect()

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        onDrawOver(c, parent, state)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        for (i in 0 until parent.childCount) {
            val view = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(view!!)
            when {
                position == RecyclerView.NO_POSITION || position >= list.size -> {
                    continue
                }

                position == 0 -> {
                    drawDateHeader(c, view, parent)
                }

                position > 0 && list[position].timestamp.asDateString("dd MMM") != list[position - 1].timestamp.asDateString(
                    "dd MMM"
                ) -> {
                    drawDateHeader(c, view, parent)
                }
            }
        }
    }

    private fun drawDateHeader(canvas: Canvas, view: View, parent: RecyclerView) {
        val position = (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
        val dateToShow = list[position].timestamp.asDateString("dd MMM")
        paintDate.getTextBounds(dateToShow, 0, dateToShow.length, dateBounds)
        canvas.drawText(
            dateToShow,
            ((parent.width - dateToShow.length) / 2).toFloat(),
            view.top.toFloat() - dateBounds.height() + 20,
            paintDate
        )
    }
}
