package ru.elnorte.tfs_spring_2024_reshetnikov

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop

class FlexBoxLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0,
    defTheme: Int = 0,
) : ViewGroup(context, attributeSet, defStyle, defTheme) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        for (child in 0..<childCount) {
            measureChildWithMargins(getChildAt(child), widthMeasureSpec, 0, heightMeasureSpec, 0)
        }

        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)

        var currentLineWidth = 0
        var currentLineHeight = 0
        var totalWidth = 0
        var totalHeight = 0

        var width: Int
        var height: Int

        for (child in 0..<childCount) {
            val element = getChildAt(child)
            width = element.measuredWidth + element.marginLeft + element.marginRight
            height = element.measuredHeight + element.marginBottom + element.marginTop
            if (currentLineWidth + width <= parentWidth - paddingLeft - paddingRight) {
                totalWidth = maxOf(totalWidth, currentLineWidth + width)
                if (currentLineHeight <= height) {
                    totalHeight = totalHeight - currentLineHeight + height
                    currentLineHeight = height
                }
                currentLineWidth += width
            } else {
                currentLineWidth = width
                currentLineHeight = height
                totalHeight += height
            }

        }
        setMeasuredDimension(
            resolveSize(parentWidth, totalWidth),
            totalHeight + paddingTop + paddingBottom
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        var currentLineTopOffset = 0
        var currentItemLeftOffset = 0
        var currentLineMaxHeight = 0
        var width: Int
        var height: Int
        for (child in 0..<childCount) {
            val element = getChildAt(child)

            width = element.measuredWidth
            height = element.measuredHeight

            val fullWidth = width + element.marginLeft + element.marginRight
            val fullHeight = height + element.marginTop + element.marginBottom

            if (currentItemLeftOffset + fullWidth > right - left - paddingRight - paddingLeft) {
                currentLineTopOffset += currentLineMaxHeight
                currentItemLeftOffset = 0
            }

            getChildAt(child).layout(
                paddingLeft + currentItemLeftOffset + element.marginLeft,
                currentLineTopOffset + paddingTop + element.marginTop,
                paddingLeft + currentItemLeftOffset + element.marginLeft + width,
                currentLineTopOffset + paddingTop + element.marginTop + height
            )
            currentLineMaxHeight = maxOf(currentLineMaxHeight, fullHeight)
            currentItemLeftOffset += fullWidth
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }
}
