package com.example.foodnote.ui.base.customView

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.foodnote.R
import com.example.foodnote.ui.base.customView.AnimatorX.ValueAnimatorX

class SplashLoading @JvmOverloads constructor(context : Context, attrs : AttributeSet? = null, style: Int = 0) : View(context,attrs,style) {

    private val paintText = Paint().apply { color = Color.argb(255,150, 150, 150)
        isAntiAlias = true
        textSize = 90f
        typeface = Typeface.createFromAsset(context.assets, "fonts/aquire.otf")
    }

    private val paintLine = Paint().apply { color = Color.argb(255,150, 150, 150)
        strokeWidth = 5f
        isAntiAlias = true
    }

    private val paintText2 = Paint().apply { color = Color.argb(255,140, 140, 140)
        isAntiAlias = true
        textSize = 40f
    }

    private lateinit var canvas : Canvas

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        this.canvas = canvas

        animLine.x2 = width.toFloat()
        animLine.render()
        animText.render()
        animTextLoading.render()

        invalidate()
    }



    private val animText = ValueAnimatorX.ofValue(0f, 9f).apply {
        vectorFunction { 8f }
        render { value -> drawText(value) }
    }
///////////
    private fun drawText(stringAnim : Float) {
        val string = context.getString(R.string.name_splash_app)
        canvas.drawText(string,0,stringAnim.toInt(),0f,height/2f,paintText)
    }



    private val animTextLoading = ValueAnimatorX.ofValue(0f, 11f).apply {
        vectorFunction { x ->
            vector = if (animText.x2 == animText.currentX) { 1 } else { 0 }
            if(x == 11f) { currentX = 8f }
            8f
        }
        render { value -> drawTextLoading(value) }
    }
//////////
    private fun drawTextLoading(stringLoadingAnim : Float) {
        val stringLoad = context.getString(R.string.loading_splash)
        val widthText = paintText2.measureText(stringLoad)

        val bounds = Rect()
        paintText2.getTextBounds(stringLoad, 0, stringLoad.length, bounds)
        val heightText: Int = bounds.height()

        canvas.drawText(stringLoad,0,stringLoadingAnim.toInt(),width - widthText,height/1.6f + heightText.toFloat(),paintText2)
    }



    private val animLine = ValueAnimatorX.ofValue(0f, 360f).apply {
        vectorFunction { 800f }
        render { lineX -> drawLine(lineX) }
    }
///////////
    private fun drawLine(lineX: Float) {
        canvas.drawLine( 0f,height/1.8f,lineX,height/1.8f,paintLine)
    }
}