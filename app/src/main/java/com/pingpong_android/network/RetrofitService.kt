package com.pingpong_android.network

import com.pingpong_android.model.OauthDTO
import com.pingpong_android.model.UserDTO
import com.pingpong_android.model.result.*
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


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

    /////////////////////////////////////////////////
    /////////////////////////////////////////////////
    // Team

    // 유저가 속한 팀 전체 조회
    @GET("/api/members/teams")
    fun requestUserTeams(
        @Header("Authorization") accessToken : String
        ) : Single<TeamListResultDTO>

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

    // 검색 로그 불러오기
    @GET("/api/members/search-log")
    fun requestSearchLog(
        @Header("Authorization") accessToken : String
    ) : Single<LogResultDTO>

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
        @Body applicantId : Long,
        @Body respondentId : Long
    ) : Single<ResultDTO>

    // 친구 신청 끊기
    @DELETE("/api/friends/unfollow?memberId=")
    fun deleteFriendShip (
        @Header("Authorization") accessToken : String,
        @Field("memberId") memberId : Long
    )
}