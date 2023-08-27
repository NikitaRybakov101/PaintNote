package com.example.foodnote.data.databaseRoom

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.foodnote.data.databaseRoom.dao.DaoDB
import com.example.foodnote.data.databaseRoom.entities.*

@Database(entities = [EntitiesNotesPaint::class, EntitiesNotesStandard::class], version = 6, exportSchema = false)
abstract class DataBase : RoomDatabase() {
    abstract fun dataBase() : DaoDB
}