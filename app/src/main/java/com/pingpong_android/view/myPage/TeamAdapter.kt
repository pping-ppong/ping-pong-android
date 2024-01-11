package com.pingpong_android.view.myPage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pingpong_android.databinding.ItemGroupBinding
import com.pingpong_android.model.TeamDTO
import com.pingpong_android.view.adapter.MemberHorizontalAdapter

class TeamAdapter(private var teamList: List<TeamDTO>) : RecyclerView.Adapter<TeamAdapter.TeamViewHolder>() {

    private lateinit var activity: MyPageActivity

    fun setActivity(activity : MyPageActivity) {
        this.activity = activity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        val binding =  ItemGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TeamViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return if (teamList != null)  teamList.size else 0
    }

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        holder.bind(teamList.get(position), position)
        // todo : teamData click-event
    }

    fun addList(teams : List<TeamDTO>) {
        teamList = teams
        notifyDataSetChanged()
    }

    inner class TeamViewHolder(val binding : ItemGroupBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(teamDTO: TeamDTO, position : Int) {
            binding.groupName.text = teamDTO.teamName
            
            val membersLayoutManager = LinearLayoutManager(activity)
            val memberHorizontalAdapter = MemberHorizontalAdapter(teamDTO.memberList)

            binding.memberRv.apply {
                layoutManager = membersLayoutManager
                adapter = memberHorizontalAdapter
            }
        }
    }
}