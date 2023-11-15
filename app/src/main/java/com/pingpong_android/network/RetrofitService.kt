package com.pingpong_android.network

import com.pingpong_android.model.OauthDTO
import com.pingpong_android.model.result.UserResultDTO
import com.pingpong_android.model.UserDTO
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface RetrofitService {

    // 로그인 및 회원가입
    @POST("/api/oauth/info")
    fun getSocialInfo(
        @Body oauthDTO: OauthDTO
    ) : Single<UserResultDTO>

    @POST("/api/oauth/login")
    fun requestLogin(
        @Body userDTO: UserDTO
    ) : Single<UserResultDTO>

    @POST("/api/oauth/reissue")
    fun requestReissue (
        @Body userDTO: UserDTO
    ) : Single<UserResultDTO>

    @POST("/api/members/sign-up")
    fun joinApp(
        @Body userDTO: UserDTO
    ) : Single<UserResultDTO>

    @FormUrlEncoded
    @POST("/api/members/validate")
    fun checkValidNickNm(
        @Field("nickname") nickName : String
    ) : Single<UserResultDTO>



}