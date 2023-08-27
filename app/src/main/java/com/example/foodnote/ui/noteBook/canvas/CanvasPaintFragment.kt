package com.example.foodnote.ui.noteBook.canvas

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Color.WHITE
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
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
import com.example.foodnote.di.VIEW_MODEL_CANVAS
import com.example.foodnote.ui.baseViewBinding.BaseViewBindingFragment
import com.example.foodnote.ui.noteBook.canvas.viewModel.CanvasPaintFragmentViewModel
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
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.random.Random


class CanvasPaintFragment : BaseViewBindingFragment<CanvasFragmentBinding>(CanvasFragmentBinding::inflate) {

    private val viewModel : CanvasPaintFragmentViewModel by viewModel(named(VIEW_MODEL_CANVAS))

    private var colorCard = WHITE
    private var saveColorCard = WHITE
    private var colorCardBackground = WHITE

    private var viewCanvasPaint: CanvasPaint? = null
    private lateinit var fragment: EditorPaintNoteFragment
    private lateinit var listView: List<MaterialCardView>

    private var canvasHeight: Int = 0
    private var canvasWidth: Int = 0

    private var widthScreen = 0
    private var heightScreen = 0

    private var bitmap: Bitmap? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        setWidthPixels()
        setSizeCanvas()

        checkColor()
        checkBrush()
        buttonCheck()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init() {
        if(viewModel.viewCanvasPaint == null) {
            viewCanvasPaint = viewModel.getCanvas(requireContext(),colorCardBackground,binding.pic)

            binding.viewCanvasContainer.addView(viewCanvasPaint)
            binding.viewCanvas.setCardBackgroundColor(colorCardBackground)

            viewModel.canvasWidth = canvasWidth
            viewModel.canvasHeight = canvasHeight
            viewModel.colorCardBackground = colorCardBackground
            viewModel.fragmentEditor = fragment
        } else {
            viewCanvasPaint = viewModel.getCanvas(requireContext(),colorCardBackground,binding.pic)!!

            (viewCanvasPaint!!.parent as ViewGroup).removeView(viewCanvasPaint)

            binding.viewCanvasContainer.addView(viewCanvasPaint)
            binding.viewCanvas.setCardBackgroundColor(colorCardBackground)

            canvasWidth = viewModel.canvasWidth!!
            canvasHeight = viewModel.canvasHeight!!
            colorCardBackground = viewModel.colorCardBackground!!
            fragment = viewModel.fragmentEditor!!
        }


        binding.palette.setFragment(this@CanvasPaintFragment)

        binding.apply {
           listView = listOf(colorBlue,colorPink,colorYellow,colorGray,colorWhite,colorBlack,colorPurple,colorGreen,colorMulti)
        }

        binding.root.setOnTouchListener { _, _ -> true }
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

    private fun checkColor() {
        listView.forEach { view ->
            view.setOnClickListener {

                clearStroke(listView)

                view.strokeColor = Color.DKGRAY
                view.strokeWidth = Const.STROKE_WIDTH_FOCUS

                colorCard = if(view.id == R.id.colorMulti) {
                    saveColorCard
                } else {
                    view.backgroundTintList?.defaultColor ?: WHITE
                }
                viewCanvasPaint?.setColor(colorCard)
            }
        }
    }

    private fun checkBrush() = with(binding){
        brushPen.strokeColor = Color.DKGRAY
        brushPen.strokeWidth = Const.STROKE_WIDTH_FOCUS

        brushPen.setOnClickListener {
            clearStroke(listOf(brushCircles))

            viewCanvasPaint?.setBrush(ConstType.BRUSH_PEN)
            brushPen.strokeColor = Color.DKGRAY
            brushPen.strokeWidth = Const.STROKE_WIDTH_FOCUS
        }
        brushCircles.setOnClickListener {
            clearStroke(listOf(brushPen))

            viewCanvasPaint?.setBrush(ConstType.BRUSH_CIRCLES)
            brushCircles.strokeColor = Color.DKGRAY
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
        viewCanvasPaint?.setColor(colorCard)
    }

    @SuppressLint("SetTextI18n")
    private fun buttonCheck() = with(binding){
        clearCanvas.setOnClickListener {
            deleteDialog()
        }
        saveButton.setOnClickListener {
            checkPermission()
        }
        cancelButton.setOnClickListener {
            cancelDialog()
        }
        colorMultiPic.visibility = View.GONE

        buttonOk.setOnClickListener {
            colorMultiPic.visibility = View.GONE
        }
        colorSelection.setOnClickListener {
            colorMultiPic.visibility = View.VISIBLE
        }
        pic.setOnClickListener {
            viewCanvasPaint?.setPic(true)
        }
        backDraw.setOnClickListener {
            viewCanvasPaint?.backDraw()
        }
        upDraw.setOnClickListener {
            viewCanvasPaint?.upDraw()
        }
        seekBarAlpha.setOnSeekBarChangeListener(seekBarChangeListener)
        seekBarPixSize.setOnSeekBarChangeListener(seekBarChangeListener)
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener)

        seekBarAlpha.progress = DEFAULT_ALPHA/2
        seekBarPixSize.progress = DEFAULT_SIZE_BRUSH

        binding.textViewAlpha.text = getString(R.string.alpha_string) + " " + DEFAULT_ALPHA
        binding.textViewSizeBrash.text = getString(R.string.pix_string) + " " + DEFAULT_SIZE_BRUSH
    }

    private val seekBarChangeListener: OnSeekBarChangeListener = object : OnSeekBarChangeListener {
        @SuppressLint("SetTextI18n")
        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {

            when (seekBar.id){
                R.id.seekBar -> {
                    binding.palette.setColorH(progress)
                }
                R.id.seekBarAlpha -> {
                    val progressScale = progress * 2
                    viewCanvasPaint?.setAlphaColor(progressScale)
                    binding.textViewAlpha.text = getString(R.string.alpha_string) + " " + progressScale
                }
                R.id.seekBarPixSize -> {
                    viewCanvasPaint?.setSize(progress.toFloat())
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
            viewCanvasPaint?.clearCanvas()
            dialog.dismiss()
        }
        dialog.create().show()
    }

    private fun cancelDialog() {
        setEmptyFragment()
    }

    private fun setEmptyFragment() {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.anim_layout_2,R.anim.anim_layout)
            .replace(R.id.containerCanvas, Fragment()).commit()
    }

    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
        if(result) {
            saveImage()
        } else {
            postInfoPermission()
        }
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                postInfoPermission()
            } else {
                saveImage()
            }
        } else {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                postInfoPermission()
            } else {
                saveImage()
            }
        }
    }

    private fun saveImage() {
        val bitmap = viewCanvasPaint?.getBitmap()
        if (bitmap != null) {
            fragment.loadImage(bitmap.copy(Bitmap.Config.ARGB_8888,false))
        }
        setEmptyFragment()
    }

    private fun postInfoPermission() {
        Toast.makeText(context, "Permission to read and write files is required", Toast.LENGTH_SHORT).show()
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
