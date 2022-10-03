package com.example.foodnote.ui.noteBook.viewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodnote.data.databaseRoom.dao.DaoDB
import com.example.foodnote.data.databaseRoom.entities.EntitiesNotesFood
import com.example.foodnote.data.databaseRoom.entities.EntitiesNotesPaint
import com.example.foodnote.data.databaseRoom.entities.EntitiesNotesStandard
import com.example.foodnote.ui.noteBook.constNote.Const
import com.example.foodnote.ui.noteBook.modelNotes.NoteFood
import com.example.foodnote.ui.noteBook.modelNotes.NotePaint
import com.example.foodnote.ui.noteBook.modelNotes.NoteStandard
import com.example.foodnote.ui.noteBook.stateData.StateDataNotes
import com.example.foodnote.ui.noteBook.viewModel.VievModelInterfaces.ViewModelNotesInterface
import kotlinx.coroutines.*

class ViewModelNotesFragment(private val notesDao: DaoDB) : ViewModel() , ViewModelNotesInterface{

    private val liveData = MutableLiveData<StateDataNotes>()
    private val scope = CoroutineScope(Dispatchers.IO)

    fun getLiveData() = liveData

    override fun loadDataNote() {
        liveData.value = StateDataNotes.Loading("Loading")

        scope.launch {
            val dataStandardNotes = notesDao.getAllNotesStandard()
            val dataPaintNotes = notesDao.getAllNotesPaint()
            val dataFoodNotes = notesDao.getAllNotesFood()
            withContext(Dispatchers.Main) {

                val listNotePaint = ArrayList<NotePaint>()
                dataPaintNotes.forEach { not ->
                    val note = NotePaint(not.widthCard,not.heightCard,not.colorCard,not.fileName,not.cardPositionX,not.cardPositionY,not.idCard,not.elevation.toFloat())
                    listNotePaint.add(note)
                }
                liveData.value = StateDataNotes.SuccessNotePaint(listNotePaint)


                val listNoteStandard = ArrayList<NoteStandard>()
                dataStandardNotes.forEach { not ->
                    val note = NoteStandard(not.widthCard,not.heightCard,not.colorCard,not.note,not.cardPositionX,not.cardPositionY,not.idCard,not.elevation.toFloat())
                    listNoteStandard.add(note)
                }
                liveData.value = StateDataNotes.SuccessNoteStandard(listNoteStandard)


                val listNoteFood = ArrayList<NoteFood>()
                dataFoodNotes.forEach { not ->
                    val note = NoteFood(not.widthCard,not.heightCard,not.colorCard,not.listFoods,not.listWeight,not.general,not.cardPositionX,not.cardPositionY,not.idCard,not.elevation.toFloat())
                    listNoteFood.add(note)
                }
                liveData.value = StateDataNotes.SuccessNoteFood(listNoteFood)
            }
        }
    }

    override fun saveDataNotesPaint(note : NotePaint): Unit = with(note) {
        scope.launch {
            notesDao.insertNotePaint(
                EntitiesNotesPaint(
                widthCard = widthCard, heightCard = heightCard, colorCard = colorCard, fileName = bitmapURL,
                cardPositionX = posX, cardPositionY = posY, idCard = idNote, elevation = elevationNote.toInt() )
            )
        }

    }

    override fun saveDataNotesStandard(note : NoteStandard): Unit = with(note){
        scope.launch {
            notesDao.insertNoteStandard(
                EntitiesNotesStandard(
                widthCard = widthCard, heightCard = heightCard, colorCard = colorCard, note = string,
                cardPositionX = posX, cardPositionY = posY, idCard = idNote, elevation = elevationNote.toInt() )
            )
        }
    }

    override fun saveDataNotesFoods(note : NoteFood): Unit = with(note){
        scope.launch {
            notesDao.insertNoteFood(
                EntitiesNotesFood(
                widthCard = widthCard, heightCard = heightCard, colorCard = colorCard, general = general, listFoods = stringFoods, listWeight = stringWeight,
                cardPositionX = posX, cardPositionY = posY, idCard = idNote, elevation = elevationNote.toInt() )
            )
        }
    }

    override fun setNewCardCoordinatesData(view: View) {
        scope.launch {
            when (view.tag) {
                Const.TABLE_STANDARD -> {
                    notesDao.updateCoordinatesCardNotesStandardX(view.x.toInt(),view.id)
                    notesDao.updateCoordinatesCardNotesStandardY(view.y.toInt(),view.id)
                }
                Const.TABLE_PAINT -> {
                    notesDao.updateCoordinatesCardNotesPaintX(view.x.toInt(),view.id)
                    notesDao.updateCoordinatesCardNotesPaintY(view.y.toInt(),view.id)
                }
                Const.TABLE_FOOD -> {
                    notesDao.updateCoordinatesCardNotesFoodX(view.x.toInt(),view.id)
                    notesDao.updateCoordinatesCardNotesFoodY(view.y.toInt(),view.id)
                }
            }
        }
    }

    override fun setElevationView(view: View) {
        scope.launch {
            when (view.tag) {
                Const.TABLE_STANDARD -> { notesDao.updateCardElevationStandard(view.elevation.toInt(),view.id) }
                Const.TABLE_PAINT -> {  notesDao.updateCardElevationPaint(view.elevation.toInt(),view.id) }
                Const.TABLE_FOOD -> {  notesDao.updateCardElevationFood(view.elevation.toInt(),view.id) }
            }
        }
    }

    override fun deleteNotes(view: View) {
        scope.launch {
            when (view.tag) {
                Const.TABLE_STANDARD -> { notesDao.deleteNoteStandard(view.id) }
                Const.TABLE_PAINT -> { notesDao.deleteNotePaint(view.id) }
                Const.TABLE_FOOD -> { notesDao.deleteNoteFood(view.id) }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }

}