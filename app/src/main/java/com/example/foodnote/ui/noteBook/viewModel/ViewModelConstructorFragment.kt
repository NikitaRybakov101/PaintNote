package com.example.foodnote.ui.noteBook.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodnote.ui.noteBook.stateData.StateData
import com.example.foodnote.ui.noteBook.viewModel.VievModelInterfaces.ViewModelConstructorInterface
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ViewModelConstructorFragment : ViewModel() , ViewModelConstructorInterface {

    private val liveData = MutableLiveData<StateData>()

    override fun getLiveData() = liveData

    override fun sendServerToCal(foods : List<String>, weights : List<String>) {

        viewModelScope.launch {
            kotlin.runCatching {

                var sum = 0
                weights.forEach { e -> sum += e.toInt() }
                "$sum gm"

            }.onSuccess {
                liveData.value = StateData.Success(it)
            }.onFailure {
                liveData.value = StateData.Error(Throwable("NetWork ERROR"))
            }
        }
    }

}