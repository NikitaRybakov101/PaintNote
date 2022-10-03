package com.example.foodnote.ui.profile

import android.os.Bundle
import android.text.Editable
import android.view.View
import com.example.foodnote.R
import com.example.foodnote.data.sharedPreference.SharedPreference
import com.example.foodnote.databinding.FragmentSettingsBinding
import com.example.foodnote.ui.activity.MainActivity
import com.example.foodnote.ui.baseViewBinding.BaseViewBindingFragment
import com.example.foodnote.utils.showToast

class ProfileSettings : BaseViewBindingFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {

    private val activity by lazy { requireActivity() as MainActivity }

    companion object {
        const val WEIGHT = "WEIGHT"
        const val HEIGHT = "HEIGHT"
        const val AGE = "AGE"
        const val MALE = "MALE"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadData()
        saveButton()
    }

    private fun saveButton() = with(binding)  {
        saveButton.setOnClickListener {
            saveInputText()
        }
    }

    private fun loadData() = with(binding){
        weightTextInputLayout.editText?.setText(SharedPreference.loadDataString(WEIGHT, activity))
        heightTextInputLayout.editText?.setText(SharedPreference.loadDataString(HEIGHT, activity))
        ageTextInputLayout.editText?.setText(SharedPreference.loadDataString(AGE, activity))

        if(SharedPreference.loadDataBoolean(MALE, requireActivity() as MainActivity)) {
            maleRadioButton.isChecked = true
        } else {
            femaleRadioButton.isChecked = true
        }
    }

    private fun saveInputText() = with(binding) {
        val weight = weightTextInput.text
        val height = heightTextInput.text
        val age = ageTextInput.text

        val boolMale = maleRadioButton.isChecked

        SharedPreference.saveDataBoolean(boolMale,MALE,activity)

        SharedPreference.saveData(weight.toString(),WEIGHT,activity)
        SharedPreference.saveData(height.toString(),HEIGHT, activity)
        SharedPreference.saveData(age.toString(),AGE, activity)

        context?.showToast(getString(R.string.save_successful))
    }

}