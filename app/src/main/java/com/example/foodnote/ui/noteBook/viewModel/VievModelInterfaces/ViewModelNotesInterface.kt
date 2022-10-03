package com.example.foodnote.ui.noteBook.viewModel.VievModelInterfaces

import android.view.View
import com.example.foodnote.ui.noteBook.modelNotes.NoteFood
import com.example.foodnote.ui.noteBook.modelNotes.NotePaint
import com.example.foodnote.ui.noteBook.modelNotes.NoteStandard

interface ViewModelNotesInterface {
    fun saveDataNotesPaint(note : NotePaint)
    fun saveDataNotesStandard(note : NoteStandard)
    fun saveDataNotesFoods(note : NoteFood)

    fun loadDataNote()

    fun setNewCardCoordinatesData(view : View)
    fun setElevationView(view: View)
    fun deleteNotes(view: View)
}