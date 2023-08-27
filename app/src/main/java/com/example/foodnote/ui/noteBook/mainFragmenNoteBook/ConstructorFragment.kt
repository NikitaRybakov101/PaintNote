package com.example.foodnote.ui.noteBook.mainFragmenNoteBook

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.text.InputFilter
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.foodnote.R
import com.example.foodnote.databinding.ConstructorNoteBinding
import com.example.foodnote.ui.baseViewBinding.BaseViewBindingFragment
import com.example.foodnote.ui.noteBook.constNote.Const
import com.example.foodnote.ui.noteBook.constNote.Const.DELAY_BUTTON
import com.example.foodnote.ui.noteBook.constNote.Const.MAX_NOTE_SIZE
import com.example.foodnote.ui.noteBook.constNote.Const.MIN_NOTE_SIZE
import com.example.foodnote.ui.noteBook.constNote.Const.NOTES_ELEVATION
import com.example.foodnote.ui.noteBook.constNote.Const.RANDOM_ID
import com.example.foodnote.ui.noteBook.constNote.Const.STROKE_WIDTH
import com.example.foodnote.ui.noteBook.constNote.Const.STROKE_WIDTH_FOCUS
import com.example.foodnote.ui.noteBook.constNote.ConstType
import com.example.foodnote.ui.noteBook.editorNote.EditorPaintNoteFragment
import com.example.foodnote.ui.noteBook.editorNote.EditorStandardNoteFragment
import com.example.foodnote.ui.noteBook.interfaces.ConstructorFragmentInterface
import com.example.foodnote.ui.noteBook.interfaces.NoteBookFragmentInterface
import com.example.foodnote.ui.noteBook.modelNotes.NotePaint
import com.example.foodnote.ui.noteBook.modelNotes.NoteStandard
import com.example.foodnote.utils.setOnTouchAlphaListener
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.random.Random

class ConstructorFragment : BaseViewBindingFragment<ConstructorNoteBinding>(ConstructorNoteBinding::inflate) , ConstructorFragmentInterface{

    private lateinit var fragmentNoteBook: NoteBookFragmentInterface

    private lateinit var editorStandardNoteFragmentEditor: EditorStandardNoteFragment
    private lateinit var editorPaintNoteFragmentEditor: EditorPaintNoteFragment
    private var typeNote : ConstType = ConstType.STANDARD_TYPE

    private var colorCard = Color.WHITE
    private var flagBackBtn = true
    private var flagCreateBtn = true

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setEditorType()

