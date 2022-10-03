package com.example.foodnote.ui.noteBook.interfaces

import com.example.foodnote.ui.noteBook.modelNotes.NoteFood
import com.example.foodnote.ui.noteBook.modelNotes.NotePaint
import com.example.foodnote.ui.noteBook.modelNotes.NoteStandard

interface NoteBookFragmentInterface {

    fun constructorFragmentClose()

    fun saveAndCreateDataNotesPaint(note : NotePaint)
    fun saveAndCreateDataNotesStandard(note : NoteStandard)
    fun saveAndCreateDataNotesFoods(note : NoteFood)
}