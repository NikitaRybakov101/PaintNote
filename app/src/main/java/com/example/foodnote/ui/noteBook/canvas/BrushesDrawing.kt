package com.example.foodnote.ui.noteBook.canvas

import android.graphics.Canvas
import android.graphics.Paint
import android.util.Log
import kotlin.math.PI
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.round
import kotlin.math.sin
import kotlin.math.sqrt

object BrushesDrawing {

    fun flatBrash(myCanvas : Canvas, xCurrent : Int, yCurrent : Int, xTemp : Int, yTemp : Int, paint : Paint, size :Int) {
        for (i in -size..size) {
            myCanvas.drawLine(xTemp.toFloat() + i, yTemp.toFloat() + i, xCurrent.toFloat() + i, yCurrent.toFloat() + i, paint)
        }
    }

    fun circleBrush(myCanvas : Canvas, xCurrent : Int, xTemp : Int, yTemp : Int, yCurrent : Int, paint : Paint, size :Int) {
        val deltaY = yCurrent - yTemp.toDouble()
        val deltaX = xCurrent - xTemp.toDouble()

        val radian = if(deltaX != 0.0) {
            val aTan = - atan((deltaY / deltaX))
            if(deltaX > 0.0) { aTan } else { PI + aTan }
        } else {
            if(deltaY < 0.0) { PI / 2.0 } else { - PI / 2.0 }
        }

        val length = sqrt((deltaY * deltaY + deltaX * deltaX))
        var lengthTemp = 0.0

        while (lengthTemp <= length) {
            val x = lengthTemp * cos(radian + PI) + xCurrent
            val y = - lengthTemp * sin(radian + PI) + yCurrent

            myCanvas.drawCircle(x.toFloat(), y.toFloat(), size.toFloat(), paint)
            lengthTemp += 1
        }
    }

}