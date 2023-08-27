package com.example.foodnote.ui.noteBook.interfaces

import android.graphics.Bitmap

interface EditorPaintNoteFragmentInterface {
    fun loadImage(_bitmap: Bitmap?)
    fun getImageBitmap(): Bitmap?
}