package com.pingpong_android.view.join

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pingpong_android.base.BaseViewModel
import com.pingpong_android.model.result.UserResultDTO
import com.pingpong_android.model.UserDTO
import com.pingpong_android.model.result.ImageResultDTO
import com.pingpong_android.model.result.ResultDTO
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody

class JoinViewModel : BaseViewModel(){

    var isReadyNickname : MutableLiveData<Boolean> = MutableLiveData(false)
    var isReadyTerms : MutableLiveData<Boolean> = MutableLiveData(false)
    var isReadyAll : MutableLiveData<Boolean> = MutableLiveData(false)

    // 닉네임 중복 확인
    private val _nickNameCheckResult = MutableLiveData<UserResultDTO>()
    val nickNameCheckResult : LiveData<UserResultDTO>
        get() = _nickNameCheckResult
    fun requestNickNameValidation(nickNm : String) {
        addDisposable(
            instance!!.checkValidNickNm(nickNm)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _nickNameCheckResult.postValue(it)
                }, {
                    Log.e("Error", "requestJoin") })
        )
    }

    // S3 사진 등록
    private val _addImgS3Result = MutableLiveData<ImageResultDTO>()
    val addImgS3Result : LiveData<ImageResultDTO>
        get() = _addImgS3Result
    fun requestAddImageS3(image : MultipartBody.Part) {
        addDisposable(
            instance!!.requestAddImage(image)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _addImgS3Result.postValue(it)
                }, {
                    Log.e("Error", "requestJoin") })
        )
    }

    // S3 사진 url 불러오기
    private val _imgUrlResult = MutableLiveData<ResultDTO>()
    val imgUrlResult : LiveData<ResultDTO>
        get() = _imgUrlResult
    fun requestImageUrl(imageName : String) {
        addDisposable(
            instance!!.requestImageName(imageName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _imgUrlResult.postValue(it)
                }, {
                    Log.e("Error", "requestJoin") })
        )
    }

    // 회원가입 요청
    private val _userData = MutableLiveData<UserResultDTO>()
    val userData : LiveData<UserResultDTO>
        get() = _userData
    fun requestJoin(userDTO : UserDTO) {
        addDisposable(
            instance!!.joinApp(userDTO)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _userData.postValue(it)
            },{
                Log.e("Error", "requestJoin")} )
        )
    }

    // 로그인 요청
    private val _userLogin = MutableLiveData<UserResultDTO>()
    val userLogin : LiveData<UserResultDTO>
        get() = _userLogin
    fun requestLogin(userDTO: UserDTO) {
        addDisposable(
            instance!!.requestLogin(userDTO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _userLogin.postValue(it)
                },{
                    Log.e("Error", "requestController")} )
        )
    }
}