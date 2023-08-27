package com.example.foodnote.ui.noteBook.canvas

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.foodnote.ui.noteBook.constNote.Const.DEFAULT_ALPHA
import com.example.foodnote.ui.noteBook.constNote.Const.DEFAULT_SIZE_BRUSH
import com.example.foodnote.ui.noteBook.constNote.ConstType
import com.example.foodnote.ui.noteBook.interfaces.CanvasInterface
import com.google.android.material.card.MaterialCardView
import java.util.Stack

@SuppressLint("ViewConstructor", "ClickableViewAccessibility")
class CanvasPaint(context: Context, private val colorCardBackground: Int, private val picView: MaterialCardView) : View(context) , View.OnTouchListener , CanvasInterface {

    init {
        this.setOnTouchListener(this)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)

        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            myCanvas = Canvas(bitmap!!)

            rect = Rect(0, 0, width, height)
            myCanvas.drawColor(colorCardBackground)
        }
    }

    private var paintX = 0
    private var paintY = 0

    private var xTemp = 0
    private var yTemp = 0

    private var flagT = true
    private var flagPic = false

    private val paint = Paint().apply { isAntiAlias = true
        strokeWidth = 2f
    }

    private val paintBitmap = Paint().apply { isAntiAlias = true
        strokeWidth = 2f
    }

    private var bitmap : Bitmap? = null
    private lateinit var myCanvas : Canvas
    private lateinit var rect: Rect

    private var color = Color.BLACK
    private var alpha = DEFAULT_ALPHA
    private var size = DEFAULT_SIZE_BRUSH

    private var brush : ConstType = ConstType.BRUSH_PEN

    private data class DrawBrush(var listPath: ArrayList<DrawPath>, var sizeS: Int, var colorS: Int, var alphaS: Int,var brushS: ConstType)
    private data class DrawPath(var xCurrentS: Int, var yCurrentS: Int, var xTempS: Int, var yTempS: Int)
    private val stackDraw = ArrayList<DrawBrush>()
    private var toDrawPosition = -1

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawBitmap(bitmap!!, null, rect, paintBitmap)
    }

    private fun setPixel(xCurrent : Int, yCurrent : Int) {
        if(flagT) {
            xTemp = xCurrent
            yTemp = yCurrent

            flagT = false
        }

        stackDraw.last().apply {
            listPath.add(DrawPath(xCurrent,yCurrent,xTemp,yTemp))
            sizeS = size; colorS = color
            alphaS = alpha; brushS = brush
        }

        setDraw(xCurrent, yCurrent, xTemp, yTemp, size, color, alpha, brush)

        paint.color = Color.argb(255,0,0,0)
        xTemp = xCurrent
        yTemp = yCurrent
    }

    private fun setDraw(xCurrent: Int,yCurrent: Int,xTemp: Int,yTemp: Int,size: Int,color: Int, alpha: Int,brush: ConstType) {
        paint.color = color
        paint.alpha = alpha

        when(brush) {
            ConstType.BRUSH_CIRCLES -> { BrushesDrawing.circleBrush(myCanvas, xCurrent, xTemp, yTemp, yCurrent, paint, size) }
            ConstType.BRUSH_PEN -> { BrushesDrawing.flatBrash(myCanvas, xCurrent, yCurrent,xTemp, yTemp, paint, size) }
            else -> {}
        }
    }

    private fun getColorPic(xCurrent : Int, yCurrent : Int) {
        if(Build.VERSION.SDK_INT >= 32) {

            val color = bitmap!!.getColor(xCurrent, yCurrent).toArgb()
            picView.setBackgroundColor(color)
            this.color = color
        }
    }

    override fun clearCanvas() {
        myCanvas.drawColor(colorCardBackground)
        stackDraw.clear()
        toDrawPosition = -1
    }

    override fun backDraw() {
        myCanvas.drawColor(colorCardBackground)

        if(toDrawPosition > -1) {
            toDrawPosition--
        }
        drawBrush()
    }

    override fun upDraw() {
        myCanvas.drawColor(colorCardBackground)

        if (toDrawPosition + 1 < stackDraw.size) {
            toDrawPosition++
        }
        drawBrush()
    }

    private fun drawBrush() {
        for (i in 0..toDrawPosition) {
            val drawBrush = stackDraw[i]

            drawBrush.listPath.forEach { path ->
                setDraw(path.xCurrentS, path.yCurrentS, path.xTempS, path.yTempS, drawBrush.sizeS, drawBrush.colorS, drawBrush.alphaS, drawBrush.brushS)
            }
        }
        invalidate()
    }

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        if (event != null) {
            when (event.action) {
                0 -> {
                    flagT = true
                    if(flagPic) { getColorPic(paintX,paintY) }
                }
                2 -> {
                    addStackDrawBrash()
                    paint(event)
                    invalidate()
                }
                1 -> {
                    flagPic = false
                    flagStop = true
                }
            }
        }
        return true
    }

    private var flagStop = true

    private fun addStackDrawBrash() {
        if(flagStop) {
            while (stackDraw.size - 1 > toDrawPosition) {
                stackDraw.removeAt(stackDraw.lastIndex)
            }

            stackDraw.add(DrawBrush(ArrayList(), 0, 0, 0, ConstType.BRUSH_CIRCLES))
            toDrawPosition = stackDraw.size - 1

            flagStop = false
        }
    }

    private fun paint(event: MotionEvent) {
        paintX = event.x.toInt()
        paintY = event.y.toInt()

        if(flagPic) {
            getColorPic(paintX,paintY)
        } else {
            setPixel(paintX,paintY)
        }
    }

    override fun setColor(color: Int) { this.color = color }

    override fun setAlphaColor(alpha: Int) { this.alpha = alpha }

    override fun setColorBackground(color: Int) { this.color = color }

    override fun setSize(size: Float) { this.size = size.toInt() }

    override fun setBrush(brush : ConstType) { this.brush = brush }

    override fun setPic(flagPic: Boolean) { this.flagPic = flagPic }

    override fun getBitmap(): Bitmap? = bitmap
}