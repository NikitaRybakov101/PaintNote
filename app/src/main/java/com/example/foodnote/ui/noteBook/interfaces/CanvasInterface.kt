package com.example.foodnote.ui.noteBook.interfaces

import android.graphics.Bitmap
import com.example.foodnote.ui.noteBook.constNote.ConstType

interface CanvasInterface {
    fun setColor(color: Int)
    fun setAlphaColor(alpha: Int)
    fun setColorBackground(color: Int)
    fun setSize(size: Float)
    fun clearCanvas()
    fun getBitmap() : Bitmap
    fun setBrush(brush : ConstType)
    fun setPic(flagPic : Boolean)
}