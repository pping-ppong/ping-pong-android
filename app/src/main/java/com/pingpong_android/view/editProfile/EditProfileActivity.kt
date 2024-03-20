package com.pingpong_android.view.editProfile

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.base.Constants
import com.pingpong_android.databinding.ActivityEditProfieBinding
import com.pingpong_android.view.gallery.GalleryActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream


class EditProfileActivity : BaseActivity<ActivityEditProfieBinding>(R.layout.activity_edit_profie, TransitionMode.RIGHT) {

    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private var userDTO = prefs.getUser()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = EditProfileViewModel()
        binding.activity = this

        initView()
        initSubscribe()
        checkNickNameValidation()
        onActivityResult()
    }

    private fun onActivityResult() {
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                if (it.data != null) {
                    val uri : Uri = Uri.parse(it.data!!.getStringExtra(Constants.INTENT_EXTRA_URI))
                    val file = File(uri.path)
                    var inputStream: InputStream? = null
                    try {
                        inputStream = contentResolver.openInputStream(uri)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream)
                    val requestBody = RequestBody.Companion.create("image/*".toMediaTypeOrNull(), byteArrayOutputStream.toByteArray())
                    val uploadFile: MultipartBody.Part =
                        MultipartBody.Part.createFormData("multipartFile", file.name, requestBody)
                    binding.viewModel!!.requestAddImageS3(uploadFile)
                }
            }
        }
    }

    private fun initView() {
        binding.topPanel.setLeftClickListener(listener = {onBackPressed()})

        if (userDTO.profileImage.isNotEmpty()) {
            binding.profileImg.visibility = View.VISIBLE
            binding.defaultPhoto.visibility = View.GONE
            Glide.with(this).load(userDTO.profileImage).into(binding.profileImg)
        } else {
            binding.profileImg.visibility = View.GONE
            binding.defaultPhoto.visibility = View.VISIBLE
            Glide.with(this).clear(binding.profileImg)
        }
    }

    private fun initSubscribe() {
        subscribeNickNmCheck()
        subscribeEditProfile()
        subscribeAddImage()
        subscribeImageUrl()
    }

    private fun subscribeNickNmCheck() {
        binding.viewModel!!.nickNameCheckResult.observe(this, Observer {
            if (it.isSuccess) {
                if (it.code == 200) {
                    binding.nickNmEtLayout.error = null
                    userDTO.nickName = binding.nickNmEt.text.toString()
                    binding.viewModel!!.isReady.value = true
                }
            } else {
                if (it.message != null) {
                    binding.nickNmEtLayout.error = it.message
                    binding.viewModel!!.isReady.value = false
                } else
                    binding.viewModel!!.isReady.value = false
            }
        })
    }

    private fun subscribeEditProfile() {
        binding.viewModel!!.updateResult.observe(this, Observer {
            if (it.isSuccess) {
                Toast.makeText(this, "프로필을 수정했습니다:-)", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "프로필 수정을 실패했습니다ㅜ_ㅜ", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun subscribeAddImage() {
        binding.viewModel!!.addImgS3Result.observe(this, Observer {
            if (it.isSuccess && it.imgList.isNotEmpty()) {
                // 사진 등록 성공 시
//                binding.viewModel!!.requestImageUrl(it.imgList[0])

                userDTO.profileImage = it.imgList[0]

                 binding.profileImg.visibility = View.VISIBLE
                binding.defaultPhoto.visibility = View.GONE
                Glide.with(this).load(userDTO.profileImage).into(binding.profileImg)
            } else {
                // 사진 등록 실패 시
                Toast.makeText(this, "사진을 불러오는데 실패했습니다", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun subscribeImageUrl() {
        binding.viewModel!!.imgUrlResult.observe(this, Observer {
            if (it.isSuccess && it.result.isNotEmpty()) {
                // url 요청 성공 시
                userDTO.profileImage = it.result

                binding.profileImg.visibility = View.VISIBLE
                binding.defaultPhoto.visibility = View.GONE
                Glide.with(this).load(userDTO.profileImage).into(binding.profileImg)
            } else {
                // url 요청 실패 시
                Toast.makeText(this, "사진을 불러오는데 실패했습니다", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun checkNickNameValidation() {
        binding.nickNmEt.addTextChangedListener(textWatcher)
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            binding.viewModel!!.requestNickNameValidation(s.toString())
        }
    }

    fun requestEditProfile() {
        binding.viewModel!!.requestEditProfile(prefs.getBearerToken(), userDTO)
    }

    fun goToGallery() {
        val intent = Intent(this, GalleryActivity::class.java)
        intent.putExtra("FROM_EDIT", true)
        activityResultLauncher.launch(intent)
    }
}