        checkButton()
        checkColor()
        editTextFilter()
    }

    private fun setEditorType() {
        when (typeNote) {
            ConstType.STANDARD_TYPE -> {
                val fragment = EditorStandardNoteFragment()
                editorStandardNoteFragmentEditor = fragment
                childFragmentManager.beginTransaction().replace(R.id.containerEditNotes,fragment).commitNow()
            }
            ConstType.PAINT_TYPE ->    {
                val fragment = EditorPaintNoteFragment.newInstance(this)
                editorPaintNoteFragmentEditor = fragment
                childFragmentManager.beginTransaction().replace(R.id.containerEditNotes,fragment).commitNow()
            }
            ConstType.FOOD_TYPE ->     {


            }
            else -> {}
        }
    }

    @SuppressLint("SetTextI18n")
    private fun editTextFilter() {
        val filterArray = Array<InputFilter>(1) { InputFilter.LengthFilter(2) }
        binding.editWidth.filters = filterArray
        binding.editHeight.filters = filterArray

        binding.editWidth.setText("50")
        binding.editHeight.setText("50")
    }

    fun setFlag(boolean: Boolean) { }

    private fun checkButton() = with(binding) {
        buttonCreate.setOnClickListener {
            if(flagCreateBtn){
                createNoteWidthHeight()
                flagCreateBtn = false
                timeOutButtonCreate()
            }
        }

        back.setOnTouchAlphaListener {
            if(flagBackBtn){
                fragmentNoteBook.constructorFragmentClose()
                flagBackBtn = false
                timeOutButtonBack()
            }
        }
    }

    private fun timeOutButtonBack(){
        scope.launch {
            delay(DELAY_BUTTON)
            flagBackBtn = true
        }
    }

    private fun timeOutButtonCreate(){
        scope.launch {
            delay(DELAY_BUTTON)
            flagCreateBtn = true
        }
    }

    private fun createNoteWidthHeight() {
        if(getHeight() > -1 && getWidth() > -1) {
            createNote(getHeight(), getWidth())
        }
    }

    override fun getWidth() : Int = heightWidthInputEditText(binding.editWidth)
    override fun getHeight() : Int = heightWidthInputEditText(binding.editHeight)

    private fun heightWidthInputEditText(editText: EditText) : Int {
        val inputW = editText.text.toString()

        if (inputW.isNotEmpty()) {
            val inW = inputW.toInt()

            if(inW in MIN_NOTE_SIZE..MAX_NOTE_SIZE) {
                return inW
            } else {
                editText.error = getString(R.string.range_error)
            }
        } else {
            editText.error = getString(R.string.empty_field_error_messange)
            Toast.makeText(requireContext(),getString(R.string.empty_field_error_messange),Toast.LENGTH_SHORT).show()
        }
        return -1
    }

    private fun checkColor() = with(binding){
        val list = listOf(colorBlue,colorPink,colorGreen,colorYellow,colorGray,colorWhite)

        list.forEach { view ->
            view.setOnClickListener {

                list.forEach { e ->
                    e.strokeColor = Color.LTGRAY
                    e.strokeWidth = STROKE_WIDTH
                }

                view.strokeColor = Color.GRAY
                view.strokeWidth = STROKE_WIDTH_FOCUS
                colorCard = view.backgroundTintList?.defaultColor ?: Color.WHITE
            }
        }
    }

    override fun getColorBackgroundCard() = colorCard

    private fun createNote(height : Int, width : Int) {
        when (typeNote) {
            ConstType.STANDARD_TYPE -> {

                val string = editorStandardNoteFragmentEditor.getNoteText()
                val font = editorStandardNoteFragmentEditor.getFont()
                val fontSize = editorStandardNoteFragmentEditor.getFontSize()

                val randomId = java.util.Random().nextInt(RANDOM_ID)

                val note = NoteStandard(width, height, colorCard, string,font,fontSize,0,0, randomId, NOTES_ELEVATION)
                fragmentNoteBook.saveAndCreateDataNotesStandard(note)
                fragmentNoteBook.constructorFragmentClose()
            }
            ConstType.PAINT_TYPE  ->    {

                val bitmap = editorPaintNoteFragmentEditor.getImageBitmap()
                var bitmapURL = ""

                if(bitmap != null) {
                    bitmapURL = getRandomName()
                    bitmapToFile(bitmap,bitmapURL)
                }
                val randomId = java.util.Random().nextInt(RANDOM_ID)

                val note = NotePaint(width, height, colorCard, bitmapURL,0 ,0, randomId, NOTES_ELEVATION)
                fragmentNoteBook.saveAndCreateDataNotesPaint(note)
                fragmentNoteBook.constructorFragmentClose()
            }
            ConstType.FOOD_TYPE  ->    {



            }
            else -> {}
        }
    }

    private fun getRandomName() = "image-paint-note-${Random(Const.SEED)}${java.util.Random().nextInt()}.png"

    private fun bitmapToFile(bitmap: Bitmap, fileNameToSave: String): File? {
        var file: File? = null
        return try {
            val name = Environment.getExternalStorageDirectory().toString() + File.separator + Environment.DIRECTORY_DCIM + File.separator + fileNameToSave

            file = File(name)
            file.createNewFile()

            Toast.makeText(requireContext(), getString(R.string.saved_mess) + name, Toast.LENGTH_SHORT).show()

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
        }
    }

    companion object {
        fun newInstance(fragmentNotes: NotesFragment, typeNotes: ConstType) : ConstructorFragment {
            val fragment = ConstructorFragment()
            fragment.fragmentNoteBook = fragmentNotes
            fragment.typeNote = typeNotes
            return fragment
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        scope.cancel()
    }
}