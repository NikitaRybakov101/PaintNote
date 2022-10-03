package com.example.foodnote.ui.calorie_calculator_fragment.sub_fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodnote.ui.calorie_calculator_fragment.sub_fragments.InterfaceViewModelWaterFragmentCompose
import com.example.foodnote.ui.noteBook.stateData.StateDataCompose

class ViewModelWaterFragmentCompose : ViewModel() , InterfaceViewModelWaterFragmentCompose {

    private val liveData = MutableLiveData<StateDataCompose>()
    override fun getLiveData() = liveData

    override fun setWaterMill(currentWaterValue : Int, maxWaterValue : Int) {
        liveData.value = StateDataCompose.Success(currentWaterValue, maxWaterValue)
    }
}