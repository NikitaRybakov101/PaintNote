package com.example.foodnote.ui.calorie_calculator_fragment.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.foodnote.R
import com.example.foodnote.data.databaseRoom.dao.DaoDbNotesCalories
import com.example.foodnote.data.databaseRoom.entities.EntitiesNotesCalories
import com.example.foodnote.databinding.FragmentCalorieCalculatorBinding
import com.example.foodnote.di.DATA_BASE_CALORIES
import com.example.foodnote.ui.activity.MainActivity
import com.example.foodnote.ui.baseViewBinding.BaseViewBindingFragment
import com.example.foodnote.ui.calorie_calculator_fragment.sub_fragments.CalculatorCalories
import com.example.foodnote.ui.calorie_calculator_fragment.sub_fragments.CircleFragment
import com.example.foodnote.ui.calorie_calculator_fragment.sub_fragments.WaterFragmentCompose
import com.example.foodnote.utils.hide
import com.example.foodnote.utils.show
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

class CalorieCalculatorFragment : BaseViewBindingFragment<FragmentCalorieCalculatorBinding>(FragmentCalorieCalculatorBinding::inflate) , InterfaceCallbackCaloriesFragment {

    private var listNotes = ArrayList<EntitiesNotesCalories>()
    private val adapterListNotes by lazy { RecyclerAdapter(listNotes , circleFragment, waterFragment, this) }

    private val circleFragment = CircleFragment()
    private val waterFragment = WaterFragmentCompose()

    private val scope = CoroutineScope(Dispatchers.IO)
    private val notesDaoCalories: DaoDbNotesCalories by inject(named(DATA_BASE_CALORIES)) { parametersOf(requireActivity()) }


    private companion object {
        const val CALORIES = 0
        const val WATER = 1
        const val ELEVATION_CARD_CREATE_NOTE = 200f
        const val DIAGRAM_ELEVATION = 20f
        const val MAX_CARD_ELEVATION = 60f
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadNotes()
    }

    private fun loadNotes() {
        scope.launch {
            listNotes = notesDaoCalories.getAllNotesCalories() as ArrayList<EntitiesNotesCalories>

            withContext(Dispatchers.Main) {
                initView()
            }
        }
    }

    private fun initView() {
        initMaxCardCalories()
        initPager()
        initListNotes()
        floatButton()
    }

    private fun initMaxCardCalories() {
        val maxCalories = CalculatorCalories.getMaxCalories(requireActivity() as MainActivity)
        val maxWater = CalculatorCalories.getMaxWater(requireActivity() as MainActivity)

        binding.maxCard.elevation = MAX_CARD_ELEVATION
        binding.maxDataText.text = getString(R.string.max_cal) + " " + maxCalories + " / " + getString(R.string.max_water) + " " + maxWater

        waterFragment.setDataViewModel(0,maxWater)
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
        createNotes.elevation = ELEVATION_CARD_CREATE_NOTE
        createNotes.show()

        saveButton.setOnClickListener {

            var cal = editCal.text.toString()
            if(cal.isEmpty()) { cal = "0"}
            var fat = editFat.text.toString()
            if(fat.isEmpty()) { fat = "0"}
            var protein = editProtein.text.toString()
            if(protein.isEmpty()) { protein = "0"}
            var water = editWater.text.toString()
            if(water.isEmpty()) { water = "0"}

            val header = nameHeaderNote.text.toString()

            val note = EntitiesNotesCalories(
                header =  header,
                calories = cal.toInt(),
                fat = fat.toInt(),
                protein = protein.toInt(),
                water = water.toInt())

            listNotes.add(note)
            recyclerUpdate()
            saveDataNotes(note)

            createNotes.hide()
        }
    }

    private fun recyclerUpdate() = with(binding) {
        adapterListNotes.notifyItemInserted(listNotes.size)
        recycler.scrollToPosition(adapterListNotes.itemCount - 1)
    }

    private fun initPager() = with(binding) {
        pager.adapter = TotalViewAdapter(this@CalorieCalculatorFragment, listOf(circleFragment,waterFragment))

        diagrams.elevation = DIAGRAM_ELEVATION
        buttonRight.setOnClickListener {
            if(pager.currentItem != WATER) {
                pager.setCurrentItem(WATER, true)
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
                if(position == CALORIES) {
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
            if(pager.currentItem != CALORIES) {
                pager.setCurrentItem(CALORIES, true)
                customTextView.setText(resources.getString(R.string.calorie))
            }
        }
    }

    private fun saveDataNotes(note: EntitiesNotesCalories) {
        scope.launch {
            notesDaoCalories.insertNote(note)
        }
    }

    override fun removedDataNotes(note: EntitiesNotesCalories) {
        scope.launch {
            notesDaoCalories.deleteNoteCalories(note.header)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}