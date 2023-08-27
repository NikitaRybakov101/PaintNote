package com.example.foodnote.ui.noteBook.canvas.viewModel

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import com.example.foodnote.ui.base.customView.ColorPic
import com.example.foodnote.ui.noteBook.canvas.CanvasPaint
import com.example.foodnote.ui.noteBook.editorNote.EditorPaintNoteFragment
import com.google.android.material.card.MaterialCardView

class CanvasPaintFragmentViewModel : ViewModel() {

    @SuppressLint("StaticFieldLeak")
    var viewCanvasPaint : CanvasPaint? = null

    var canvasHeight: Int? = null
    var canvasWidth: Int? = null
    var fragmentEditor: EditorPaintNoteFragment? = null
    var colorCardBackground: Int? = null

    fun getCanvas(context: Context,colorCardBackground: Int, viewPic: MaterialCardView): CanvasPaint? {

        if(viewCanvasPaint == null) {
            viewCanvasPaint = CanvasPaint(context, colorCardBackground, viewPic)
            return viewCanvasPaint
        }
        return viewCanvasPaint
    }
}