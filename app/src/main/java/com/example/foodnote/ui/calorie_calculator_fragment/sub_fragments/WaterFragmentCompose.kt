package com.example.foodnote.ui.calorie_calculator_fragment.sub_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.foodnote.ui.base.customView.AnimatorX.ValueFunctionX
import com.example.foodnote.ui.noteBook.stateData.StateDataCompose
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.cos
import kotlin.math.sin

class WaterFragmentCompose : Fragment() {

    private val viewModel : ViewModelWaterFragmentCompose by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                StateCompose()
            }
        }
    }

    private val message = mutableStateOf("0")

    fun setDataViewModel(current : Int,maxWater : Int) {
        this.currentWaterValue = current
        this.maxWaterValue = maxWater

        message.value = "$currentWaterValue/$maxWaterValue милл"
    }

    @Preview
    @Composable
    fun StateCompose() {
        Card(message)

        ChangeState(message)
    }

    @Composable
    fun ChangeState(message: MutableState<String>) {

        viewModel.getLiveData().observe(viewLifecycleOwner) { stateData ->
            when(stateData) {
                is StateDataCompose.Success -> {

                    this.currentWaterValue = stateData.currentValue
                    this.maxWaterValue = stateData.maxValue

                    message.value = "$currentWaterValue/$maxWaterValue милл"
                }
                else -> {}
            }
        }
    }

    @Composable
    fun Card(name: MutableState<String>) {
        ConstraintLayout(modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)) {
            val (card) = createRefs()

            androidx.compose.material.Card(modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(horizontal = 18.dp)
                .constrainAs(card) {

                    bottom.linkTo(parent.bottom)
                    top.linkTo(parent.top)
                },

                backgroundColor = Color(212, 248, 245),
                elevation = 5.dp,
                shape = RoundedCornerShape(50.dp),
                border = BorderStroke(2.dp, Color.LightGray)
            ) {
                AnimatedWater()
                DrawText(name)
            }
        }
    }

    @Composable
    fun AnimatedWater() {
        val transition = rememberInfiniteTransition()
        val parameter = transition.animateFloat(
            initialValue = 0f,
            targetValue = 200f,
            animationSpec = infiniteRepeatable(animation = tween(durationMillis = 100000, easing = LinearEasing)),
        )
        Canvas(parameter.value)
    }

    @Composable
    fun DrawText(name: MutableState<String>) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (text) = createRefs()

            Text(text = name.value, color = Color.White, fontSize = 20.sp, modifier = Modifier
                .padding()
                .constrainAs(text) {
                    bottom.linkTo(parent.bottom)
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
        }
    }

    private val valueFunctionX = ValueFunctionX()

    private var tempY = 0f
    private var stopX = 3.2f
    private var startX = -0.1f
    private var amplitudeScale = 60f/263f
    private var maxWaterValue = 0
    private var currentWaterValue = 0

    @Composable
    fun Canvas(parameter: Float) {

       androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
            val maxWaterH = ((size.width * currentWaterValue) / maxWaterValue)

            valueFunctionX.apply {
                parametricFunction { x, a, h, amplitude -> (0.5f * (x - 2f) * cos(x + a) * sin(x + a)) * amplitude + h }
                render { value, x -> if(x != tempY) {
                        drawLine(
                            start = Offset(x = 0f, y = x),
                            end = Offset(x = value, y = x),
                            color = Color(84, 210, 235),
                            strokeWidth = 5F)
                    }
                    tempY = x
                }
            }.startFunction(startX, stopX, size.height.toInt(), parameter, maxWaterH, size.height * amplitudeScale)
        })
    }
}