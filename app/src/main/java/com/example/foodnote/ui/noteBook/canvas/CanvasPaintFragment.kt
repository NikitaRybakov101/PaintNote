package com.example.foodnote.ui.noteBook.canvas

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.Color.RED
import android.graphics.Color.WHITE
import android.os.Bundle
import android.os.Environment
import android.util.DisplayMetrics
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import com.example.foodnote.R
import com.example.foodnote.databinding.CanvasFragmentBinding
import com.example.foodnote.ui.baseViewBinding.BaseViewBindingFragment
import com.example.foodnote.ui.noteBook.constNote.Const
import com.example.foodnote.ui.noteBook.constNote.Const.CARD_NOTE_SPLASH_DP
import com.example.foodnote.ui.noteBook.constNote.Const.DEFAULT_ALPHA
import com.example.foodnote.ui.noteBook.constNote.Const.DEFAULT_SIZE_BRUSH
import com.example.foodnote.ui.noteBook.constNote.Const.MARGIN_CANVAS_DP
import com.example.foodnote.ui.noteBook.constNote.Const.MAX_CANVAS_HEIGHT_DP
import com.example.foodnote.ui.noteBook.constNote.Const.SEED
import com.example.foodnote.ui.noteBook.constNote.ConstType
import com.example.foodnote.ui.noteBook.editorNote.EditorPaintNoteFragment
import com.google.android.material.card.MaterialCardView
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.random.Random

class CanvasPaintFragment : BaseViewBindingFragment<CanvasFragmentBinding>(CanvasFragmentBinding::inflate) {

    private var colorCard = WHITE
    private var saveColorCard = WHITE
    private var colorCardBackground = RED

    private lateinit var viewCanvasPaint : CanvasPaint
    private lateinit var fragment: EditorPaintNoteFragment
    private lateinit var listView: List<MaterialCardView>

    private var canvasHeight : Int = 0
    private var canvasWidth : Int = 0

    private var widthScreen = 0
    private var heightScreen = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        setWidthPixels()
        setSizeCanvas()

