package com.example.foodnote.ui.noteBook.viewModel.VievModelInterfaces

import androidx.lifecycle.MutableLiveData
import com.example.foodnote.ui.noteBook.stateData.StateData

interface ViewModelConstructorInterface {
    fun getLiveData() : MutableLiveData<StateData>
    fun sendServerToCal(foods : List<String>, weights : List<String>)
}