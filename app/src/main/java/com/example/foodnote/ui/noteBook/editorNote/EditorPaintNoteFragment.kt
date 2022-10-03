package com.example.foodnote.ui.noteBook.editorNote

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.view.View
import com.example.foodnote.R
import com.example.foodnote.databinding.PaintNoteEditorBinding
import com.example.foodnote.ui.baseViewBinding.BaseViewBindingFragment
import com.example.foodnote.ui.noteBook.mainFragmenNoteBook.ConstructorFragment
import com.example.foodnote.ui.noteBook.canvas.CanvasPaintFragment
import com.example.foodnote.ui.noteBook.interfaces.EditorPaintNoteFragmentInterface
import java.io.File

class EditorPaintNoteFragment : BaseViewBindingFragment<PaintNoteEditorBinding>(PaintNoteEditorBinding::inflate) , EditorPaintNoteFragmentInterface {

    private lateinit var fragment: ConstructorFragment
    private var fileName = "null"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chekButton()
    }

    private fun chekButton() {
        binding.canvas.setOnClickListener {

            val height = fragment.getHeight()
            val width = fragment.getWidth()
            val color = fragment.getColorBackgroundCard()

            fragment.setFlag(false)

            if(height > -1 && width > -1) {
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.anim_layout_2, R.anim.anim_layout)
                    .replace(R.id.containerCanvas, CanvasPaintFragment.newInstance(this, height, width, color))
                    .commit()
            }
        }
    }

    fun setFlagAnBlockButton() {
        fragment.setFlag(true)
    }

    override fun loadImage(fileNameToSave : String) {
        fileName = fileNameToSave
        binding.image.setImageBitmap( BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().toString() + File.separator +  Environment.DIRECTORY_DCIM + File.separator + fileNameToSave) )
    }

    override fun getImageURL() = fileName

    companion object {
        fun newInstance(fragment : ConstructorFragment) : EditorPaintNoteFragment {
            val fragmentEditor = EditorPaintNoteFragment()
            fragmentEditor.fragment = fragment
            return fragmentEditor
        }
    }
}