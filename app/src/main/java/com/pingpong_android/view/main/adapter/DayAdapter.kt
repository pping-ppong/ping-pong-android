package com.pingpong_android.view.main.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.marginEnd
import androidx.recyclerview.widget.RecyclerView
import com.pingpong_android.R
import com.pingpong_android.databinding.ItemCalendarDateBinding
import com.pingpong_android.model.AchieveDTO
import com.pingpong_android.view.main.MainActivity
import java.util.*

class DayAdapter(
    val activity : MainActivity,
    val tempMonth : Int,
    val dayList: MutableList<Date>,
    val achieveList : MutableList<Double>,
    val picked_day : Int
): RecyclerView.Adapter<DayAdapter.DayView>() {

    val ROW = 6
    var last_month_days : Int = 0

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
//            if ((position+1)%7 == 0) {
//                val params = binding.dateLayout.layoutParams as RecyclerView.LayoutParams
//                params.marginEnd = 0
//                binding.dateLayout.layoutParams = params
//            } else {
//                val params = binding.dateLayout.layoutParams as RecyclerView.LayoutParams
//                params.marginEnd = 10
//                binding.dateLayout.layoutParams = params
//            }

            if(tempMonth != day.month) {
                binding.dateLayout.visibility = View.INVISIBLE
                last_month_days += 1
            } else {
                binding.dateLayout.visibility = View.VISIBLE

                // 달성율 표시
                binding.popColor.visibility = View.VISIBLE
                binding.icPop.visibility = View.GONE
                val num : Double = achieveList.get(position)
                if (num < 0) {
                    binding.popColor.setBackgroundResource(R.drawable.circle_0)
                    binding.mark.visibility = View.GONE
                } else if (num == 0.0) {
                    binding.popColor.setBackgroundResource(R.drawable.circle_0)
                    binding.mark.visibility = View.VISIBLE
                } else if (0 < num && num <= 15) {
                    binding.popColor.setBackgroundResource(R.drawable.circle_15)
                    binding.mark.visibility = View.VISIBLE
                } else if (15 < num && num <= 30) {
                    binding.popColor.setBackgroundResource(R.drawable.circle_30)
                    binding.mark.visibility = View.VISIBLE
                } else if (30 < num && num <= 45) {
                    binding.popColor.setBackgroundResource(R.drawable.circle_45)
                    binding.mark.visibility = View.VISIBLE
                } else if (45 < num && num <= 60) {
                    binding.popColor.setBackgroundResource(R.drawable.circle_60)
                    binding.mark.visibility = View.VISIBLE
                } else if (60 < num && num <= 75) {
                    binding.popColor.setBackgroundResource(R.drawable.circle_75)
                    binding.mark.visibility = View.VISIBLE
                } else if (75 < num && num <= 90) {
                    binding.popColor.setBackgroundResource(R.drawable.circle_90)
                    binding.mark.visibility = View.VISIBLE
                } else if (num == 100.0) {
                    binding.popColor.visibility = View.GONE
                    binding.icPop.visibility = View.VISIBLE
                    binding.mark.visibility = View.GONE

                    setRandomPop(day.date.toString())
                }

                binding.dateLayout.setOnClickListener {
                    activity.requestPlans(day)
                }
                binding.dayTv.text = day.date.toString()

                // 선택된 날짜
                if (day.date == picked_day) {
                    binding.dayTvLayout.background = activity.getDrawable(R.drawable.back_black_30dp)
                    binding.dayTv.setTextColor(activity.getColor(R.color.white))
                } else {
                    binding.dayTvLayout.background = null
                    binding.dayTv.setTextColor(activity.getColor(R.color.black))
                }
            }
        }

        private fun setRandomPop(day : String) {
            val num = day.substring(day.lastIndex).toInt()
            when (num) {
                1,6 -> binding.icPop.setImageResource(R.drawable.ic_pop1)
                2,7 -> binding.icPop.setImageResource(R.drawable.ic_pop2)
                3,8 -> binding.icPop.setImageResource(R.drawable.ic_pop3)
                4,9 -> binding.icPop.setImageResource(R.drawable.ic_pop4)
                5,0 -> binding.icPop.setImageResource(R.drawable.ic_pop5)
            }
        }
    }
}
