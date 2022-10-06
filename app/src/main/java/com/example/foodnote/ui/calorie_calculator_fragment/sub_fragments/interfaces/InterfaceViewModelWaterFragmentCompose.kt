package com.example.foodnote.ui.calorie_calculator_fragment.sub_fragments.interfaces

import androidx.lifecycle.MutableLiveData
import com.example.foodnote.ui.noteBook.stateData.StateDataCompose

interface InterfaceViewModelWaterFragmentCompose {
    fun getLiveData() : MutableLiveData<StateDataCompose>
    fun setWaterMill(currentWaterValue : Int, maxWaterValue : Int)
}