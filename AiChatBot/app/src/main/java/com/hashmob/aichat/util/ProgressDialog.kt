package com.hashmob.aichat.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.hashmob.aichat.R

class ProgressDialog {
    private var dialog: Dialog? = null
    fun show(context: Context?) {
        if (dialog != null && dialog!!.isShowing) {
            return
        }
        dialog = Dialog(context!!)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.progressbar_dialog_layout)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.show()
    }

    fun dismiss() {
        if (dialog != null && dialog!!.isShowing) {
            dialog!!.dismiss()
        }
    }

    companion object {
        private var mInstance: ProgressDialog? = null

        @get:Synchronized
        val instance: ProgressDialog?
            get() {
                if (mInstance == null) mInstance = ProgressDialog()
                return mInstance
            }
    }
}