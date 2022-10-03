package com.example.foodnote.ui.noteBook.helperView

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnticipateOvershootInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager

object ExpandView {

    private var elevation = 0f
    private var heightView = 0
    private var widthView = 0
    private var viewX = 0f
    private var viewY = 0f

    @SuppressLint("Recycle")
    fun expandView(view : View, root : ConstraintLayout) {

        elevation = view.elevation
        heightView = view.height
        widthView = view.width
        viewX = view.x
        viewY = view.y

        view.elevation = 80f
        ObjectAnimator.ofFloat(view,View.X,0f).setDuration(400).start()
        ObjectAnimator.ofFloat(view,View.Y,0f).setDuration(400).start()

        animation(root)
        animationExpanded(view)
    }

    fun decreaseView(view : View, root : ConstraintLayout) {
        animation(root)
        animationDecrease(view)
    }

    private fun animation(root : ConstraintLayout, timeAnimation : Long = 800, startDelay : Long = 0) {
        val transition = ChangeBounds()
        transition.duration     = timeAnimation
        transition.interpolator = AnticipateOvershootInterpolator(0f)
        transition.startDelay   = startDelay

        TransitionManager.beginDelayedTransition(root, transition)
    }

    private fun animationExpanded(view: View) {
        view.layoutParams = view.layoutParams.apply {
            height = ViewGroup.LayoutParams.MATCH_PARENT
            width  = ViewGroup.LayoutParams.MATCH_PARENT
        }
    }

    private fun animationDecrease(view: View) {
        view.layoutParams = view.layoutParams.apply {
            width  = widthView
            height  = heightView
        }

        ObjectAnimator.ofFloat(view,View.X,viewX).setDuration(400).apply {
            startDelay = 600
            start()
        }
        ObjectAnimator.ofFloat(view,View.Y,viewY).setDuration(400).apply {
            startDelay = 600
            start()
        }
        view.elevation = elevation
    }

}