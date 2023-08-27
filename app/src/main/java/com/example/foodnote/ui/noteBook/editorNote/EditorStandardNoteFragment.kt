package com.example.foodnote.ui.noteBook.editorNote

import android.R
import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import com.example.foodnote.databinding.StandartNoteEditorBinding
import com.example.foodnote.ui.baseViewBinding.BaseViewBindingFragment
import com.example.foodnote.ui.noteBook.interfaces.EditorStandardInterface
import com.example.foodnote.utils.setOnTouchAlphaListener


class EditorStandardNoteFragment : BaseViewBindingFragment<StandartNoteEditorBinding>(StandartNoteEditorBinding::inflate) , EditorStandardInterface {

    private val hintsFonts = arrayOf(
        "Classic",
        "Poppins",
        "Aquire",
        "Magicpearl",
        "Arctika"
    )

    private val hintsSizeFont = arrayOf(
        "4",
        "8",
        "12",
        "16",
        "20",
        "24",
        "28",
        "32",
        "64",
        "84",
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        changeSizeFont()
        setHintsForInput()
        changeFont()
    }

    private fun changeSizeFont() {
        binding.fontSizeEdit.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                if (s.toString().isNotEmpty()) {

                    val fontSize = s.toString().toFloat()

                    if(fontSize in 1.0..80.0) {
                        binding.editNote.textSize = s.toString().toFloat()
                    } else {
                        if(fontSize < 1) binding.editNote.textSize = 1f
                        if(fontSize > 80) binding.editNote.textSize = 80f
                    }
                }
            }
        })
    }

    private fun changeFont() {
        binding.fontEdit.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val font = s.toString()
                if (font.isNotEmpty()) {
                    when(font) {
                        "Classic" -> { setTypeface("classic") }
                        "Poppins" -> { setTypeface("poppinssemibold.ttf") }
                        "Aquire" -> { setTypeface("aquire.otf") }
                        "Magicpearl" -> { setTypeface("magicpearl.ttf") }
                        "Arctika" -> { setTypeface("arctika.ttf") }
                    }
                }
            }
        })
    }

    private fun setTypeface(font: String) {
        val typeface = if(font == "classic") {
            Typeface.DEFAULT
        } else {
            Typeface.createFromAsset(context?.assets, "fonts/${font}")
        }

        binding.editNote.typeface = typeface
        binding.fontEdit.typeface = typeface
    }

    private fun setHintsForInput() {
        binding.fontEdit.setAdapter(ArrayAdapter(requireContext(), R.layout.simple_spinner_dropdown_item, hintsFonts))
        binding.fontEdit.isFocusable = false

        binding.dropFonts.setOnTouchAlphaListener {
            binding.fontEdit.showDropDown()
        }

        binding.fontSizeEdit.setAdapter(ArrayAdapter(requireContext(), R.layout.simple_gallery_item, hintsSizeFont))
        binding.fontSizeEdit.isFocusable = false

        binding.dropSizeFont.setOnTouchAlphaListener {
            binding.fontSizeEdit.showDropDown()
        }
    }

    override fun getNoteText() = binding.editNote.text.toString()
    override fun getFontSize() = binding.fontSizeEdit.text.toString()

    override fun getFont() : String {
        return when(binding.fontEdit.text.toString()) {
            "Classic" -> "classic"
            "Poppins" -> "poppinssemibold.ttf"
            "Aquire" -> "aquire.otf"
            "Magicpearl" -> "magicpearl.ttf"
            "Arctika" -> "arctika.ttf"
            else -> ""
        }
    }
}