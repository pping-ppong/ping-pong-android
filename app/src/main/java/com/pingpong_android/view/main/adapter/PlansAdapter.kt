package com.pingpong_android.view.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pingpong_android.R
import com.pingpong_android.base.Status
import com.pingpong_android.databinding.ItemTodoListBinding
import com.pingpong_android.model.PlanDTO
import com.pingpong_android.model.TeamDTO
import com.pingpong_android.view.main.MainActivity

class PlansAdapter(val activity : MainActivity, val team: TeamDTO, val planList: List<PlanDTO>) : RecyclerView.Adapter<PlansAdapter.PlansViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlansAdapter.PlansViewHolder {
        val binding = ItemTodoListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlansViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlansAdapter.PlansViewHolder, position: Int) {
        holder.bind(planList.get(position), position)
    }

    override fun getItemCount(): Int {
        return planList.size
    }

    inner class PlansViewHolder(val binding : ItemTodoListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(planDTO: PlanDTO, position: Int) {
            binding.title.text = planDTO.title

            // 할 일 상태 - 생성/삭제됨
            if (planDTO.status == Status.ACTIVE) {
                binding.planLayout.visibility = View.VISIBLE

                // 수행 상태 - 완료/미완
                if (planDTO.achievement == Status.COMPLETE) {
                    binding.btnStar.setImageResource(R.drawable.ic_star_on)
                    binding.btnStar.setOnClickListener {
                        activity.requestPlanComplete(false, team.teamId, planDTO.planId)
                    }
                } else {
                    binding.btnStar.setImageResource(R.drawable.ic_star_off)
                    binding.btnStar.setOnClickListener {
                        activity.requestPlanComplete(true, team.teamId, planDTO.planId)
                    }
                }

                // bottomSheet
                binding.btnEtc.setOnClickListener { activity.showBottomSheet(team, planDTO.planId) }
            } else {
                binding.planLayout.visibility = View.GONE
            }
        }
    }
}