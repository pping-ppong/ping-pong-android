package com.pingpong_android.view.editProfile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.base.Constants
import com.pingpong_android.databinding.ActivityEditProfieBinding
import com.pingpong_android.view.gallery.GalleryActivity
import com.pingpong_android.view.main.MainActivity

class EditProfileActivity : BaseActivity<ActivityEditProfieBinding>(R.layout.activity_edit_profie, TransitionMode.RIGHT) {

    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = EditProfileViewModel()
        binding.activity = this

        initSubscribe()
        onActivityResult()

        binding.topPanel.setLeftClickListener(listener = {onBackPressed()})
    }

    private fun onActivityResult() {
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                if (it.data != null) {
                    val uri : Uri = Uri.parse(it.data!!.getStringExtra(Constants.INTENT_EXTRA_URI))
                    // todo : 압축 및 전송 준비
                }
            }
        }
    }

    private fun initSubscribe() {
        subscribeAddImage()
        subscribeImageUrl()
    }

    private fun subscribeAddImage() {
        binding.viewModel!!.addImgS3Result.observe(this, Observer {
            if (it.isSuccess && it.imgList.isNotEmpty()) {
                // 사진 등록 성공 시

            } else {
                // 사진 등록 실패 시

            }
        })
    }

    private fun subscribeImageUrl() {
        binding.viewModel!!.imgUrlResult.observe(this, Observer {
            if (it.isSuccess && it.imgList.isNotEmpty()) {
                // url 요청 성공 시

            } else {
                // url 요청 실패 시

            }
        })
    }


    fun goToGallery() {
        val intent = Intent(this, GalleryActivity::class.java)
        intent.putExtra("FROM_EDIT", true)
        activityResultLauncher.launch(intent)
    }
}