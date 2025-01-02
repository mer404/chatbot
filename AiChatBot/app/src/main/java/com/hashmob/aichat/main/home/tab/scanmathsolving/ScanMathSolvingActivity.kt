package com.hashmob.aichat.main.home.tab.scanmathsolving

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.hashmob.aichat.base.BaseActivity
import com.hashmob.aichat.constants.Constants
import com.hashmob.aichat.data.main.Resource
import com.hashmob.aichat.data.model.apimodel.WriteResponse
import com.hashmob.aichat.databinding.ActivityScanMathSolvingBinding
import com.hashmob.aichat.di.ViewModelFactory
import com.hashmob.aichat.main.home.tab.scanmathsolving.solution.SolutionActivity
import com.hashmob.aichat.util.Utils
import javax.inject.Inject


class ScanMathSolvingActivity : BaseActivity() {
    lateinit var binding: ActivityScanMathSolvingBinding
    val REQUEST_CODE_PERMISSIONS = 20
    val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var viewModel: ScanMathViewModel
    override fun initViewBinding() {
        binding = DataBindingUtil.setContentView(
            this,
            com.hashmob.aichat.R.layout.activity_scan_math_solving
        )
        viewModel = viewModelFactory.create(ScanMathViewModel::class.java)
        binding.lifecycleOwner = this
        viewModel.initialize(this, binding)
        binding.viewModel = viewModel
        setAllPermission()
        initView()
        viewModel.getScanLiveData().observe(this) { response: Resource<WriteResponse> ->
            consumeScanAPIResponse(response)
        }
    }

    private fun initView() {
        binding.ivGallery.setOnClickListener {
            val i = Intent()
            i.type = "image/*"
            i.action = Intent.ACTION_GET_CONTENT
            resultLauncher.launch(i)
        }
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                binding.iv.visibility = View.VISIBLE
                binding.iv.setImageUriAsync(result.data?.data)
                binding.viewFinder.visibility = View.INVISIBLE
                binding.btnTakePhoto.visibility = View.INVISIBLE
                binding.btnSolve.visibility = View.VISIBLE
                binding.btnRetake.visibility = View.VISIBLE
                binding.ivGallery.visibility = View.INVISIBLE
            }
        }

    fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                viewModel.startCamera()
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
    private fun consumeScanAPIResponse(response: Resource<WriteResponse>) {
        when (response.status) {
            Resource.Status.LOADING -> {
                showProgress()
            }

            Resource.Status.ERROR -> {
                hideProgress()
                if (response.error?.message != null) Utils.makeToast(this, response.error.message)
            }

            Resource.Status.SUCCESS -> {
                hideProgress()
                if (response.data?.choices != null) {
                    val intent = Intent(this, SolutionActivity::class.java)
                    intent.putExtra(
                        Constants.answer,
                        response.data.choices.first()?.message?.content
                    )
//                    preferences.putInt(Constants.IMAGE_COUNTER, preferences.getInt(Constants.IMAGE_COUNTER) - 1)
                    Utils.setFirebaseData(this,this.preferences.getInt(Constants.allService),false,false)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}