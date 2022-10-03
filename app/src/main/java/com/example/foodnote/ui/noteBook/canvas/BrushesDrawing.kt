package com.example.foodnote.ui.noteBook.canvas

import android.graphics.Canvas
import android.graphics.Paint
import kotlin.math.round

object BrushesDrawing {

    fun flatBrash(myCanvas : Canvas, xCurrent : Int, yCurrent : Int, xTemp : Int, yTemp : Int, paint : Paint, size :Int) {
        for (i in -size..size) {
            myCanvas.drawLine(xTemp.toFloat() + i, yTemp.toFloat() + i, xCurrent.toFloat() + i, yCurrent.toFloat() + i, paint)
        }
    }

    fun circleBrush(myCanvas : Canvas, xCurrent : Int, xTemp : Int, yTemp : Int, yCurrent : Int, paint : Paint, size :Int) {
        val sizeBrush = size.toFloat()

        val deltaY = ((yCurrent.toDouble() - yTemp.toDouble())/(xCurrent.toDouble() - xTemp.toDouble()))
        if(xCurrent > xTemp) {
            var tempY = round(deltaY * (xTemp - xTemp) + yTemp)

            for (i in xTemp..xCurrent) {
                val y = round(deltaY * (i - xTemp) + yTemp)
                myCanvas.drawCircle(i.toFloat(), y.toFloat(),sizeBrush,paint)

                if (y.toInt() > tempY.toInt() + 2) {
                    for (j in tempY.toInt()..y.toInt()) {
                        myCanvas.drawCircle(i.toFloat(), j.toFloat(),sizeBrush,paint)
                    }
                }
                if (y.toInt() < tempY.toInt() - 2) {
                    for (j in tempY.toInt() downTo y.toInt()) {
                        myCanvas.drawCircle(i.toFloat(), j.toFloat(),sizeBrush,paint)
                    }
                }
                tempY = y
            }
        } else {
            var tempY = round(deltaY * (xTemp - xTemp) + yTemp)

            for (i in xTemp downTo xCurrent) {
                val y = round( deltaY * (i - xTemp) + yTemp)
                myCanvas.drawCircle(i.toFloat(), y.toFloat(), sizeBrush, paint)

                if (y.toInt() > tempY.toInt() + 2) {
                    for (j in tempY.toInt()..y.toInt()) {
                        myCanvas.drawCircle(i.toFloat(), j.toFloat(),sizeBrush,paint)
                    }
                }
                if (y.toInt() < tempY.toInt() - 2) {
                    for (j in tempY.toInt() downTo y.toInt()) {
                        myCanvas.drawCircle(i.toFloat(), j.toFloat(),sizeBrush,paint)
                    }
                }
                tempY = y
            }
        }
        if(xCurrent == xTemp) {
            if (yCurrent > yTemp + 2) {
                for (j in yTemp..yCurrent) {
                    myCanvas.drawCircle(xCurrent.toFloat(), j.toFloat(), sizeBrush, paint)
                }
            }
            if (yCurrent < yTemp - 2) {
                for (j in yTemp downTo yCurrent) {
                    myCanvas.drawCircle(xCurrent.toFloat(), j.toFloat(), sizeBrush, paint)
                }
            }
        }
    }

}