package com.example.foodnote.ui.noteBook.stateData

import com.example.foodnote.ui.noteBook.modelNotes.NoteFood
import com.example.foodnote.ui.noteBook.modelNotes.NotePaint
import com.example.foodnote.ui.noteBook.modelNotes.NoteStandard

sealed class StateData {
    data class Success(val data : String?) : StateData()
    data class Loading(val loading : String)     : StateData()
    data class Error  (val error   : Throwable)  : StateData()
}

sealed class StateDataNotes {

    data class SuccessNoteStandard(val listNote : ArrayList<NoteStandard>?) : StateDataNotes()
    data class SuccessNotePaint(val listNote : ArrayList<NotePaint>?) : StateDataNotes()
    data class SuccessNoteFood(val listNote : ArrayList<NoteFood>?) : StateDataNotes()

    data class Loading(val loading : String)     : StateDataNotes()
    data class Error  (val error   : Throwable)  : StateDataNotes()
}

sealed class StateDataCompose {
    data class Success(val currentValue : Int, val maxValue : Int) : StateDataCompose()
}

sealed class StateDataDonat {
    data class SetData(val expanded : Boolean, val angle : Boolean) : StateDataDonat()
    data class Animated(val name: String) : StateDataDonat()
}