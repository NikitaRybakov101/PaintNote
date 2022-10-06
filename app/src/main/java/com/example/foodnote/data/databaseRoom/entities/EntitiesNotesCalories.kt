package com.example.foodnote.data.databaseRoom.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "noteCalories")
data class EntitiesNotesCalories(
    @PrimaryKey(autoGenerate = true) val id : Int = 0,

    @ColumnInfo(name = "header") val header : String,
    @ColumnInfo(name = "calories") val calories : Int,
    @ColumnInfo(name = "fat") val fat : Int,
    @ColumnInfo(name = "protein") val protein : Int,
    @ColumnInfo(name = "water") val water : Int
 )