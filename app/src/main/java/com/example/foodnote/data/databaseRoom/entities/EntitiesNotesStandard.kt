package com.example.foodnote.data.databaseRoom.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "noteStandard")
data class EntitiesNotesStandard(
    @PrimaryKey(autoGenerate = true) val id : Int = 0,

    @ColumnInfo(name = "widthCard")   val widthCard: Int,
    @ColumnInfo(name = "heightCard")  val heightCard: Int,
    @ColumnInfo(name = "colorCard")   val colorCard: Int,
    @ColumnInfo(name = "note")        val note: String,

    @ColumnInfo(name = "cardPositionX")    val cardPositionX: Int,
    @ColumnInfo(name = "cardPositionY")    val cardPositionY: Int,
    @ColumnInfo(name = "elevation")        val elevation: Int,

    @ColumnInfo(name = "idCard") val idCard: Int
)
