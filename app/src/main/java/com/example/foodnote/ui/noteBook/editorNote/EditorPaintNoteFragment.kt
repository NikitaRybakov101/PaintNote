package com.example.foodnote.ui.noteBook.editorNote

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import com.example.foodnote.R
import com.example.foodnote.databinding.PaintNoteEditorBinding
import com.example.foodnote.ui.baseViewBinding.BaseViewBindingFragment
import com.example.foodnote.ui.noteBook.mainFragmenNoteBook.ConstructorFragment
import com.example.foodnote.ui.noteBook.canvas.CanvasPaintFragment
import com.example.foodnote.ui.noteBook.constNote.Const
import com.example.foodnote.ui.noteBook.interfaces.EditorPaintNoteFragmentInterface
import com.example.foodnote.utils.hide
import com.example.foodnote.utils.show
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.random.Random

class EditorPaintNoteFragment : BaseViewBindingFragment<PaintNoteEditorBinding>(PaintNoteEditorBinding::inflate) , EditorPaintNoteFragmentInterface {

    private lateinit var fragment: ConstructorFragment
    private var bitmap: Bitmap? = null

    private var canvasFragment: CanvasPaintFragment? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkButton()
    }

    private fun checkButton() {
        binding.textToOpenCanvas.show()
        binding.canvas.setOnClickListener {

            val height = fragment.getHeight()
            val width = fragment.getWidth()
            val color = fragment.getColorBackgroundCard()

            if(canvasFragment == null) {
                canvasFragment = CanvasPaintFragment.newInstance(this, height, width, color)
            }

            if(height > -1 && width > -1) {
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.anim_layout_2, R.anim.anim_layout)
                    .replace(R.id.containerCanvas, canvasFragment!!)
                    .addToBackStack("CANVAS_STACK")
                    .commit()
            }
        }
    }

    override fun loadImage(_bitmap: Bitmap?) {
        binding.textToOpenCanvas.hide()
        binding.image.setImageBitmap(_bitmap)

        bitmap = _bitmap
    }

    override fun getImageBitmap() = bitmap

    companion object {
        fun newInstance(fragment : ConstructorFragment) : EditorPaintNoteFragment {
            val fragmentEditor = EditorPaintNoteFragment()
            fragmentEditor.fragment = fragment
            return fragmentEditor
        }
    }
}