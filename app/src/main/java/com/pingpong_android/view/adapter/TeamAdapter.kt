package com.pingpong_android.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pingpong_android.databinding.ItemGroupBinding
import com.pingpong_android.model.TeamDTO
import com.pingpong_android.view.main.MainActivity
import com.pingpong_android.view.myPage.MyPageActivity

class TeamAdapter(private var teamList: List<TeamDTO>) : RecyclerView.Adapter<TeamAdapter.TeamViewHolder>() {

    private lateinit var context: Context

    fun setContext(context: Context) {
        this.context = context
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
        holder.itemView.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)  // todo : 그룹 할일 페이지로 변경
            context.startActivity(intent)
        }
    }

    fun addList(teams : List<TeamDTO>) {
        teamList = teams
        notifyDataSetChanged()
    }

    inner class TeamViewHolder(val binding : ItemGroupBinding) : RecyclerView.ViewHolder(binding.root) {
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