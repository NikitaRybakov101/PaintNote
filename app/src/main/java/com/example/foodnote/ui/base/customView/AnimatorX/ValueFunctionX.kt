package com.example.foodnote.ui.base.customView.AnimatorX

import kotlin.math.round

class ValueFunctionX {
    private lateinit var functionX : (Float,Float,Float,Float) -> Float
    private lateinit var renderFunction : (Float,Float) -> Unit

    fun startFunction(startX : Float, stopX : Float, length : Int, paramA : Float, paramB : Float, paramC : Float) {
        var x = startX
        while (x < stopX) {

            val value = functionX(x,paramA,paramB,paramC)

            val xs = round((x * length) / stopX)
            render(value,xs)

            x += stopX/length
        }
    }

    private fun render(value: Float, x: Float) {
        renderFunction(value,x)
    }

    fun ValueFunctionX.parametricFunction(function : (Float, Float, Float, Float) -> Float)  { functionX = function }
    fun ValueFunctionX.render(render: (Float, Float) -> Unit)  { renderFunction = render }
}