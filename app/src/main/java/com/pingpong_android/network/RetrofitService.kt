package com.pingpong_android.network

import com.pingpong_android.model.AchieveDTO
import com.pingpong_android.model.OauthDTO
import com.pingpong_android.model.TodoDTO
import com.pingpong_android.model.UserDTO
import com.pingpong_android.model.result.*
import io.reactivex.Single
import retrofit2.http.*


interface RetrofitService {

    /////////////////////////////////////////////////
    /////////////////////////////////////////////////
    // UserDTO

    // 로그인
    @POST("/api/oauth/login")
    fun requestLogin(
        @Body userDTO: UserDTO
    ) : Single<UserResultDTO>

    // 토큰 재발행
    @POST("/api/oauth/reissue")
    fun requestReissue (
        @Body userDTO: UserDTO
    ) : Single<UserResultDTO>

    // 회원가입
    @POST("/api/members/sign-up")
    fun joinApp(
        @Body userDTO: UserDTO
    ) : Single<UserResultDTO>

    // 닉네임 중복체크
    @FormUrlEncoded
    @POST("/api/members/validate")
    fun checkValidNickNm(
        @Field("nickname") nickName : String
    ) : Single<UserResultDTO>

    // 마이페이지 - 유저 정보 조회
    @GET("/api/members/{id}/mypage")
    fun requestMyPageUserInfo(
        @Header("Authorization") accessToken : String,
        @Path("id") userId : String
    ) : Single<UserResultDTO>

    // 로그아웃
    @POST("/api/oauth/logout")
    fun requestLogout(
        @Body userDTO: UserDTO
    ) : Single<UserResultDTO>

    // 회원탈퇴
    @DELETE("/api/members/{id}")
    fun requestDeleteAccount(
        @Header("Authorization") accessToken : String,
        @Path("id") userId : String
    ) : Single<ResultDTO>

    /////////////////////////////////////////////////
    /////////////////////////////////////////////////
    // Notice

    // 알림 전체 조회 (30일 이내)
    @GET("/api/notifications")
    fun requestAllNotice(
        @Header("Authorization") accessToken : String
    ) : Single<NoticeResultDTO>

    // 안읽은 알림 조회
    @GET("/api/notifications/un-read")
    fun requestUnReadNotice(
        @Header("Authorization") accessToken : String
    ) : Single<ResultDTO>

    // 친구 신청 알림 요청
    @POST ("/api/notifications/friends")
    fun requestAlarmFriend(
        @Header("Authorization") accessToken : String,
        @Body respondentId : Long
    ) : Single<ResultDTO>


    /////////////////////////////////////////////////
    /////////////////////////////////////////////////
    // Team

    // 전체 캘린더 조회 (팝콘 성취율만)
    @GET("/api/members/calendars/achievement")
    fun requestMainCalendarAll(
        @Header("Authorization") accessToken : String,
        @Query("startDate") startDate : String,
        @Query("endDate") endDate : String
    ) : Single<AchieveResultDTO>

    // 해당 날짜의 전체 할일 조회
    @GET("/api/members/calendars")
    fun requestMainPlans(
        @Header("Authorization") accessToken : String,
        @Query("date") date : String
    ) : Single<TeamResultDTO>

    // 유저가 속한 팀 전체 조회
    @GET("/api/members/teams")
    fun requestUserTeams(
        @Header("Authorization") accessToken : String
    ) : Single<TeamListResultDTO>

    // 팀 생성
    @POST("/api/teams")
    fun requestMakeGroup(
        @Header("Authorization") accessToken : String,
        @Body team : HashMap<String, Any>
    ) : Single<TeamResultDTO>

    // 할 일 등록
    @POST("/api/teams/{id}/plans")
    fun requestAddTodo(
        @Header("Authorization") accessToken : String,
        @Path("id") teamId: Long,
        @Body todoDTO: TodoDTO
    ) : Single<TodoResultDTO>

    // 할 일 삭제
    @DELETE("/api/teams/{teamId}/plans/{planId}")
    fun deletePlan (
        @Header("Authorization") accessToken : String,
        @Path("teamId") teamId : Long,
        @Path("planId") planId : Long
    ) : Single<ResultDTO>

    // 그룹의 캘린더 조회 (팝콘 성취율만)
    @GET("/api/teams/{id}/calendars/achievement")
    fun requestTeamCalendarAll(
        @Header("Authorization") accessToken : String,
        @Path("id") teamId: Long,
        @Query("startDate") startDate : String,
        @Query("endDate") endDate : String
    ) : Single<AchieveResultDTO>

    // 그룹의 해당 날짜의 할일 조회
    @GET("/api/teams/{id}/calendars")
    fun requestTeamPlans(
        @Header("Authorization") accessToken : String,
        @Path("id") teamId: Long,
        @Query("date") date : String
    ) : Single<TeamResultDTO>

    // 할 일 표시 완료
    @PATCH("/api/teams/{teamId}/plans/{planId}/complete")
    fun requestPlanComplete(
        @Header("Authorization") accessToken : String,
        @Path("teamId") teamId : Long,
        @Path("planId") planId : Long
    ) : Single<ResultDTO>

    // 할 일 표시 미완료
    @PATCH("/api/teams/{teamId}/plans/{planId}/incomplete")
    fun requestPlanIncomplete(
        @Header("Authorization") accessToken : String,
        @Path("teamId") teamId : Long,
        @Path("planId") planId : Long
    ) : Single<ResultDTO>

    /////////////////////////////////////////////////
    /////////////////////////////////////////////////
    // Friend

    // 유저가 속한 팀 전체 조회
    @GET("/api/friends")
    fun requestUserFriendList(
        @Header("Authorization") accessToken : String
    ) : Single<FriendListResultDTO>

    // 닉네임으로 유저 검색
    @GET("/api/members/search")
    fun searchUserWithNickNm(
        @Header("Authorization") accessToken : String,
        @Query("nickname") nickName : String
    ) : Single<FriendListResultDTO>

    // 유저 검색 로그 저장
    @POST("/api/members/search-log")
    fun addSearchLog(
        @Header("Authorization") accessToken : String,
        @Body id : Long
    ) : Single<ResultDTO>

    // 유저 검색 로그 불러오기
    @GET("/api/members/search-log")
    fun requestSearchLog(
        @Header("Authorization") accessToken : String
    ) : Single<LogResultDTO>

    // 타 유저의 프로필 불러오기
    @GET("/api/members/{id}/profile")
    fun requestOthersProfile(
        @Header("Authorization") accessToken : String,
        @Path("id") memberId: Long
    ) : Single<UserResultDTO>

    // 친구 신청 승인
    @POST("/api/friends/accept")
    fun acceptFriendShip(
        @Header("Authorization") accessToken : String,
        @Body respondentId : Long
    ) : Single<ResultDTO>

    // 친구 신청 거절
    @POST("/api/friends/refuse")
    fun refuseFriendShip(
        @Header("Authorization") accessToken : String,
        @Body respondentId : Long
    ) : Single<ResultDTO>

    // 친구 신청하기
    @POST("/api/friends/apply")
    fun requestFriendShip(
        @Header("Authorization") accessToken : String,
        @Body params: HashMap<String, Long>
    ) : Single<ResultDTO>

    // 친구 신청 끊기
    @DELETE("/api/friends/unfollow")
    fun deleteFriendShip (
        @Header("Authorization") accessToken : String,
        @Field("memberId") memberId : Long
    )
}