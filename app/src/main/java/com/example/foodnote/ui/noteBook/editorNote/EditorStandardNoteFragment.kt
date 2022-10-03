package com.example.foodnote.ui.noteBook.editorNote

import com.example.foodnote.databinding.StandartNoteEditorBinding
import com.example.foodnote.ui.baseViewBinding.BaseViewBindingFragment
import com.example.foodnote.ui.noteBook.interfaces.EditorStandardInterface


class EditorStandardNoteFragment : BaseViewBindingFragment<StandartNoteEditorBinding>(StandartNoteEditorBinding::inflate) , EditorStandardInterface {
    override fun getNoteText() = binding.editNote.text.toString()
}