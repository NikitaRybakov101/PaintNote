package com.example.foodnote.ui.base.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.foodnote.ui.base.customView.AnimatorX.ValueFunctionX
import com.example.foodnote.ui.base.customView.AnimatorX.ValueAnimatorX
import com.example.foodnote.ui.base.customView.customViewInterfaces.WaterButtonInterface
import kotlin.math.cos
import kotlin.math.sin

class WaterButton @JvmOverloads constructor(context : Context, attrs : AttributeSet? = null, style: Int = 0) : View(context,attrs,style) ,
    WaterButtonInterface {

    private val paint = Paint().apply { color = Color.argb(250,84, 210, 235) }

    private val paintText = Paint().apply { color = Color.argb(255,255, 255, 255)
        isAntiAlias = true
        textSize = 60f
    }

    private var tempY = 0f

    private var stopX = 3.2f
    private var startX = -0.1f

    private var amplitudeScale = 60f/263f

    private var maxWaterValue = 2100
    private var currentWaterValue = 1250

    private var marginText = 20f


    private lateinit var canvas: Canvas

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        this.canvas = canvas

        animWater.render()
        drawText(canvas)

        invalidate()
    }



    private val valueFunctionX = ValueFunctionX()

    private val animWater = ValueAnimatorX.ofValue(0f, 5000f).apply {
        vectorFunction { x -> if (x >= x2) {currentX = x1} ; 2f }
        render { parameter -> drawWater(parameter) }
    }
////////////////
    private fun drawWater(parameter : Float) {
        val maxWaterH = ((width * currentWaterValue) / maxWaterValue).toFloat()

        valueFunctionX.apply {
              parametricFunction { x, a, h, amplitude -> (0.5f * (x - 2f) * cos(x + a) * sin(x + a)) * amplitude + h }
              render { value, x -> drawLineWater(value,x) }

        }.startFunction(startX,stopX,height,parameter,maxWaterH,height*amplitudeScale)
    }
    private fun drawLineWater(value: Float, x: Float) {
        if(x != tempY) { canvas.drawLine(0f, x, value, x, paint) }
        tempY = x
    }




    private fun drawText(canvas: Canvas) {
        val string = "${currentWaterValue}/${maxWaterValue} милл"
        val widthText = paintText.measureText(string)

        canvas.drawText(string, width/2f - widthText/2f,height/2f + marginText, paintText)
    }

    override fun setDataWaterToStart(maxWater : Int, currentWater : Int) {
        this.maxWaterValue = maxWater
        this.currentWaterValue = currentWater
    }
}