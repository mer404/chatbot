package com.hashmob.aichat.util

import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import androidx.appcompat.widget.AppCompatImageView
import com.hashmob.aichat.R

object PasswordUtils {

    fun showHidePass(view: AppCompatImageView, editText: EditText) {

        if (editText.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
            view.setImageResource(R.drawable.ic_show_password)

            //Show Password
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
        } else {
            view.setImageResource(R.drawable.ic_hide_password);

            //Hide Password
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());

        }

    }


}