package com.hashmob.aichat.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.*
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import com.hashmob.aichat.R
import com.hashmob.aichat.base.BaseApplication
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class ImagePicker : DialogFragment(), View.OnClickListener {
    var listener: onUpdate? = null
    lateinit var tvCancel: TextView
     lateinit var llCamera: LinearLayout
     lateinit var llGallery: LinearLayout
    var imageUri: Uri? = null
    var imgPath: String? = null
    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(0, R.style.MaterialDialogSheet)
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        //super.setupDialog(dialog, style);
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setGravity(Gravity.BOTTOM)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_choose_source, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        llCamera = view.findViewById(R.id.llCamera)
        llGallery = view.findViewById(R.id.llGallery)
        tvCancel = view.findViewById(R.id.tvCancel)
        llCamera.setOnClickListener(this)
        llGallery.setOnClickListener(this)
        tvCancel.setOnClickListener(this)
    }

    private fun chkAllPermission(requestCode: Int): Boolean {
        var result: Int
        val permissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        for (p in permissions) {
            result = ContextCompat.checkSelfPermission(BaseApplication.context, p)
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p)
            }
        }
        return if (!listPermissionsNeeded.isEmpty()) {
            if (CAMERA_PERMISSION_REQUEST == requestCode) requestPermissions(
                listPermissionsNeeded.toTypedArray(),
                CAMERA_PERMISSION_REQUEST
            ) else requestPermissions(
                listPermissionsNeeded.toTypedArray(),
                STORAGE_PERMISSION_REQUEST
            )
            false
        } else {
            true
        }
    }

    private fun chkStoragePermission(): Boolean {
        var result: Int
        val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        for (p in permissions) {
            result = ContextCompat.checkSelfPermission(BaseApplication.context, p)
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p)
            }
        }
        return if (!listPermissionsNeeded.isEmpty()) {
            requestPermissions(listPermissionsNeeded.toTypedArray(), STORAGE_PERMISSION_REQUEST)
            false
        } else {
            true
        }
    }

    private fun chkCameraPermission(): Boolean {
        var result: Int
        val permissions = arrayOf(Manifest.permission.CAMERA)
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        for (p in permissions) {
            result = ContextCompat.checkSelfPermission(requireContext(), p)
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p)
            }
        }
        return if (!listPermissionsNeeded.isEmpty()) {
            requestPermissions(listPermissionsNeeded.toTypedArray(), CAMERA_PERMISSION_REQUEST)
            false
        } else {
            true
        }
    }

    @JvmName("setListener1")
    fun setListener(listener: onUpdate) {
        this.listener = listener
    }

    private fun callGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK
        startActivityForResult(Intent.createChooser(intent, "Select Source"), RESULT_LOAD_IMAGE)
    }

    private fun callCamera() {
        val takePictureIntent = Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA)
        if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
                Log.e(TAG, "Create Image File")
            } catch (ex: IOException) {
                Log.e(TAG, "Error File$ex")
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                imageUri = FileProvider.getUriForFile(
                    requireActivity(),
                    requireActivity().packageName + ".provider",
                    photoFile
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                startActivityForResult(takePictureIntent, CAMERA_REQUEST)
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir =
            BaseApplication.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        imgPath = image.absolutePath
        return image
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e(TAG, "onActivityResult: ")
        if (resultCode == Activity.RESULT_OK) {
            val uri: Uri?
            if (requestCode == RESULT_LOAD_IMAGE) {
                if (data != null) {
                    uri = data.data
                    if (uri != null) {
                        Log.e("uriname=>", "" + uri)
                        imgPath = getRealPathFromUri(BaseApplication.context, uri)
                        Log.e(TAG, "Gallery Path: $imgPath")
                        if (listener != null) {
                            listener!!.set(imgPath!!)
                        }
                        dismiss()
                    } else {
                        Log.e(TAG, "uri null: ")
                    }
                }
            } else if (requestCode == CAMERA_REQUEST) {
                Log.e("CAMERA_REQUEST", "load 1")
                val cr = BaseApplication.context.contentResolver
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(cr, imageUri)
                    uri = getImageUri(BaseApplication.context, bitmap)
                    if (uri != null) {
                        imgPath = getRealPathFromUri(BaseApplication.context, uri)
                        Log.e(TAG, "Camera Path: $imgPath")
                        if (listener != null) {
                            listener!!.set(imgPath!!)
                        }
                        dismiss()
                    } else {
                        Log.e(TAG, "Uri Null")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    e.localizedMessage
                    Log.e("CAMERA_REQUEST", "Failed to load", e)
                }
            }
        } else {
            dismiss()
        }
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST -> {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, "Camera permission require", Toast.LENGTH_SHORT).show()
                } else {
                    callCamera()
                }
            }
            STORAGE_PERMISSION_REQUEST -> {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, "Storgae permission require", Toast.LENGTH_SHORT).show()
                } else {
                    callGallery()
                }
            }
        }
    }

    fun compressImage(filePath: String?): String {

        //String filePath = getRealPathFromURI(imageUri);
        var scaledBitmap: Bitmap? = null
        val options = BitmapFactory.Options()

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true
        var bmp = BitmapFactory.decodeFile(filePath, options)
        var actualHeight = options.outHeight
        var actualWidth = options.outWidth

//      max Height and width values of the compressed image is taken as 816x612
        val maxHeight = 512.0f
        val maxWidth = 512.0f
        var imgRatio = (actualWidth / actualHeight).toFloat()
        val maxRatio = maxWidth / maxHeight

//      width and height values are set maintaining the aspect ratio of the image
        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight
                actualWidth = (imgRatio * actualWidth).toInt()
                actualHeight = maxHeight.toInt()
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth
                actualHeight = (imgRatio * actualHeight).toInt()
                actualWidth = maxWidth.toInt()
            } else {
                actualHeight = maxHeight.toInt()
                actualWidth = maxWidth.toInt()
            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true
        options.inInputShareable = true
        options.inTempStorage = ByteArray(16 * 1024)
        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }
        val ratioX = actualWidth / options.outWidth.toFloat()
        val ratioY = actualHeight / options.outHeight.toFloat()
        val middleX = actualWidth / 2.0f
        val middleY = actualHeight / 2.0f
        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)
        val canvas = Canvas(scaledBitmap!!)
        canvas.setMatrix(scaleMatrix)
        canvas.drawBitmap(
            bmp,
            middleX - bmp.width / 2,
            middleY - bmp.height / 2,
            Paint(Paint.FILTER_BITMAP_FLAG)
        )

