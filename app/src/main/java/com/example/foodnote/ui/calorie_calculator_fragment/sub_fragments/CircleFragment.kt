package com.example.foodnote.ui.calorie_calculator_fragment.sub_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodnote.databinding.CircleLayoutBinding
import com.example.foodnote.ui.activity.MainActivity
import com.example.foodnote.ui.baseViewBinding.BaseViewBindingFragment

class CircleFragment : BaseViewBindingFragment<CircleLayoutBinding>(CircleLayoutBinding::inflate) , InterfaceCircleFragment {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val maxCalories = CalculatorCalories.getMaxCalories(requireActivity() as MainActivity)
        startDiagrams(0,maxCalories,0,100,0,200)
    }

    override fun startDiagrams(x1 : Int, maxCalories : Int, x2 : Int, maxFats :Int, x3 : Int, maxProtein : Int) {
        binding.circleContainerDiagramView.start(x1, maxCalories, x2, maxFats, x3, maxProtein)
    }
}