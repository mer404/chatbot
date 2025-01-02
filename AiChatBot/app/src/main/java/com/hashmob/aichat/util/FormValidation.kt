package com.hashmob.aichat.util

import android.app.Activity
import android.text.TextUtils
import android.util.Patterns
import android.widget.RatingBar
import android.widget.Spinner

class FormValidation(private val activity: Activity) {
    fun isEmpty(value: String?, err: String?): Boolean {
        return if (value != null && !TextUtils.isEmpty(value)) {
            true
        } else {
            Utils.showSnackBar(err, activity)
            false
        }
    }

    fun isValidEmail(email: String?, errEmpty: String?, errValid: String?): Boolean {
        if (!isEmpty(email, errEmpty)) {
            return false
        }
        return if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            true
        } else {
            Utils.showSnackBar(errValid, activity)
            false
        }
    }

    fun isValidPassword(password: String, errEmpty: String?, errLength: String?): Boolean {
        if (!isEmpty(password, errEmpty)) {
            return false
        }
        return if (password.length >= 6) {
            true
        } else {
            Utils.showSnackBar(errLength, activity)
            false
        }
    }

    fun isNewOldSame(oldpassword: String,newPassword:String, err: String?): Boolean {
        if (!oldpassword.equals(newPassword)) {
            return true
        }
       else {
            Utils.showSnackBar(err, activity)
           return false
        }
    }

    fun isValidPhone(phone: String?, errEmpty: String?, errInvalid: String?): Boolean {
        if (!isEmpty(phone, errEmpty)) {
            return false
        }
        return if (phone != null && phone.length > 7) {
            true
        } else {
            Utils.showSnackBar(errInvalid, activity)
            false
        }
    }

    fun isPasswordMatch(password: String, confirmPassword: String, err: String?): Boolean {
        return if (password == confirmPassword) {
            true
        } else {
            Utils.showSnackBar(err, activity)
            false
        }
    }

    fun isPositionInitial(spinner: Spinner, err: String?): Boolean {
        return if (spinner.selectedItemPosition == 0) {
            Utils.showSnackBar(err, activity)
            true
        } else {
            false
        }
    }

    fun isValidOTP(otp: String?): Boolean {
        //            Utils.makeToast(activity, activity.getResources().getString(R.string.otp_invalid));
        return otp != null && otp.length >= 6
    }

    fun isValidRating(otp: RatingBar?, err: String?): Boolean {
        return if (otp != null && otp.rating.toDouble() != 0.0) {
            true
        } else {
            Utils.showSnackBar(err, activity)
            false
        }
    }
    fun isValidAge(value: String?, err: String?): Boolean {
        return if (value != null && !value.equals("Age",true)) {
            true
        } else {
            Utils.showSnackBar(err, activity)
            false
        }
    }
    fun isValidGender(value: String?, err: String?): Boolean {
        return if (value != null && !value.equals("Gender",true)) {
            true
        } else {
            Utils.showSnackBar(err, activity)
            false
        }
    }
}