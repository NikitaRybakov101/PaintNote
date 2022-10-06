package com.example.foodnote.ui.calorie_calculator_fragment.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodnote.data.databaseRoom.entities.EntitiesNotesCalories
import com.example.foodnote.databinding.ItemRecyclerBinding
import com.example.foodnote.ui.activity.MainActivity
import com.example.foodnote.ui.calorie_calculator_fragment.sub_fragments.CalculatorCalories
import com.example.foodnote.ui.calorie_calculator_fragment.sub_fragments.CircleFragment
import com.example.foodnote.ui.calorie_calculator_fragment.sub_fragments.WaterFragmentCompose

class RecyclerAdapter(
    private val notesCalories: ArrayList<EntitiesNotesCalories>,
    private val circleFragment: CircleFragment,
    private val waterFragmentCompose: WaterFragmentCompose,
    private val calorieCalculatorFragment: CalorieCalculatorFragment
) : RecyclerView.Adapter<RecyclerAdapter.NewViewHolderNotes>() {

    private var _binding : ItemRecyclerBinding? = null
    private val binding : ItemRecyclerBinding get() = _binding!!

    private val activity by lazy {
        circleFragment.requireActivity() as MainActivity
    }

    private val maxCalories by lazy { CalculatorCalories.getMaxCalories(activity) }
    private val maxProtein by lazy { CalculatorCalories.getMaxProtein(activity) }
    private val maxFats by lazy {  CalculatorCalories.getMaxFats(activity) }
    private val maxWater by lazy {  CalculatorCalories.getMaxWater(activity) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.NewViewHolderNotes {
        _binding = ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NewViewHolderNotes(binding.root)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.NewViewHolderNotes, position: Int) {
        val notes = notesCalories[position]

        holder.textHeader.text = notes.header
        holder.itemRemoved()

        holder.textCalories.text = "${notes.calories} cal / ${notes.fat} fat / ${notes.protein} protein / ${notes.water} water"

        holder.notesCaloriesCard.setOnClickListener {
            circleFragment.startDiagrams(notes.calories,maxCalories,notes.fat,maxFats,notes.protein,maxProtein)
            waterFragmentCompose.setDataViewModel(notes.water,maxWater)
        }
    }

    override fun getItemCount(): Int {
        return notesCalories.size
    }

    inner class NewViewHolderNotes(view : View) : RecyclerView.ViewHolder(view) , InterfaceViewHolderNotes {
        val textHeader = binding.textHeader
        val textCalories = binding.calories
        val notesCaloriesCard = binding.notesCaloriesCard
        private val buttonDelete = binding.buttonDelete

        override fun itemRemoved(){
            buttonDelete.setOnClickListener {
                calorieCalculatorFragment.removedDataNotes(notesCalories[layoutPosition])

                notesCalories.removeAt(layoutPosition)
                notifyItemRemoved(layoutPosition)
            }
        }
    }

    interface InterfaceViewHolderNotes {
        fun itemRemoved()
    }
}