//      check the rotation of the image and display it properly
        val exif: ExifInterface
        try {
            exif = ExifInterface(filePath!!)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION, 0
            )
            LogUtils.Print("EXIF", "Exif: $orientation")
            val matrix = Matrix()
            if (orientation == 6) {
                matrix.postRotate(90f)
                LogUtils.Print("EXIF", "Exif: $orientation")
            } else if (orientation == 3) {
                matrix.postRotate(180f)
                LogUtils.Print("EXIF", "Exif: $orientation")
            } else if (orientation == 8) {
                matrix.postRotate(270f)
                LogUtils.Print("EXIF", "Exif: $orientation")
            }
            scaledBitmap = Bitmap.createBitmap(
                scaledBitmap, 0, 0,
                scaledBitmap.width, scaledBitmap.height, matrix,
                true
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
        var out: FileOutputStream? = null
        val filename = filename
        try {
            out = FileOutputStream(filename)

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap!!.compress(Bitmap.CompressFormat.JPEG, 90, out)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return filename
    }

    val filename: String
        get() {
            val file =
                File(Environment.getExternalStorageDirectory().path, ".rusocial/Images")
            if (!file.exists()) {
                file.mkdirs()
            }
            return file.absolutePath + "/" + System.currentTimeMillis() + ".jpg"
        }

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        val totalPixels = (width * height).toFloat()
        val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++
        }
        return inSampleSize
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.llCamera -> {
                if (chkAllPermission(CAMERA_PERMISSION_REQUEST)) callCamera()
            }
            R.id.llGallery -> {
                if (chkAllPermission(STORAGE_PERMISSION_REQUEST)) callGallery()
            }
            R.id.tvCancel -> {
                dismiss()
            }
        }
    }

    // Interface
    interface onUpdate {
        fun set(imagePath: String)
    }

    companion object {
        private const val CAMERA_REQUEST = 1
        private const val STORAGE_REQUEST = 2
        private const val RESULT_LOAD_IMAGE = 2
        private const val CAMERA_PERMISSION_REQUEST = 101
        private const val STORAGE_PERMISSION_REQUEST = 102
        private const val TAG = "ImagePicker"
        fun getRealPathFromUri(context: Context, contentUri: Uri?): String {
            var cursor: Cursor? = null
            return try {
                val proj = arrayOf(MediaStore.Images.Media.DATA)
                cursor = context.contentResolver.query(contentUri!!, proj, null, null, null)
                val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                cursor.moveToFirst()
                cursor.getString(column_index)
            } finally {
                cursor?.close()
            }
        }
    }
}