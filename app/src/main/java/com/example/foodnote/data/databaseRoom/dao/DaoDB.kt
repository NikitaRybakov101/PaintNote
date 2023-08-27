package com.example.foodnote.data.databaseRoom.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.foodnote.data.databaseRoom.entities.EntitiesNotesPaint
import com.example.foodnote.data.databaseRoom.entities.EntitiesNotesStandard

@Dao
interface DaoDB {
    @Query("SELECT * FROM notePaint")
    fun getAllNotesPaint() : List<EntitiesNotesPaint>

    @Query("SELECT * FROM noteStandard")
    fun getAllNotesStandard() : List<EntitiesNotesStandard>



    @Query("DELETE FROM notePaint WHERE idCard LIKE :id")
    fun deleteNotePaint(id : Int) : Int

    @Query("DELETE FROM noteStandard WHERE idCard LIKE :id")
    fun deleteNoteStandard(id : Int) : Int

    @Query("DELETE FROM notePaint")
    fun deleteAll() : Int



    @Query("UPDATE notePaint SET cardPositionX = :x WHERE idCard IN (:id)")
    fun updateCoordinatesCardNotesPaintX(x : Int, id: Int)

    @Query("UPDATE notePaint SET cardPositionY = :y WHERE idCard IN (:id)")
    fun updateCoordinatesCardNotesPaintY(y : Int, id: Int)

    @Query("UPDATE notePaint SET elevation = :elevation WHERE idCard IN (:id)")
    fun updateCardElevationPaint(elevation: Int, id: Int)


    @Query("UPDATE noteStandard SET cardPositionX = :x WHERE idCard IN (:id)")
    fun updateCoordinatesCardNotesStandardX(x : Int, id: Int)

    @Query("UPDATE noteStandard SET cardPositionY = :y WHERE idCard IN (:id)")
    fun updateCoordinatesCardNotesStandardY(y : Int, id: Int)

    @Query("UPDATE noteStandard SET elevation = :elevation WHERE idCard IN (:id)")
    fun updateCardElevationStandard(elevation: Int, id: Int)



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotePaint(note : EntitiesNotesPaint)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNoteStandard(note : EntitiesNotesStandard)
}