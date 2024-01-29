package com.pingpong_android.view.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pingpong_android.R
import com.pingpong_android.databinding.LayoutTodoBinding
import com.pingpong_android.model.TeamDTO
import com.pingpong_android.view.main.MainActivity

class PlanTeamAdapter(private var todoList : List<TeamDTO>) : RecyclerView.Adapter<PlanTeamAdapter.PlanTeamViewHolder>() {

    private lateinit var activity : MainActivity

    fun setActivity(activity: MainActivity) {
        this.activity = activity
    }

    fun addTodoList(todoList : List<TeamDTO>) {
        this.todoList = todoList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanTeamViewHolder {
        val binding = LayoutTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlanTeamViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    override fun onBindViewHolder(holder: PlanTeamViewHolder, position: Int) {
        holder.bind(todoList.get(position), position)
    }

    inner class PlanTeamViewHolder(val binding : LayoutTodoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(teamDTO: TeamDTO, position: Int) {
            binding.todoGroup.text = teamDTO.teamName

            // drop-down
            binding.title.setOnClickListener {
                if (binding.planRv.visibility == View.GONE) {
                    binding.planRv.visibility = View.VISIBLE
                    binding.btnDropDown.setImageResource(R.drawable.ic_arrow_up)
                } else {
                    binding.planRv.visibility = View.GONE
                    binding.btnDropDown.setImageResource(R.drawable.ic_arrow_down)
                }
            }

            // plan-list
            val planListManager = LinearLayoutManager(activity)
            val planListAdapter = PlansAdapter(activity, teamDTO.teamId, teamDTO.planList)

            binding.planRv.apply {
                layoutManager = planListManager
                adapter = planListAdapter
            }
        }
    }
}