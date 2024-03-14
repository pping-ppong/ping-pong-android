package com.pingpong_android.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TeamDTO(
    @SerializedName("teamId") var teamId : Long
) : Serializable {

    @SerializedName("teamName")
    var teamName : String = ""

    @SerializedName("hostId")
    var hostId : Long = -1

    @SerializedName("members")
    lateinit var memberList : List<MemberDTO>

    @SerializedName("memberId")
    lateinit var memberIdList : List<Long>

    @SerializedName("planList")
    lateinit var planList : List<PlanDTO>

    // todo : responses 추가 _ 문서 24번
}