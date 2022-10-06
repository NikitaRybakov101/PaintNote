package com.example.foodnote.ui.base.customView

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.foodnote.ui.base.customView.AnimatorX.ValueAnimatorX
import com.example.foodnote.ui.base.customView.customViewInterfaces.DiagramViewInterface
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class CircleDiagramView @JvmOverloads constructor(context : Context, attrs : AttributeSet? = null, style: Int = 0) : View(context,attrs,style) ,
    DiagramViewInterface {

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)

        createRect(width)
        setMeasuredDimension(width, width)
    }

    private val paintCircle = Paint().apply { color = Color.rgb(10, 180, 255)
        isAntiAlias = true
    }

    private val paintCircle3 = Paint().apply { color = Color.rgb(150, 180, 255)
        isAntiAlias = true
    }

    private val paintCircle4 = Paint().apply { color = Color.rgb(255, 180, 255)
        isAntiAlias = true
    }

    private val paintCircleBek = Paint().apply { color = Color.rgb(245, 245, 245)
        isAntiAlias = true
    }

    private val paintCircleBek3 = Paint().apply { color = Color.rgb(245, 245, 245)
        isAntiAlias = true
    }

    private val paintCircleBek4 = Paint().apply { color = Color.rgb(255, 255, 255)
        setShadowLayer(7.0f, 1f, 1f, Color.GRAY)
        isAntiAlias = true
    }

    private val paintText = Paint().apply { color = Color.rgb(185, 185, 185)
        isAntiAlias = true
        textSize = 65f
    }

    private val paintSmallText = Paint().apply { color = Color.rgb(135, 135, 135)
        isAntiAlias = true
        textSize = 29f
    }

    private val paintLine = Paint().apply { color = Color.rgb(255, 255, 255)
        setShadowLayer(3.0f, 1f, 1f, Color.GRAY)
        strokeWidth = 6f
    }

    private var angle = 0f
    private var angle2 = 0f
    private var angle3 = 0f

    private var length = 0f
    private var length2 = 0f
    private var length3 = 0f

    private var maxCalories = 0
    private var maxFats = 0
    private var maxProtein = 0

    private var x1 = 0
    private var x2 = 0
    private var x3 = 0

    private lateinit var rectF : RectF
    private lateinit var rectF2 : RectF
    private lateinit var rectF3 : RectF

    private var widthDiagram = 12f
    private var radiuse = 120f

    private lateinit var canvas : Canvas

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        this.canvas = canvas

        animCirclesOne.x2 = length
        animCirclesTwo.x2 = length2
        animCirclesThree.x2 = length3

        animCirclesOne.render()
        animCirclesTwo.render()
        animCirclesThree.render()

        drawText(canvas)

        invalidate()
    }

    private val animCirclesOne = ValueAnimatorX.ofValue(0f, 360f).apply {
        vectorFunction { x -> 50f * (x2 - x) * (1f/50f) }
        render { angle -> drawCirclesOne(angle) }
    }
/////////////
    private fun drawCirclesOne(angle: Float){
        canvas.drawCircle(width/2f,width/2f,width/2f - radiuse ,paintCircleBek4)

        canvas.drawArc(rectF,0f,angle,true,paintCircle)
        line(angle,canvas,"$x1 ккал")
        canvas.drawCircle(width/2f,width/2f,width/2f - widthDiagram - radiuse,paintCircleBek)
    }




    private val animCirclesTwo = ValueAnimatorX.ofValue(0f, 360f).apply {
        vectorFunction { x -> 50f * (x2 - x) * (1f/50f) }
        render { angle -> drawCirclesTwo(angle) }
    }
