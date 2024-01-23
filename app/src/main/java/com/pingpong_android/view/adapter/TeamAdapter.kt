package com.pingpong_android.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pingpong_android.base.Constants.Companion.INTENT_EXTRA_TEAM_DTO
import com.pingpong_android.databinding.ItemTeamBinding
import com.pingpong_android.model.TeamDTO
import com.pingpong_android.view.teamCalendar.TeamCalendarActivity

class TeamAdapter(private var teamList: List<TeamDTO>) : RecyclerView.Adapter<TeamAdapter.TeamViewHolder>() {

    private lateinit var context: Context

    fun setContext(context: Context) {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        val binding =  ItemTeamBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TeamViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return if (teamList != null)  teamList.size else 0
    }

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        holder.bind(teamList.get(position), position)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, TeamCalendarActivity::class.java)
            intent.putExtra(INTENT_EXTRA_TEAM_DTO, teamList.get(position))
            context.startActivity(intent)
        }
    }

    fun addList(teams : List<TeamDTO>) {
        teamList = teams
        notifyDataSetChanged()
    }

    inner class TeamViewHolder(val binding : ItemTeamBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(teamDTO: TeamDTO, position : Int) {
            binding.groupName.text = teamDTO.teamName
            
            val membersLayoutManager = LinearLayoutManager(context)
            val memberHorizontalAdapter = MemberHorizontalAdapter(teamDTO.memberList)

            binding.memberRv.apply {
                layoutManager = membersLayoutManager
                adapter = memberHorizontalAdapter
            }
        }
    }
}