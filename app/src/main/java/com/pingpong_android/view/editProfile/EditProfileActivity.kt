package com.pingpong_android.view.editProfile

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.databinding.ActivityEditProfieBinding
import com.pingpong_android.view.gallery.GalleryActivity
import com.pingpong_android.view.main.MainActivity

class EditProfileActivity : BaseActivity<ActivityEditProfieBinding>(R.layout.activity_edit_profie) {

    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = EditProfileViewModel()
        binding.activity = this

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
//                val return = it.data?.getStringExtra("return") ?: ""
            }
        }
    }

    fun goToGallery() {
        val intent = Intent(this, GalleryActivity::class.java)
        activityResultLauncher.launch(intent)
    }
}