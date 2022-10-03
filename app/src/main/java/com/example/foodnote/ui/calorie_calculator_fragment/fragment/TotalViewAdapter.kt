package com.example.foodnote.ui.calorie_calculator_fragment.fragment

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.foodnote.ui.calorie_calculator_fragment.sub_fragments.CircleFragment
import com.example.foodnote.ui.calorie_calculator_fragment.sub_fragments.WaterFragmentCompose

class TotalViewAdapter(fragment: Fragment, private val fragments : List<Fragment>) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = fragments.size
    override fun createFragment(position: Int) =
        when (position) {
            0 -> fragments[0]
            1 -> fragments[1]
            else -> fragments[0]
        }
}