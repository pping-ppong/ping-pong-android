package com.pingpong_android.view.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.marginEnd
import androidx.recyclerview.widget.RecyclerView
import com.pingpong_android.databinding.ItemCalendarDateBinding
import java.util.*

class DayAdapter(val tempMonth:Int, val dayList: MutableList<Date>): RecyclerView.Adapter<DayAdapter.DayView>() {
    val ROW = 6

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayView {
        var binding = ItemCalendarDateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DayView(binding)
    }

    override fun onBindViewHolder(holder: DayView, position: Int) {
        holder.bind(dayList.get(position), position)
    }

    override fun getItemCount(): Int {
        return ROW * 7
    }

    inner class DayView(val binding: ItemCalendarDateBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(day : Date, position: Int) {
            if ((position+1)%7 == 0) {
                val params = binding.dateLayout.layoutParams as RecyclerView.LayoutParams
                params.marginEnd = 0
                binding.dateLayout.layoutParams = params
            } else {
                val params = binding.dateLayout.layoutParams as RecyclerView.LayoutParams
                params.marginEnd = 10
                binding.dateLayout.layoutParams = params
            }

            if(tempMonth != day.month) {
                binding.dateLayout.visibility = View.INVISIBLE
            } else {
                binding.dateLayout.visibility = View.VISIBLE

                binding.dateLayout.setOnClickListener {
                    Toast.makeText(binding.dateLayout.context, "${day}", Toast.LENGTH_SHORT).show()
                }
                binding.dayTv.text = day.date.toString()
            }
        }
    }

}
