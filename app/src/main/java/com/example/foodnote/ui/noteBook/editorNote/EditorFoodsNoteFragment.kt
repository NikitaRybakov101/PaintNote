package com.example.foodnote.ui.noteBook.editorNote

import com.example.foodnote.databinding.FoodNoteEditorBinding
import com.example.foodnote.ui.baseViewBinding.BaseViewBindingFragment
import com.example.foodnote.ui.noteBook.interfaces.EditorFoodsNoteFragmentInterface

class EditorFoodsNoteFragment : BaseViewBindingFragment<FoodNoteEditorBinding>(FoodNoteEditorBinding::inflate) , EditorFoodsNoteFragmentInterface {
    override fun getListFoodsText() = binding.listFoods.text.toString()
    override fun getListWeightText() = binding.listWeight.text.toString()
}