package com.pingpong_android.view.teamCalendar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pingpong_android.databinding.ItemCalendarMonthBinding
import com.pingpong_android.model.AchieveDTO
import com.pingpong_android.view.main.adapter.DayAdapter
import com.pingpong_android.view.teamCalendar.TeamCalendarActivity
import java.time.LocalDate
import java.util.*

class TeamCalendarAdapter : RecyclerView.Adapter<TeamCalendarAdapter.CalendarViewHolder>() {

    private var calendar = Calendar.getInstance()
    private lateinit var date: LocalDate
    private lateinit var activity : TeamCalendarActivity
    private var achieveList : List<AchieveDTO> = emptyList()
    private var last_month_days = 0
    private var picked_day = 1

    fun setActivity(activity: TeamCalendarActivity) {
        this.activity = activity
    }

    fun addAchieveList(achieveList: List<AchieveDTO>) {
        this.achieveList = achieveList
    }

    fun setPickedDate(day : Int) {
        picked_day = day
        notifyDataSetChanged()
    }

    fun setDateToCalendar(date: LocalDate) {
        this.date = date
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
            calendar.set(date.year, date.monthValue-1, 1)
            // 날짜 adapter
            last_month_days = 0
            binding.monthInfo.text = "${calendar.get(Calendar.YEAR)}년 ${calendar.get(Calendar.MONTH) + 1}월"
            val tempMonth = calendar.get(Calendar.MONTH)

            var dayList: MutableList<Date> = MutableList(6 * 7) { Date() }
            for(i in 0..5) {
                for(k in 0..6) {
                    calendar.add(Calendar.DAY_OF_MONTH, (1-calendar.get(Calendar.DAY_OF_WEEK)) + k)
                    dayList[i * 7 + k] = calendar.time

                    if (i == 0 && calendar.time.month != tempMonth) {
                        last_month_days += 1
                    }
                }
                calendar.add(Calendar.WEEK_OF_MONTH, 1)
            }

            val dayListManager = GridLayoutManager(activity, 7)
            val dayListAdapter = TeamDayAdapter(activity, tempMonth, dayList, getAchieve(dayList), picked_day)

            binding.dateRv.apply {
                layoutManager = dayListManager
                adapter = dayListAdapter
            }

            // 버튼 이벤트
            binding.lastMonth.setOnClickListener {
                activity.requestCalAchieveNow(-1)
                picked_day = 1
            }
            binding.nextMonth.setOnClickListener {
                activity.requestCalAchieveNow(1)
                picked_day = 1
            }
        }

        private fun getAchieve(days : List<Date>) : MutableList<Double> {
            var achieves : MutableList<Double> = MutableList(days.size) { 0.0 }

            // 달성율 리스트 생성
            for (i in achieveList) {
                val date : LocalDate = LocalDate.parse(i.date)
                achieves[last_month_days + date.dayOfMonth-1] = i.achievement
            }

            return achieves
        }
    }
}