        chekColor()
        chekBrush()
        buttonChek()
    }

    private fun init() {
        viewCanvasPaint = CanvasPaint(requireContext(),colorCardBackground,binding.pic)
        binding.viewCanvasContainer.addView(viewCanvasPaint)
        binding.viewCanvas.setCardBackgroundColor(colorCardBackground)

        binding.palette.setFragment(this@CanvasPaintFragment)

        binding.apply {
           listView = listOf(colorBlue,colorPink,colorYellow,colorGray,colorWhite,colorBlack,colorPurple,colorGreen,colorMulti)
        }
    }

    private fun setWidthPixels() {
        val metrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(metrics)
        widthScreen = metrics.widthPixels
        heightScreen = metrics.heightPixels
    }

    private fun setSizeCanvas() {
        val heightScreen = convertDpToPixels(MAX_CANVAS_HEIGHT_DP)
        val margin = convertDpToPixels(MARGIN_CANVAS_DP)

        if(canvasHeight.toFloat()/canvasWidth.toFloat() <= heightScreen/(widthScreen.toFloat() - margin)) {

            binding.viewCanvas.updateLayoutParams {
                height = (((widthScreen - margin) * canvasHeight.toFloat()) / canvasWidth.toFloat()).toInt() + convertDpToPixels(CARD_NOTE_SPLASH_DP)
                width = (widthScreen - margin)
            }
        }
        else {
            binding.viewCanvas.updateLayoutParams {
                height = heightScreen + convertDpToPixels(CARD_NOTE_SPLASH_DP)
                width = ((heightScreen * canvasWidth.toFloat()) / canvasHeight.toFloat()).toInt()
            }
        }
    }

    private fun convertDpToPixels( dp: Int) = (dp * requireContext().resources.displayMetrics.density).toInt()

    private fun chekColor() {
        listView.forEach { view ->
            view.setOnClickListener {

                clearStroke(listView)

                view.strokeColor = Color.GRAY
                view.strokeWidth = Const.STROKE_WIDTH_FOCUS

                colorCard = if(view.id == R.id.colorMulti) {
                    saveColorCard
                } else {
                    view.backgroundTintList?.defaultColor ?: WHITE
                }
                viewCanvasPaint.setColor(colorCard)
            }
        }
    }

    private fun chekBrush() = with(binding){
        brushPen.strokeColor = Color.GRAY
        brushPen.strokeWidth = Const.STROKE_WIDTH_FOCUS

        brushPen.setOnClickListener {
            clearStroke(listOf(brushCircles))

            viewCanvasPaint.setBrush(ConstType.BRUSH_PEN)
            brushPen.strokeColor = Color.GRAY
            brushPen.strokeWidth = Const.STROKE_WIDTH_FOCUS
        }
        brushCircles.setOnClickListener {
            clearStroke(listOf(brushPen))

            viewCanvasPaint.setBrush(ConstType.BRUSH_CIRCLES)
            brushCircles.strokeColor = Color.GRAY
            brushCircles.strokeWidth = Const.STROKE_WIDTH_FOCUS
        }
    }

    private fun clearStroke(listView: List<MaterialCardView>) {
        listView.forEach { e ->
            e.strokeColor = Color.LTGRAY
            e.strokeWidth = Const.STROKE_WIDTH
        }
    }

    fun setColorPic(color: Int) = with(binding.colorMulti){
        setBackgroundColor(color)
        saveColorCard = color

        clearStroke(listView)
        strokeColor = Color.GRAY
        strokeWidth = Const.STROKE_WIDTH_FOCUS

        colorCard = color
        viewCanvasPaint.setColor(colorCard)
    }

    private fun buttonChek() = with(binding){
        clearCanvas.setOnClickListener {
            deleteDialog()
        }
        saveButton.setOnClickListener {
            saveImage()
        }
        cancelButton.setOnClickListener {
            setEmptyFragment()
            fragment.setFlagAnBlockButton()
        }
        colorMultiPic.visibility = View.GONE

        buttonOk.setOnClickListener {
            colorMultiPic.visibility = View.GONE
        }
        colorSelection.setOnClickListener {
            colorMultiPic.visibility = View.VISIBLE
        }
        pic.setOnClickListener {
            viewCanvasPaint.setPic(true)
        }
        seekBarAlpha.setOnSeekBarChangeListener(seekBarChangeListener)
        seekBarPixSize.setOnSeekBarChangeListener(seekBarChangeListener)
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener)

        seekBarAlpha.progress = DEFAULT_ALPHA
        seekBarPixSize.progress = DEFAULT_SIZE_BRUSH

        binding.textViewAlpha.text = getString(R.string.alpha_string) + " " + DEFAULT_ALPHA
        binding.textViewSizeBrash.text = getString(R.string.pix_string) + " " + DEFAULT_SIZE_BRUSH
    }

    private val seekBarChangeListener: OnSeekBarChangeListener = object : OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {

            when (seekBar.id){
                R.id.seekBar -> {
                    binding.palette.setColorH(progress)
                }
                R.id.seekBarAlpha -> {
                    viewCanvasPaint.setAlphaColor(progress)
                    binding.textViewAlpha.text = getString(R.string.alpha_string) + " " + progress
                }
                R.id.seekBarPixSize -> {
                    viewCanvasPaint.setSize(progress.toFloat())
                    binding.textViewSizeBrash.text = getString(R.string.pix_string) + " " + progress
                }
            }
        }
        override fun onStartTrackingTouch(seekBar: SeekBar) {}
        override fun onStopTrackingTouch(seekBar: SeekBar) {}
    }

    private fun deleteDialog() {
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setTitle(getString(R.string.clearCanvas_mess))
        dialog.setCancelable(true)

        dialog.setNegativeButton(getString(R.string.no)) { dialog, _ ->
            dialog.dismiss()
        }
        dialog.setPositiveButton(getString(R.string.yes)) { dialog, _ ->
            viewCanvasPaint.clearCanvas()
            dialog.dismiss()
        }
        dialog.create().show()
    }

    private fun setEmptyFragment() {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.anim_layout_2,R.anim.anim_layout)
            .replace(R.id.containerCanvas, Fragment()).commit()
    }

    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { saveImage() }
    private fun requestLocationPermissions() = permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    private fun saveImage() {

        if(ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            val bitmap = viewCanvasPaint.getBitmap()
            bitmapToFile(bitmap, getRandomName())
            setEmptyFragment()
        } else {
            requestLocationPermissions()
        }
    }

    private fun getRandomName() = "imageBase${Random(SEED)}.png"

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

            fragment.loadImage(fileNameToSave)
            fragment.setFlagAnBlockButton()

            file
        } catch (e: Exception) {
            e.printStackTrace()
            file
        }
    }

    companion object {
        fun newInstance(fragment: EditorPaintNoteFragment, height: Int, width: Int, color : Int) : CanvasPaintFragment {
            val canvasPaint = CanvasPaintFragment()
            canvasPaint.canvasHeight = height
            canvasPaint.canvasWidth = width
            canvasPaint.fragment = fragment
            canvasPaint.colorCardBackground = color

            return canvasPaint
        }
    }
}
