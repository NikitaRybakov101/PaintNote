package com.example.foodnote.ui.noteBook.mainFragmenNoteBook

import android.graphics.Color
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.foodnote.R
import com.example.foodnote.databinding.ConstructorNoteBinding
import com.example.foodnote.ui.baseViewBinding.BaseViewBindingFragment
import com.example.foodnote.ui.noteBook.constNote.Const.DELAY_BUTTON
import com.example.foodnote.ui.noteBook.constNote.Const.MAX_NOTE_SIZE
import com.example.foodnote.ui.noteBook.constNote.Const.MIN_NOTE_SIZE
import com.example.foodnote.ui.noteBook.constNote.Const.NOTES_ELEVATION
import com.example.foodnote.ui.noteBook.constNote.Const.RANDOM_ID
import com.example.foodnote.ui.noteBook.constNote.Const.STROKE_WIDTH
import com.example.foodnote.ui.noteBook.constNote.Const.STROKE_WIDTH_FOCUS
import com.example.foodnote.ui.noteBook.constNote.ConstType
import com.example.foodnote.ui.noteBook.editorNote.EditorFoodsNoteFragment
import com.example.foodnote.ui.noteBook.editorNote.EditorPaintNoteFragment
import com.example.foodnote.ui.noteBook.editorNote.EditorStandardNoteFragment
import com.example.foodnote.ui.noteBook.interfaces.ConstructorFragmentInterface
import com.example.foodnote.ui.noteBook.interfaces.NoteBookFragmentInterface
import com.example.foodnote.ui.noteBook.modelNotes.NoteFood
import com.example.foodnote.ui.noteBook.modelNotes.NotePaint
import com.example.foodnote.ui.noteBook.modelNotes.NoteStandard
import com.example.foodnote.ui.noteBook.stateData.StateData
import com.example.foodnote.ui.noteBook.viewModel.ViewModelConstructorFragment
import com.example.foodnote.utils.showToast
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class ConstructorFragment : BaseViewBindingFragment<ConstructorNoteBinding>(ConstructorNoteBinding::inflate) , ConstructorFragmentInterface{

    private lateinit var fragmentNoteBook: NoteBookFragmentInterface

    private lateinit var editorStandardNoteFragmentEditor: EditorStandardNoteFragment
    private lateinit var editorPaintNoteFragmentEditor: EditorPaintNoteFragment
    private lateinit var editorFoodNoteFragmentEditor: EditorFoodsNoteFragment
    private var typeNote : ConstType = ConstType.STANDARD_TYPE

    private var colorCard = Color.WHITE
    private var flag = true

    private val scope = CoroutineScope(Dispatchers.IO)
    private val viewModel : ViewModelConstructorFragment by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()

        setEditorType()

        chekButton()
        chekColor()
        editTextFilter()
    }

    private fun initViewModel() { viewModel.getLiveData().observe(viewLifecycleOwner) { render(it) } }

    private fun render(stateData: StateData) {
        when(stateData) {
            is StateData.Loading -> {

            }
            is StateData.Success -> {
                stateData.data?.let { createFoodNote(it) }
            }
            is StateData.Error ->   {
                context?.showToast(getString(R.string.network_error_mess))
            }
        }
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
                val fragment = EditorFoodsNoteFragment()
                editorFoodNoteFragmentEditor = fragment
                childFragmentManager.beginTransaction().replace(R.id.containerEditNotes,fragment).commitNow()
            }
            else -> {}
        }
    }

    private fun editTextFilter() {
        val filterArray = Array<InputFilter>(1) { InputFilter.LengthFilter(2) }
        binding.editWidth.filters = filterArray
        binding.editHeight.filters = filterArray
    }

    fun setFlag(boolean: Boolean) {flag = boolean}

    private fun chekButton() = with(binding) {
        buttonCreate.setOnClickListener {
            if(flag){
                createNoteWidthHeight()
                flag = false
                timeOutButton()
            }
        }

        back.setOnClickListener {
            if(flag){
                fragmentNoteBook.constructorFragmentClose()
                flag = false
                timeOutButton()
            }
        }
    }

    private fun timeOutButton(){
        scope.launch {
            delay(DELAY_BUTTON)
            flag = true
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

    private fun chekColor() = with(binding){
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
                val randomId = Random().nextInt(RANDOM_ID)

                val note = NoteStandard(width, height, colorCard, string,0,0, randomId, NOTES_ELEVATION)
                fragmentNoteBook.saveAndCreateDataNotesStandard(note)
                fragmentNoteBook.constructorFragmentClose()
            }
            ConstType.PAINT_TYPE  ->    {
                val bitmapURL = editorPaintNoteFragmentEditor.getImageURL()
                val randomId = Random().nextInt(RANDOM_ID)

                val note = NotePaint(width, height, colorCard, bitmapURL,0 ,0, randomId, NOTES_ELEVATION)
                fragmentNoteBook.saveAndCreateDataNotesPaint(note)
                fragmentNoteBook.constructorFragmentClose()
            }
            ConstType.FOOD_TYPE  ->    {
                val stringFoods = editorFoodNoteFragmentEditor.getListFoodsText()
                val stringWeight = editorFoodNoteFragmentEditor.getListWeightText()

                val listFoods = stringFoods.split("\n")
                val listWeight = stringWeight.split("\n")

                viewModel.sendServerToCal(listFoods,listWeight)
            }
            else -> {}
        }
    }

    private fun createFoodNote(general: String){
        val stringFoods = editorFoodNoteFragmentEditor.getListFoodsText()
        val stringWeight = editorFoodNoteFragmentEditor.getListWeightText()
        val randomId = Random().nextInt(RANDOM_ID)

        val note = NoteFood(getWidth(), getHeight(), colorCard, stringFoods, stringWeight, general,0 ,0, randomId, NOTES_ELEVATION)
        fragmentNoteBook.saveAndCreateDataNotesFoods(note)
        fragmentNoteBook.constructorFragmentClose()
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