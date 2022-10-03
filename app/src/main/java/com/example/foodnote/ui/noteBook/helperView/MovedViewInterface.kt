package com.example.foodnote.ui.noteBook.helperView

import android.view.View

interface MovedViewInterface {
    fun addView(view : View)
    fun removeView(view : View)
    fun blockMove(flag : Boolean)
}