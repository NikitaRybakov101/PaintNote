package com.example.foodnote.ui.calorie_calculator_fragment.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.foodnote.R
import com.example.foodnote.databinding.ItemRecyclerBinding
import com.example.foodnote.ui.activity.MainActivity
import com.example.foodnote.ui.calorie_calculator_fragment.sub_fragments.CalculatorCalories
import com.example.foodnote.ui.calorie_calculator_fragment.sub_fragments.CircleFragment
import com.example.foodnote.ui.calorie_calculator_fragment.sub_fragments.ViewModelWaterFragmentCompose
import com.example.foodnote.ui.calorie_calculator_fragment.sub_fragments.WaterFragmentCompose

class RecyclerAdapter(private val data: ArrayList<CalorieCalculatorFragment.NotesCalories>, private val circleFragment: CircleFragment, private val waterFragmentCompose: WaterFragmentCompose) : RecyclerView.Adapter<RecyclerAdapter.NewViewHolderNotes>() {

    private var _binding : ItemRecyclerBinding? = null
    private val binding : ItemRecyclerBinding get() = _binding!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.NewViewHolderNotes {
        _binding = ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NewViewHolderNotes(binding.root)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.NewViewHolderNotes, position: Int) {
        val notes = data[position]

        holder.textHeader.text = notes.header
        holder.itemRemoved()

        holder.textCalories.text = "${notes.calories} cal / ${notes.fat} fat / ${notes.protein} protein / ${notes.water} water"

        holder.notesCaloriesCard.setOnClickListener {
            val maxCalories = CalculatorCalories.getMaxCalories(circleFragment.requireActivity() as MainActivity)

            circleFragment.startDiagrams(notes.calories,maxCalories,notes.fat,100,notes.protein,200)
            waterFragmentCompose.setDataViewModel(notes.water,2100)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class NewViewHolderNotes(view : View) : RecyclerView.ViewHolder(view) , InterfaceViewHolderNotes {
        val textHeader = binding.textHeader
        val textCalories = binding.calories
        val notesCaloriesCard = binding.notesCaloriesCard
        private val buttonDelete = binding.buttonDelete

        override fun itemRemoved(){
            buttonDelete.setOnClickListener {
                data.removeAt(layoutPosition)
                notifyItemRemoved(layoutPosition)
            }
        }
    }

    interface InterfaceViewHolderNotes {
        fun itemRemoved()
    }
}