package com.example.foodnote.ui.base.customView

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.foodnote.ui.base.customView.customViewInterfaces.ColorPicInterface
import com.example.foodnote.ui.noteBook.canvas.CanvasPaintFragment

class ColorPic @JvmOverloads constructor(context : Context, attrs : AttributeSet? = null, style: Int = 0) : View(context,attrs,style) , View.OnTouchListener ,
    ColorPicInterface {

    init {
        this.setOnTouchListener(this)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = MeasureSpec.getSize(widthMeasureSpec)/scale
        val height = MeasureSpec.getSize(heightMeasureSpec)/scale

        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        myCanvas = Canvas(bitmap)
    }

    private lateinit var canvasPaintFragment: CanvasPaintFragment

    private val hsvArray = FloatArray(3)

    private val paint = Paint()
    private val paintCircles = Paint().apply {
        color = Color.argb(200,255,255,255)
    }

    private var targetX = 0f
    private var targetY = 0f

    private val scale = 10
    private var colorH = 360f

    private val radiusCircles = 8f

    private lateinit var bitmap : Bitmap
    private lateinit var myCanvas : Canvas

    private var flag = true

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        createBitmap()

        canvas.drawBitmap(bitmap, null, Rect(0, 0, width, height), paint)
        canvas.drawCircle(targetX, targetY, radiusCircles,paintCircles)
    }

    private fun createBitmap() {
        if(flag) {
            val h = colorH
            for (j in 0..height/scale){
                val v = 1 - (j/(height/scale).toFloat())

                for (i in 0..width/scale) {
                    val s = (i/(width/scale).toFloat())
                    hsvArray[0] = h
                    hsvArray[1] = s
                    hsvArray[2] = v

                    paint.color = Color.HSVToColor(hsvArray)
                    myCanvas.drawPoint(i.toFloat(),j.toFloat(),paint)
                }
            }
            flag = false
        }
    }

    override fun setColorH(colorH : Int) {
        this.colorH = colorH.toFloat()
        flag = true
        invalidate()
    }

    override fun onTouch(p0: View?, event: MotionEvent?): Boolean {
        if (event != null) {
            when (event.action) {
                0 -> {
                    setTarget(event)
                    loadColor(event)
                }
                2 -> {
                    setTarget(event)
                    loadColor(event)
                }
                1 -> {}
            }
        }
        return true
    }

    private fun loadColor(event: MotionEvent) {
        val hsv = FloatArray(3)

        hsv[0] = colorH
        hsv[1] = (( ((width/scale) * event.x)/width )/(width/scale).toFloat())
        hsv[2] = 1 - (( ((height/scale) * event.y)/height )/(height/scale).toFloat())

        val color = Color.HSVToColor(hsv)
        canvasPaintFragment.setColorPic(color)
    }

    private fun setTarget(event: MotionEvent) {
        targetX = event.x
        targetY = event.y
        invalidate()
    }

    override fun setFragment(fragment: CanvasPaintFragment) {
        canvasPaintFragment = fragment
    }
}