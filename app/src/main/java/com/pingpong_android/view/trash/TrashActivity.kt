package com.pingpong_android.view.trash

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.pingpong_android.R
import com.pingpong_android.base.BaseActivity
import com.pingpong_android.base.Constants
import com.pingpong_android.base.Constants.Companion.INTENT_EXTRA_TEAM_DTO
import com.pingpong_android.databinding.ActivityTrashBinding
import com.pingpong_android.layout.HostDialog
import com.pingpong_android.layout.ModalBottomSheetDialog
import com.pingpong_android.layout.YNDialog
import com.pingpong_android.model.TeamDTO
import com.pingpong_android.model.TrashDTO

class TrashActivity : BaseActivity<ActivityTrashBinding>(R.layout.activity_trash) {

    lateinit var team : TeamDTO
    var isHost : Boolean = false
    private var trashAdapter = TrashAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = TrashViewModel()
        binding.activity = this

        initData()
        initView()
        subscribeTrash()
        subscribeResult()

        binding.viewModel!!.requestTrashAll(prefs.getBearerToken(), team.teamId)
    }

    private fun initData() {
        team = intent.getSerializableExtra(INTENT_EXTRA_TEAM_DTO) as TeamDTO
        isHost = team.hostId == prefs.getId().toLong()
    }

    private fun initView() {
        binding.topPanel.setLeftClickListener(listener = {onBackPressed()})
        if (isHost) {
            binding.topPanel.setRightBtn(getDrawable(R.drawable.ic_trash))
            binding.topPanel.setRightClickListener(listener = { deleteAllDialog() })
        }

        // adapter
        val trashLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        trashAdapter.setActivity(this)
        trashAdapter.addList(emptyList())

        binding.trashRv.apply {
            layoutManager = trashLayoutManager
            adapter = trashAdapter
        }
    }

    private fun subscribeTrash() {
        binding.viewModel!!.trashResult.observe(this, Observer {
            if (it.isSuccess && it.trashList.isNotEmpty()) {
                binding.trashRv.visibility = View.VISIBLE
                binding.noTrashList.visibility = View.GONE

                trashAdapter.addList(it.trashList)
            } else {
                binding.trashRv.visibility = View.GONE
                binding.noTrashList.visibility = View.VISIBLE
            }
        })
    }

    private fun subscribeResult() {
        binding.viewModel!!.result.observe(this, Observer {
            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            binding.viewModel!!.requestTrashAll(prefs.getBearerToken(), team.teamId)
        })
    }

    private fun deleteAllDialog() {
        val ynDialog = YNDialog("휴지통을 완전히 비우시겠습니까?", listOf("취소", "비우기"))
        ynDialog.setButtonClickListener(object : YNDialog.OnButtonClickListener{
            override fun onFirstClicked() {
                // 취소
                ynDialog.dismiss()
            }

            override fun onSecondClicked() {
                // 비우기
                binding.viewModel!!.requestDeleteAllPlan(prefs.getBearerToken(), team.teamId)
                ynDialog.dismiss()
            }
        })
        ynDialog.show(supportFragmentManager, HostDialog.TAG)
    }

    fun restoreTodo(trash : TrashDTO) {
        if (isHost) {
            val modalBottomSheet = ModalBottomSheetDialog(listOf("복구하기", "삭제하기"))
            modalBottomSheet.setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme)
            modalBottomSheet.setDialogInterface(object : ModalBottomSheetDialog.ModalBottomSheetDialogInterface {
                override fun onFirstClickListener() {
                    // 복구하기
                    binding.viewModel!!.requestRestorePlan(prefs.getBearerToken(), team.teamId, trash.planId)
                    modalBottomSheet.dismiss()
                }

                override fun onSecondClickListener() {
                    // 삭제하기
                    binding.viewModel!!.requestDeleteOnePlan(prefs.getBearerToken(), team.teamId, trash.planId)
                    modalBottomSheet.dismiss()
                }

            })
            modalBottomSheet.show(supportFragmentManager, ModalBottomSheetDialog.TAG)
        } else {
            val modalBottomSheet = ModalBottomSheetDialog(listOf("복구하기", ""))
            modalBottomSheet.setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme)
            modalBottomSheet.setDialogInterface(object : ModalBottomSheetDialog.ModalBottomSheetDialogInterface {
                override fun onFirstClickListener() {
                    // 복구하기
                    binding.viewModel!!.requestRestorePlan(prefs.getBearerToken(), team.teamId, trash.planId)
                    modalBottomSheet.dismiss()
                }

                override fun onSecondClickListener() {
                    // none
                    modalBottomSheet.dismiss()
                }

            })
            modalBottomSheet.show(supportFragmentManager, ModalBottomSheetDialog.TAG)
        }
    }
}