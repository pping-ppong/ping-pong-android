package com.pingpong_android.view.editProfile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pingpong_android.base.BaseViewModel
import com.pingpong_android.model.result.ImageResultDTO
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody

class EditProfileViewModel : BaseViewModel(){

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
    private val _imgUrlResult = MutableLiveData<ImageResultDTO>()
    val imgUrlResult : LiveData<ImageResultDTO>
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

}