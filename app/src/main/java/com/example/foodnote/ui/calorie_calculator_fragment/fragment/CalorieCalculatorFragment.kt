package com.example.foodnote.ui.calorie_calculator_fragment.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.foodnote.R
import com.example.foodnote.databinding.FragmentCalorieCalculatorBinding
import com.example.foodnote.ui.activity.MainActivity
import com.example.foodnote.ui.baseViewBinding.BaseViewBindingFragment
import com.example.foodnote.ui.calorie_calculator_fragment.sub_fragments.CalculatorCalories
import com.example.foodnote.ui.calorie_calculator_fragment.sub_fragments.CircleFragment
import com.example.foodnote.ui.calorie_calculator_fragment.sub_fragments.WaterFragmentCompose
import com.example.foodnote.utils.hide
import com.example.foodnote.utils.show

class CalorieCalculatorFragment : BaseViewBindingFragment<FragmentCalorieCalculatorBinding>(FragmentCalorieCalculatorBinding::inflate) {

    data class NotesCalories(val header : String, val calories : Int, val fat : Int, val protein : Int, val water : Int)
    private val listNotes = ArrayList<NotesCalories>()

    private val circleFragment = CircleFragment()
    private val waterFragment = WaterFragmentCompose()

    private val adapterListNotes by lazy { RecyclerAdapter(listNotes , circleFragment, waterFragment) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initListNotes()
        floatButton()
    }

    private fun initView() {
        initMaxCardCalories()
        initPager()
    }

    private fun initMaxCardCalories() {
        val maxCalories = CalculatorCalories.getMaxCalories(requireActivity() as MainActivity)

        binding.maxCard.elevation = 60f
        binding.maxDataText.text = getString(R.string.max_cal) + " " + maxCalories + " / " + getString(R.string.max_water)
    }

    private fun initListNotes() = with(binding) {
        recycler.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)
        recycler.adapter = adapterListNotes
    }

    private fun floatButton() = with(binding)  {
        addButton.setOnClickListener {
            createNotes()
        }
    }

    private fun createNotes() = with(binding) {
        createNotes.elevation = 200f
        createNotes.show()

        saveButton.setOnClickListener {

            val cal = editCal.text.toString().toInt()
            val fat = editFat.text.toString().toInt()
            val protein = editProtein.text.toString().toInt()
            val water = editWater.text.toString().toInt()

            listNotes.add(NotesCalories(getString(R.string.notes_calories),cal,fat,protein,water))
            recyclerUpdate()
            createNotes.hide()
        }
    }

    private fun recyclerUpdate() = with(binding) {
        adapterListNotes.notifyItemInserted(listNotes.size)
        recycler.scrollToPosition(adapterListNotes.itemCount - 1)
    }

    private fun initPager() = with(binding) {
        pager.adapter = TotalViewAdapter(this@CalorieCalculatorFragment, listOf(circleFragment,waterFragment))

        diagrams.elevation = 20f
        buttonRight.setOnClickListener {
            if(pager.currentItem != 1) {
                pager.setCurrentItem(1, true)
                customTextView.setText(
                    resources.getString(
                        R.string.water
                    )
                )
            }
        }

        pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if(position == 0) {
                    customTextView.setText(resources.getString(R.string.calorie))
                } else {
                    customTextView.setText(
                        resources.getString(
                            R.string.water
                        )
                    )
                }
            }
        })

        buttonLeft.setOnClickListener {
            if(pager.currentItem != 0) {
                pager.setCurrentItem(0, true)
                customTextView.setText(resources.getString(R.string.calorie))
            }
        }
    }

}