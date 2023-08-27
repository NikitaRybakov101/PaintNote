package com.example.foodnote.ui.noteBook.mainFragmenNoteBook

import android.animation.ObjectAnimator
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.os.Bundle
import android.os.Environment
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import androidx.appcompat.app.AlertDialog
import androidx.core.animation.doOnEnd
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import com.example.foodnote.R
import com.example.foodnote.data.databaseRoom.dao.DaoDB
import com.example.foodnote.databinding.CardNotesBinding
import com.example.foodnote.databinding.NotebookFragmentBinding
import com.example.foodnote.di.DATA_BASE
import com.example.foodnote.di.VIEW_MODEL_NOTES
import com.example.foodnote.ui.baseViewBinding.BaseViewBindingFragment
import com.example.foodnote.ui.noteBook.constNote.Const.CARD_NOTE_DP
import com.example.foodnote.ui.noteBook.constNote.Const.CONST_SCALE
import com.example.foodnote.ui.noteBook.constNote.Const.DURATION_ANIMATION_CONSTRUCTOR
import com.example.foodnote.ui.noteBook.constNote.Const.IS_FIRST_RUN
import com.example.foodnote.ui.noteBook.constNote.Const.MAX_SIZE_TEXT
import com.example.foodnote.ui.noteBook.constNote.Const.PRESENT_100
import com.example.foodnote.ui.noteBook.constNote.Const.STACK_CONSTRUCTOR
import com.example.foodnote.ui.noteBook.constNote.Const.TABLE_PAINT
import com.example.foodnote.ui.noteBook.constNote.Const.TABLE_STANDARD
import com.example.foodnote.ui.noteBook.constNote.ConstType
import com.example.foodnote.ui.noteBook.helperView.MovedView
import com.example.foodnote.ui.noteBook.interfaces.NoteBookFragmentInterface
import com.example.foodnote.ui.noteBook.modelNotes.*
import com.example.foodnote.ui.noteBook.stateData.StateDataNotes
import com.example.foodnote.ui.noteBook.viewModel.ViewModelNotesFragment
import com.example.foodnote.utils.Permission
import com.example.foodnote.utils.hide
import com.example.foodnote.utils.show
import com.example.foodnote.utils.showToast
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import java.io.File
import java.lang.Exception

class NotesFragment : BaseViewBindingFragment<NotebookFragmentBinding>(NotebookFragmentBinding::inflate) , NoteBookFragmentInterface {

    private lateinit var movedView: MovedView
    private var widthScreen = 0
    private var flagBlockChip = true

    private val scope = CoroutineScope(Dispatchers.IO)
    private val notesDao: DaoDB by inject(named(DATA_BASE)) { parametersOf(requireActivity()) }
    private val viewModel : ViewModelNotesFragment by viewModel(named(VIEW_MODEL_NOTES)) { parametersOf(notesDao) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initMovedView()

        viewModel.loadDataNote()
        loadStartExampleNote()

        setWidthPixels()
        checkChip()

        setTypeface()
    }

    private fun setTypeface() {
        val typeface = Typeface.createFromAsset(context?.assets, "fonts/poppinssemibold.ttf")
        binding.textNote.typeface = typeface
        binding.painNote.typeface = typeface
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        clearPopBackStack();
    }

