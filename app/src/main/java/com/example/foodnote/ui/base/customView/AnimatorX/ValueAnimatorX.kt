package com.example.foodnote.ui.base.customView.AnimatorX

class ValueAnimatorX(var x1: Float, var x2: Float) {

    private lateinit var vectorFunction : (Float) -> Float
    private lateinit var renderFunction : (Float) -> Unit

    private var saveTime = 0L
    private var flag = true

    var vector = 1
    var currentX = x1

    private fun valueAnimator() {
        if(flag) {
            saveTime = System.currentTimeMillis()
            flag = false
        }
        val currentTime = System.currentTimeMillis()
        val deltaTime = currentTime - saveTime

        boundsValue(deltaTime)

        saveTime = currentTime
    }

    private fun boundsValue(deltaTime: Long) {

        val speed = vectorFunction(currentX)
        currentX += speed * vector * (deltaTime / 1000f)

        if(currentX > x2) currentX = x2
        if(currentX < x1) currentX = x1
    }

    fun render() {
        valueAnimator()
        renderFunction(currentX)
    }

    fun ValueAnimatorX.vectorFunction(getSpeed : (Float) -> Float)  { vectorFunction = getSpeed }
    fun ValueAnimatorX.render(render: (Float) -> Unit)  { renderFunction = render }

    companion object {
        fun ofValue(x1: Float, x2: Float) = ValueAnimatorX(x1, x2)
    }
}