/////////////
    private fun drawCirclesTwo(angle2: Float){
        canvas.drawArc(rectF2,0f,angle2,true,paintCircle3)
        line(angle2,canvas,"$x2 жиры")
        canvas.drawCircle(width/2f,width/2f,width/2f - widthDiagram*2 - radiuse,paintCircleBek)
    }




    private val animCirclesThree = ValueAnimatorX.ofValue(0f, 360f).apply {
        vectorFunction { x -> 50f * (x2 - x) * (1f/50f) }
        render { angle -> drawCirclesThree(angle) }
    }
//////////
    private fun drawCirclesThree(angle3: Float){
        canvas.drawArc(rectF3,0f,angle3,true,paintCircle4)
        line(angle3,canvas,"$x3 белок")
        canvas.drawCircle(width/2f,width/2f,width/2f - widthDiagram*3 - radiuse,paintCircleBek3)
    }




    private fun line(angle : Float, canvas : Canvas, text : String) {
        if(angle > 0) {
            val x = width/2f + (width/2f - radiuse/2 - 32) * cos(- (angle * 2 * PI) / 360).toFloat()
            val y = width/2f - (width/2f - radiuse/2 - 32) * sin(- (angle * 2 * PI) / 360).toFloat()

            canvas.drawLine(width/2f,width/2f, x, y, paintLine)
            canvas.drawCircle(x, y,25f,paintLine)

            val x2 = width/2f + (width/2f - radiuse/2 - 32) * cos(- (angle * 2 * PI) / 360).toFloat()
            val y2 = width/2f - (width/2f - radiuse/2 - 32) * sin(- (angle * 2 * PI) / 360).toFloat()

            val widthText = paintSmallText.measureText(text)

            val bounds = Rect()
            paintSmallText.getTextBounds(text, 0, text.length, bounds)
            val heightText: Int = bounds.height()

           if(angle <= 180) {
                canvas.drawText(text,x2 - widthText/2f,y2 + heightText/2f + 44f,paintSmallText)
            } else {
                canvas.drawText(text,x2 - widthText/2f,y2 + heightText/2f - 44f,paintSmallText)
            }
        }
    }

    private fun drawText(canvas: Canvas) {
        val string = "max ${maxCalories}ккал"
        val widthText = paintText.measureText(string)

        paintText.textSize = ((width/2f) - radiuse) / 5f
        canvas.drawText(string,width/2f - widthText/2 ,width/2f ,paintText)
    }

    private fun createRect(width: Int) {
        this.radiuse = width / 6f
        rectF = RectF(0f + radiuse, 0f + radiuse, width.toFloat() - radiuse, width.toFloat() - radiuse)
        rectF2 = RectF(widthDiagram + radiuse,widthDiagram + radiuse,width.toFloat() - widthDiagram - radiuse,width.toFloat() - widthDiagram - radiuse)
        rectF3 = RectF(widthDiagram*2 + radiuse,widthDiagram*2 + radiuse,width.toFloat() - widthDiagram*2 - radiuse,width.toFloat() - widthDiagram*2 - radiuse)
    }

    override fun start(x1 : Int, maxCalories : Int,x2 : Int, maxFats :Int , x3 : Int, maxProtein : Int) {
        animCirclesOne.x1 = 0f
        animCirclesTwo.x1 = 0f
        animCirclesThree.x1 = 0f

        animCirclesOne.currentX = 0f
        animCirclesTwo.currentX = 0f
        animCirclesThree.currentX = 0f

        angle = 0f
        angle2 = 0f
        angle3 = 0f

        this.maxCalories = maxCalories
        this.maxFats = maxFats
        this.maxProtein = maxProtein

        this.x1 = x1
        this.x2 = x2
        this.x3 = x3

        if(maxCalories != 0  && maxFats != 0 && maxProtein != 0) {
            length = (360f * ((x1 * 100) / maxCalories)) / 100f
            length2 = (360f * ((x2 * 100) / maxFats)) / 100f
            length3 = (360f * ((x3 * 100) / maxProtein)) / 100f
        } else {
            length = 0f
            length2 = 0f
            length3 = 0f
        }
    }

}