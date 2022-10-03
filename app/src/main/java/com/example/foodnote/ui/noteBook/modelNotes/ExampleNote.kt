package com.example.foodnote.ui.noteBook.modelNotes

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Environment
import android.widget.Toast
import com.example.foodnote.R
import com.example.foodnote.ui.noteBook.constNote.Const
import com.example.foodnote.ui.noteBook.mainFragmenNoteBook.NotesFragment
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.random.Random

class ExampleNote(private val fragment: NotesFragment, private val context: Context, private val widthScreen : Int) {

////////////////------------------Example note --- Hard code----------------/////////////////////////
    private lateinit var nameFile : String
    private fun getName() = "image244${Random(Const.SEED)}.png"

    fun bitmapToFile(bitmap: Bitmap, fileNameToSave: String = getName()): File? {
        var file: File? = null
        return try {
            val name = Environment.getExternalStorageDirectory().toString() + File.separator + Environment.DIRECTORY_DCIM + File.separator + fileNameToSave
            nameFile = fileNameToSave

            file = File(name)
            file.createNewFile()

            Toast.makeText(context, context.getString(R.string.saved_mess) + name, Toast.LENGTH_SHORT).show()

            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos)

            val bitmapData = bos.toByteArray()

            val fos = FileOutputStream(file)
            fos.write(bitmapData)
            fos.flush()
            fos.close()

            file
        } catch (e: Exception) {
            e.printStackTrace()
            file
        } finally {
            createExampleNote()
        }
    }

    private fun createExampleNote() {
        val stringStandard = "Note\n- Finish the project\n- Bugfix navigation\n- Added new brash"

        val bitmapURL = nameFile

        val stringFoods = "Milk\nApple\nOrange"
        val stringWeight = "1300\n700\n430"
        val general = "2430 gm"

        val height = ((55 * widthScreen) / 100) + convertDpToPixels(Const.CARD_NOTE_DP)
        val width = (42 * widthScreen) / 100

        val noteStandard = NoteStandard(50, 40, Color.rgb(252, 252, 215 ), stringStandard,width + 20 + 40,height + 20 + 180,  51, Const.NOTES_ELEVATION)
        fragment.saveAndCreateDataNotesStandard(noteStandard)

        val notePaint = NotePaint(75, 55, Color.WHITE, bitmapURL,40 ,180, 52, Const.NOTES_ELEVATION)
        fragment.saveAndCreateDataNotesPaint(notePaint)

        val noteFood = NoteFood(42, 60, Color.WHITE, stringFoods, stringWeight, general,40 ,height + 20 + 180, 53, Const.NOTES_ELEVATION)
        fragment.saveAndCreateDataNotesFoods(noteFood)
    }

    private fun convertDpToPixels(dp: Int) = (dp * context.resources.displayMetrics.density).toInt()

}