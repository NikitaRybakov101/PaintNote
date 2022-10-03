package com.example.foodnote.ui.noteBook.modelNotes

class NoteStandard(
    widthCard: Int,
    heightCard: Int,
    colorCard: Int,
    val string: String,
    posX: Int,
    posY: Int,
    id: Int,
    elevation: Float

) : Note(widthCard, heightCard, colorCard, posX, posY, id, elevation)

class NotePaint(
    widthCard: Int,
    heightCard: Int,
    colorCard: Int,
    val bitmapURL: String,
    posX: Int,
    posY: Int,
    id: Int,
    elevation: Float

) : Note(widthCard, heightCard, colorCard, posX, posY, id, elevation)

class NoteFood(
    widthCard: Int,
    heightCard: Int,
    colorCard: Int,
    val stringFoods : String,
    val stringWeight : String,
    val general : String,
    posX: Int,
    posY: Int,
    id: Int,
    elevation: Float

) : Note(widthCard, heightCard, colorCard, posX, posY, id, elevation)

open class Note(
    val widthCard: Int,
    val heightCard: Int,
    val colorCard: Int,
    val posX: Int,
    val posY: Int,
    val idNote: Int,
    val elevationNote: Float
)
