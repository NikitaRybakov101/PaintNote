package com.example.foodnote.data.databaseRoom.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.foodnote.data.databaseRoom.entities.EntitiesNotesCalories

@Dao
interface DaoDbNotesCalories {
    @Query("SELECT * FROM noteCalories")
    fun getAllNotesCalories() : List<EntitiesNotesCalories>

    @Query("DELETE FROM noteCalories WHERE header LIKE :name")
    fun deleteNoteCalories(name : String) : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note : EntitiesNotesCalories)
}