package com.example.foodnote.ui.noteBook.modelNotes

class NoteStandard(
    widthCard: Int,
    heightCard: Int,
    colorCard: Int,
    val string: String,
    val font: String,
    val fontSize: String,
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

open class Note(
    val widthCard: Int,
    val heightCard: Int,
    val colorCard: Int,
    val posX: Int,
    val posY: Int,
    val idNote: Int,
    val elevationNote: Float
)
