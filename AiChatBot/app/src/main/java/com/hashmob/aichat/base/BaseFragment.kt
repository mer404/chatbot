package com.hashmob.aichat.base


import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.NonNull
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.hashmob.aichat.R
import com.hashmob.aichat.util.*
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


abstract class BaseFragment : Fragment() {

    @Inject
    lateinit var preferences: Preferences
    protected abstract fun initViewModel()
    protected abstract fun observeViewModel()

    var baseActivity: BaseActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
        baseActivity = activity as BaseActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun showProgressDialog() {
        baseActivity?.showProgress()
    }

    fun dismissProgressDialog() {
        baseActivity?.hideProgress()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        observeViewModel()
    }

    fun replaceFragment(
        @NonNull fragment: Fragment,
        backStackName: Boolean = false,
        popStack: Boolean = false,
        aTAG: String = "",
        @IdRes containerViewId: Int = R.id.container
    ) {
        baseActivity!!.replaceFragment(fragment, backStackName, popStack, aTAG, containerViewId)
    }

    fun addFragment(
        @NonNull fragment: Fragment,
        backStackName: Boolean = false,
        aTAG: String = "",
        @IdRes containerViewId: Int = R.id.container

    ) {
        baseActivity!!.addFragment(fragment, backStackName, aTAG, containerViewId)
    }

    public fun isFragmentAlreadyInStack(tag1: String): Boolean {
        if (baseActivity!!.supportFragmentManager.backStackEntryCount > 0) {
            val backEntry =
                baseActivity!!.supportFragmentManager.getBackStackEntryAt(baseActivity!!.supportFragmentManager.backStackEntryCount - 1)
            val tag = backEntry.name

            return (tag.equals(tag1))
        } else {
            return false
        }
    }

    fun makeLinks(textView: TextView, vararg links: Pair<String, View.OnClickListener>) {
        var textView: TextView = textView
        if (!textView.text.isEmpty()) {

            val spannableString = SpannableString(textView.text)
            for (link in links) {
                val clickableSpan = object : ClickableSpan() {
                    override fun onClick(view: View) {
                        Selection.setSelection((view as TextView).text as Spannable, 0)
                        view.invalidate()
                        link.second.onClick(view)
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        ds.isUnderlineText = false
                    }
                }
                val startIndexOfLink = textView.text.toString().indexOf(link.first)
                spannableString.setSpan(
                    clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                val myTypeface = Typeface.create(
                    ResourcesCompat.getFont(requireContext(), R.font.inter_bold),
                    Typeface.NORMAL
                )

                spannableString.setSpan(
                    CustomTypefaceSpan("", myTypeface),
                    startIndexOfLink,
                    startIndexOfLink + link.first.length,
                    Spanned.SPAN_EXCLUSIVE_INCLUSIVE
                );


            }



            textView.movementMethod =
                LinkMovementMethod.getInstance() // without LinkMovementMethod, link can not click
            textView.setText(spannableString, TextView.BufferType.SPANNABLE)
        }
    }


    open fun showProgress() {
        ProgressDialog.instance?.show(requireContext())
    }

    open fun hideProgress() {
        ProgressDialog.instance?.dismiss()
    }

    protected open fun showMessage(message: String?) {
        Utils.makeToast(requireContext(), message)
    }

    protected open fun shoSwMessage(message: String?) {
        Utils.showSnackBar(message, requireActivity())
    }

    fun getValueFromBundle(bundle: Bundle?, keyName: String): String {
        if (bundle != null) {
            if (bundle.containsKey(keyName)) {
                return bundle.getString(keyName).toString()
            } else {
                return ""
            }
        } else {
            return ""
        }
    }

    fun getBooleanValueFromBundle(bundle: Bundle?, keyName: String): Boolean {
        if (bundle != null) {
            if (bundle.containsKey(keyName)) {
                return bundle.getBoolean(keyName)
            } else {
                return false
            }
        } else {
            return false
        }
    }
}
