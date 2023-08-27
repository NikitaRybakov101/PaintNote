package com.example.foodnote.ui.base.customView

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.foodnote.R
import com.example.foodnote.ui.base.customView.AnimatorX.ValueAnimatorX
import kotlinx.coroutines.Job

class SplashLoading @JvmOverloads constructor(context : Context, attrs : AttributeSet? = null, style: Int = 0) : View(context,attrs,style) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val height = MeasureSpec.getSize(heightMeasureSpec)

        val string = context.getString(R.string.name_splash_app)
        val bounds = Rect()
        paintText.getTextBounds(string, 0, string.length, bounds)
        val widthText: Int = bounds.width()

        setMeasuredDimension(widthText, height)
    }

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
        textSize = 35f
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
        vectorFunction { 20f }
        render { value -> drawText(value) }
    }
///////////
    private fun drawText(stringAnim : Float) {
        val string = context.getString(R.string.name_splash_app)
        canvas.drawText(string,0,stringAnim.toInt(),0f,height/2f,paintText)
    }



    private val animTextLoading = ValueAnimatorX.ofValue(0f, 11f).apply {
        vectorFunction {
            vector = if (animText.x2 == animText.currentX) { 1 } else { 0 }
            20f
        }
        render { value -> drawTextLoading(value) }
    }
//////////
    private fun drawTextLoading(stringLoadingAnim : Float) {
        val stringLoad = ""
        animTextLoading.x2 = (stringLoad.length).toFloat()

        val widthText = paintText2.measureText(stringLoad)

        val bounds = Rect()
        paintText2.getTextBounds(stringLoad, 0, stringLoad.length, bounds)
        val heightText: Int = bounds.height()

        canvas.drawText(stringLoad,0,stringLoadingAnim.toInt(),width - widthText,height/1.6f + heightText.toFloat(),paintText2)

        if (animTextLoading.currentX == animTextLoading.x2) {
      //      callback()
        }
    }

    private lateinit var callback: () -> Job

    fun setCallBack(function: () -> Job) {
        this.callback = function
    }

    private val animLine = ValueAnimatorX.ofValue(0f, 360f).apply {
        vectorFunction { 1600f }
        render { lineX -> drawLine(lineX) }
    }
///////////
    private fun drawLine(lineX: Float) {
       // canvas.drawLine( 0f,height/1.8f,lineX,height/1.8f,paintLine)
    }
}