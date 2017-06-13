package com.sola.github.kotlin.tonight.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.Button
import android.widget.TextView

/**
 * Created by 禄骥
 * 2016/7/15.
 */
class DrawableCenterTextView : TextView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onDraw(canvas: Canvas) {
        val drawables = compoundDrawables
        val drawableLeft = drawables[0]
        val drawableRight = drawables[2]
        if (drawableLeft != null || drawableRight != null) {
            val textWidth = paint.measureText(text.toString())
            val drawablePadding = compoundDrawablePadding
            val drawableWidth: Int
            if (drawableLeft != null) {
                drawableWidth = drawableLeft.intrinsicWidth
            } else {
                drawableWidth = drawableRight.intrinsicWidth
            }
            val bodyWidth = textWidth + drawableWidth.toFloat() + drawablePadding.toFloat()
            setPadding(0, 0, (width - bodyWidth).toInt(), 0)
            canvas.translate((width - bodyWidth) / 2, 0f)
        }
        super.onDraw(canvas)
    }

}
