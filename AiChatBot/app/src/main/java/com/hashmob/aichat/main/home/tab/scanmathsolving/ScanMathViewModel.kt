package com.hashmob.aichat.main.home.tab.scanmathsolving

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hashmob.aichat.R
import com.hashmob.aichat.constants.Constants
import com.hashmob.aichat.data.main.Resource
import com.hashmob.aichat.data.model.apimodel.WriteResponse
import com.hashmob.aichat.data.repository.MainRepository
import com.hashmob.aichat.databinding.ActivityScanMathSolvingBinding
import com.hashmob.aichat.util.LogUtils
import com.hashmob.aichat.util.Utils.Companion.convertBitmapToString
import com.hashmob.aichat.util.Utils.Companion.getOutputDirectory
import com.hashmob.aichat.util.Utils.Companion.premiumScreenIntent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_scan_math_solving.viewFinder
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

class ScanMathViewModel @Inject constructor(var authRepository: MainRepository) : ViewModel() {
    @SuppressLint("StaticFieldLeak")
    lateinit var activity: ScanMathSolvingActivity
    lateinit var binding: ActivityScanMathSolvingBinding
    private var imageCapture: ImageCapture? = null
    private var outputDirectory: File? = null
    private lateinit var cameraExecutor: ExecutorService

    //LIVE DATA
    private val liveData = MutableLiveData<Resource<WriteResponse>>()
    var prompt: MutableLiveData<String> = MutableLiveData()

    //API
    private val compositeDisposable = CompositeDisposable()
    fun initialize(
        scanMathSolvingActivity: ScanMathSolvingActivity,
        binding: ActivityScanMathSolvingBinding
    ) {
        this.activity = scanMathSolvingActivity
        this.binding = binding
        outputDirectory = getOutputDirectory(activity)
        cameraExecutor = Executors.newSingleThreadExecutor()
        if (activity.allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                activity,
                activity.REQUIRED_PERMISSIONS,
                activity.REQUEST_CODE_PERMISSIONS
            )
        }
        initView()
    }

    fun initView() {
        activity.preferences.getString(Constants.LANGUAGE).toString()
        binding.header.ivDrawer.visibility = View.GONE
        binding.header.tv.visibility = View.GONE
        binding.header.tvTitle.visibility = View.VISIBLE
        binding.header.ivPdf.visibility = View.VISIBLE
        binding.header.ivPdf.setImageResource(R.drawable.iv_premium)
        binding.header.tvTitle.text =
            activity.getString(R.string.scan_and_math_solving)
        binding.header.ivBack.setOnClickListener {
            activity.onBackPressedDispatcher.onBackPressed()
        }
        binding.header.ivPdf.setOnClickListener {
            premiumScreenIntent(activity)
        }
        binding.btnTakePhoto.setOnClickListener {
            takePhoto()
        }
        binding.btnSolve.setOnClickListener {
//            if (activity.preferences.isProVersion() || activity.preferences.getInt(Constants.IMAGE_COUNTER) > 0) {
//                    val cropped: Bitmap = binding.iv.croppedImage
//                    val baseString = convertBitmapToString(cropped)
//                    callScanApi(baseString)
//            } else {
//                premiumScreenIntent(activity)
//            }
            if (activity.preferences.isProVersion()) {
                val cropped: Bitmap = binding.iv.croppedImage
                    val baseString = convertBitmapToString(cropped)
                    callScanApi(baseString)
            } else if (activity.preferences.getInt(Constants.allService) < 5){
                val cropped: Bitmap = binding.iv.croppedImage
                    val baseString = convertBitmapToString(cropped)
                    callScanApi(baseString)
            }else{
                premiumScreenIntent(activity)
            }
        }
        binding.btnRetake.setOnClickListener {
            binding.iv.visibility = View.INVISIBLE
            binding.viewFinder.visibility = View.VISIBLE
            binding.btnTakePhoto.visibility = View.VISIBLE
            binding.btnSolve.visibility = View.INVISIBLE
            binding.btnRetake.visibility = View.INVISIBLE
            binding.ivGallery.visibility = View.VISIBLE
        }
    }

    fun getScanLiveData(): MutableLiveData<Resource<WriteResponse>> {
        return liveData
    }

    fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(activity)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(activity.viewFinder.createSurfaceProvider())
                }
            imageCapture = ImageCapture.Builder().build()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    activity, cameraSelector, preview, imageCapture
                )
            } catch (exc: Exception) {
                LogUtils.Print("TAGS", "Use case binding failed" + exc.message)
            }
        }, ContextCompat.getMainExecutor(activity))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = File(
            outputDirectory,
            File.separator + System.currentTimeMillis() + ".png"
        )
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(activity),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    LogUtils.Print("TAGS", "Photo capture failed" + exc.message.toString() + exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    binding.iv.visibility = View.VISIBLE
                    binding.iv.setImageUriAsync(savedUri)
                    binding.viewFinder.visibility = View.INVISIBLE
                    binding.btnTakePhoto.visibility = View.INVISIBLE
                    binding.btnSolve.visibility = View.VISIBLE
                    binding.btnRetake.visibility = View.VISIBLE
                    binding.ivGallery.visibility = View.INVISIBLE
                }
            })
    }

    private fun callScanApi(baseString: String) {
        val messagesArr = ArrayList<HashMap<String, Any>>()
        val messageObj = HashMap<String, Any>()
        val messagesArr2 = ArrayList<HashMap<String, Any>>()
        val messageObj2 = HashMap<String, Any>()
        val messageObj0 = HashMap<String, Any>()
        val messageObj1 = HashMap<String, Any>()
        messageObj2[Constants.type] = Constants.text
        messageObj2[Constants.text] = activity.getString(R.string.read_the_image_params)
        messageObj0[Constants.url] = activity.getString(R.string.data_image_jpeg_base64, baseString)
        messageObj1[Constants.type] = Constants.image_url
        messageObj1[Constants.image_url] = messageObj0
        messageObj[Constants.content] = messagesArr2
        messageObj[Constants.role] = Constants.role_user
        messagesArr2.add(messageObj2)
        messagesArr2.add(messageObj1)
        messagesArr.add(messageObj)

        val params = HashMap<String, Any>()
        params[Constants.model] = Constants.scan_image_model
        params[Constants.messages] = messagesArr
        params[Constants.max_tokens] = 300
        compositeDisposable.add(authRepository.aiChat(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                liveData.setValue(Resource.loading())
            }
            .subscribe(
                { response: WriteResponse? ->
                    liveData.setValue(Resource.success(response))
                },
                { throwable: Throwable? ->
                    liveData.setValue(Resource.error(throwable!!))
                }
            ))
    }

}