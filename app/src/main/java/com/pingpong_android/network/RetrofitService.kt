package com.pingpong_android.network

import com.pingpong_android.model.OauthDTO
import com.pingpong_android.model.UserDTO
import com.pingpong_android.model.result.*
import io.reactivex.Single
import retrofit2.http.Body
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

    // 소셜 로그인 사용자 정보 불러오기
    @POST("/api/oauth/info")
    fun getSocialInfo(
        @Body oauthDTO: OauthDTO
    ) : Single<UserResultDTO>

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
    // Member

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

    // 검색 로그 저장
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
}