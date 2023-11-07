package com.pingpong_android.network

import com.pingpong_android.model.OauthDTO
import com.pingpong_android.model.ResultDTO
import com.pingpong_android.model.UserDTO
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface RetrofitService {

    // 로그인 및 회원가입
    @POST("/api/oauth/info")
    fun getSocialInfo(
        @Body userDTO: UserDTO  // OauthDTO
    ) : Single<ResultDTO>

    @POST("/api/oauth/login")
    fun requestLogin(
        @Body userDTO: UserDTO
    ) : Single<ResultDTO>

    @POST("/api/members/sign-up")
    fun joinApp(
        @Body userDTO: UserDTO
    ) : Single<ResultDTO>

    @FormUrlEncoded
    @POST("/api/members/validate")
    fun checkValidNickNm(
        @Field("nickname") nickName : String
    ) : Single<ResultDTO>



}