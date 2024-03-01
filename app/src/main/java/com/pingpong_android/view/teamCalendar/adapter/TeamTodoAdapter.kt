package com.pingpong_android.view.teamCalendar.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pingpong_android.R
import com.pingpong_android.databinding.ItemTodoListBinding
import com.pingpong_android.model.PlanDTO
import com.pingpong_android.view.teamCalendar.TeamCalendarActivity

class TeamTodoAdapter : RecyclerView.Adapter<TeamTodoAdapter.TeamTodoViewHolder>() {

    private lateinit var activity : TeamCalendarActivity
    private var planList : List<PlanDTO> = emptyList()

    fun setActivity(activity: TeamCalendarActivity) {
        this.activity = activity
    }

    fun addPlanList(planList: List<PlanDTO>) {
        this.planList = planList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): TeamTodoAdapter.TeamTodoViewHolder {
        val binding = ItemTodoListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TeamTodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TeamTodoAdapter.TeamTodoViewHolder, position: Int) {
        holder.bind(planList.get(position), position)
    }

    override fun getItemCount(): Int {
        return planList.size
    }

    inner class TeamTodoViewHolder(val binding : ItemTodoListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(planDTO: PlanDTO, position: Int) {
            binding.title.text = planDTO.title

            // 할 일 상태 - 생성/삭제됨
            if (planDTO.status == "ACTIVE") {
                binding.planLayout.visibility = View.VISIBLE

                // 담당자
                if (activity.myMemberId == planDTO.manager!!.memberId) {
                    binding.btnStar.visibility = View.VISIBLE
                    binding.image.visibility = View.GONE
                    binding.btnEtc.visibility = View.VISIBLE

                    // 수행 상태 - 완료/미완
                    if (planDTO.achievement == "COMPLETE") {
                        binding.btnStar.setImageResource(R.drawable.ic_star_on)
                        binding.btnStar.setOnClickListener {
                            activity.requestPlanComplete(false, planDTO.planId)
                        }
                    } else {
                        binding.btnStar.setImageResource(R.drawable.ic_star_off)
                        binding.btnStar.setOnClickListener {
                            activity.requestPlanComplete(true, planDTO.planId)
                        }
                    }

                    // bottomSheet
                    binding.btnEtc.setOnClickListener { activity.showBottomSheet(planDTO.planId) }
                } else {
                    binding.btnStar.visibility = View.GONE
                    binding.image.visibility = View.VISIBLE
                    binding.btnEtc.visibility = View.GONE

                    // 다른 담당자의 이미지
                    if (planDTO.manager!!.profileImage.isNotEmpty()) {
                        binding.defaultImage.visibility = View.GONE
                        Glide.with(binding.profileImg)
                            .load(planDTO.manager!!.profileImage)
                            .into(binding.profileImg)
                    } else {
                        binding.defaultImage.visibility = View.VISIBLE
                        Glide.with(binding.profileImg).clear(binding.profileImg)
                    }
                }
            } else {
                binding.planLayout.visibility = View.GONE
            }
        }
    }
}