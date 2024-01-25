package com.pingpong_android.view.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pingpong_android.R
import com.pingpong_android.databinding.ItemCalendarMonthBinding
import com.pingpong_android.view.main.MainActivity
import java.util.Calendar
import java.util.Date

class CalendarAdapter : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    private var calendar = Calendar.getInstance()
    private lateinit var activity : MainActivity

    fun setMainActivity(activity: MainActivity) {
        this.activity = activity
    }

    fun addAchieveList() {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val binding = ItemCalendarMonthBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CalendarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return 1
    }

    inner class CalendarViewHolder(val binding: ItemCalendarMonthBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            // 날짜 adapter
            calendar.time = Date()
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            calendar.add(Calendar.MONTH, position)
            binding.monthInfo.text = "${calendar.get(Calendar.YEAR)}년 ${calendar.get(Calendar.MONTH) + 1}월"
            val tempMonth = calendar.get(Calendar.MONTH)

            var dayList: MutableList<Date> = MutableList(6 * 7) { Date() }
            for(i in 0..5) {
                for(k in 0..6) {
                    calendar.add(Calendar.DAY_OF_MONTH, (1-calendar.get(Calendar.DAY_OF_WEEK)) + k)
                    dayList[i * 7 + k] = calendar.time
                }
                calendar.add(Calendar.WEEK_OF_MONTH, 1)
            }

            val dayListManager = GridLayoutManager(activity, 7)
            val dayListAdapter = DayAdapter(tempMonth, dayList)

            binding.dateRv.apply {
                layoutManager = dayListManager
                adapter = dayListAdapter
            }

            // 버튼 이벤트
            binding.lastMonth.setOnClickListener { bind(position-1) }
            binding.nextMonth.setOnClickListener { bind(position+1) }
        }
    }
}