    private fun clearPopBackStack() {
        for (i in 0 until  requireActivity().supportFragmentManager.backStackEntryCount) {
            requireActivity().supportFragmentManager.clearBackStack("CANVAS_STACK")
        }

        requireActivity().supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.anim_layout_2, R.anim.anim_layout)
            .replace(R.id.containerCanvas, Fragment())
            .commit()
    }

    private fun initViewModel() { viewModel.getLiveData().observe(viewLifecycleOwner) { render(it) } }

    private fun render(stateData: StateDataNotes) {
        when(stateData) {
            is StateDataNotes.Loading -> {
                binding.progress.show()
            }
            is StateDataNotes.SuccessNoteStandard -> {
                binding.progress.hide()
                stateData.listNote?.let {
                    it.forEach { noteStandard -> setDataCreateStandardNote(noteStandard) }
                }
            }
            is StateDataNotes.SuccessNotePaint -> {
                binding.progress.hide()
                stateData.listNote?.let {
                    it.forEach { notePaint -> setDataCreatePaintNote(notePaint) }
                }
            }
            is StateDataNotes.Error ->   {
                context?.showToast(getString(R.string.network_error_mess))
            }
        }
    }

    override fun saveAndCreateDataNotesPaint(note : NotePaint) {
        viewModel.saveDataNotesPaint(note)
        setDataCreatePaintNote(note)
    }

    override fun saveAndCreateDataNotesStandard(note : NoteStandard) {
        viewModel.saveDataNotesStandard(note)
        setDataCreateStandardNote(note)
    }

    private fun setDataCreateStandardNote(note : NoteStandard) {
        val cardNoteViewBind = CardNotesBinding.inflate(layoutInflater, binding.root, false)
        val cardNoteView = cardNoteViewBind.root

        cardNoteView.tag = TABLE_STANDARD

        var size = 18f
        try { size = note.fontSize.toFloat()
        } catch (_: Exception) { }

        val fontName = note.font
        val fontType = if(fontName == "classic") {
            Typeface.DEFAULT
        } else {
            Typeface.createFromAsset(context?.assets, "fonts/${fontName}")
        }

        cardNoteViewBind.textNote.apply {
            text = note.string
            textSize = size
            typeface = fontType
        }

        cardNoteViewBind.buttonDelete.setOnClickListener { deleteDialog(cardNoteView) }
        createNote(note,cardNoteView)
    }

    private fun setDataCreatePaintNote(note: NotePaint) {
        val cardNoteViewBind = CardNotesBinding.inflate(layoutInflater, binding.root, false)
        val cardNoteView = cardNoteViewBind.root

        cardNoteView.tag = TABLE_PAINT
        cardNoteViewBind.buttonDelete.setOnClickListener { deleteDialog(cardNoteView) }
        cardNoteViewBind.imageNote.setImageBitmap( BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().toString() + File.separator +  Environment.DIRECTORY_DCIM + File.separator + note.bitmapURL) )

        createNote(note,cardNoteView)
    }

    private fun createNote(note: Note, cardNoteView: MaterialCardView) = with(note) {
        cardNoteView.updateLayoutParams {
            height = ((heightCard * widthScreen) / PRESENT_100) + convertDpToPixels(CARD_NOTE_DP)
            width = (widthCard * widthScreen) / PRESENT_100
        }

        cardNoteView.apply {
            x = posX.toFloat()
            y = posY.toFloat()
            id = idNote
            elevation = elevationNote
            setCardBackgroundColor(colorCard)
        }
        binding.root.addView(cardNoteView)
        movedView.addView(cardNoteView)
    }

    private fun deleteDialog(cardNoteView: View) {

        val dialog = AlertDialog.Builder(requireContext())
        dialog.setTitle(getString(R.string.alert_dialog_header))
        dialog.setCancelable(true)

        dialog.setNegativeButton(getString(R.string.no)) { dialog, _ ->
            dialog.dismiss()
        }
        dialog.setPositiveButton(getString(R.string.yes)) { dialog, _ ->
            binding.root.removeView(cardNoteView)
            movedView.removeView(cardNoteView)

            viewModel.deleteNotes(cardNoteView)
            dialog.dismiss()
        }
        dialog.create().show()
    }

    private fun convertDpToPixels(dp: Int) = (dp * requireContext().resources.displayMetrics.density).toInt()

    private fun initMovedView() {
        flagBlockChip = true
        movedView = MovedView(ArrayList(),binding.root, viewModel)
    }

    private fun setWidthPixels() {
        val metrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(metrics)
        widthScreen = metrics.widthPixels

        binding.containerConstructor.x = widthScreen.toFloat()
    }

    private fun checkChip() {
        binding.chipStandardNote.setOnClickListener { actionChip(ConstType.STANDARD_TYPE) }
        binding.chipPaintNote.setOnClickListener { actionChip(ConstType.PAINT_TYPE) }
    }

    private var typeSave : ConstType = ConstType.NULL_TYPE

    private fun actionChip(type: ConstType) {
        if (flagBlockChip) {
            change(type)
            flagBlockChip = false
        } else {
            typeSave = type
        }
    }

    private fun change(type: ConstType) {

        if(typeSave == ConstType.NULL_TYPE) {
            constructorDrop(type)
        } else {
            if(type == ConstType.NULL_TYPE) {
                constructorFragmentClose()
            } else {
                constructorCloseAndOpen(type)
            }
        }
        typeSave = type
    }

    private fun constructorDrop(type: ConstType) {
        flagBlockChip = false
        movedView.blockMove(false)
        setFragmentConstructor(type)
        objectAnimation(0f)
    }

    override fun constructorFragmentClose() {
        typeSave = ConstType.NULL_TYPE

        flagBlockChip = false
        movedView.blockMove(true)
        objectAnimation( widthScreen.toFloat())
    }

    private fun objectAnimation(value : Float) {
        ObjectAnimator.ofFloat(binding.containerConstructor, View.X, value).apply {
            duration = DURATION_ANIMATION_CONSTRUCTOR
            interpolator = AnticipateOvershootInterpolator(1f)
            start()
        }.doOnEnd {
            flagBlockChip = true

            if((value == widthScreen.toFloat()) && (typeSave != ConstType.NULL_TYPE)) {
                constructorDrop(typeSave)
            }
        }
    }

    private fun constructorCloseAndOpen(type: ConstType) {
        ObjectAnimator.ofFloat(binding.containerConstructor, View.X, widthScreen.toFloat()).apply {
            duration = DURATION_ANIMATION_CONSTRUCTOR
            interpolator = AnticipateOvershootInterpolator(1f)
            start()
        }.doOnEnd {
            constructorDrop(type)
        }
    }

    private fun setFragmentConstructor(type : ConstType) {

        childFragmentManager
            .beginTransaction()
            .replace(R.id.containerConstructor, ConstructorFragment.newInstance(this,type))
            .addToBackStack(STACK_CONSTRUCTOR)
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

////////////////----------------Example note-----------/////////////////////////
    private fun loadStartExampleNote() {
        val prefs: SharedPreferences = requireActivity().getPreferences(MODE_PRIVATE)
        if (prefs.getBoolean(IS_FIRST_RUN, true)) {
            saveImage()
        }
        prefs.edit().putBoolean(IS_FIRST_RUN, false).apply()
    }

    private fun saveImage() {
        Permission.sendPermissionForCreateFile({ result ->
            if(result) {
                val bitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.image_note_paint)
                val example = ExampleNote(this, requireContext(), widthScreen)
                example.bitmapToFile(bitmap)
            }
        },requireContext(),this)
    }

}