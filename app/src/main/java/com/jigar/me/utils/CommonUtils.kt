package com.jigar.me.utils

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.text.Html
import android.util.Log
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jigar.me.BuildConfig
import com.jigar.me.R
import com.jigar.me.data.model.data.LoginData
import com.jigar.me.data.model.data.PlanAssignFromAdminData
import com.jigar.me.data.pref.AppPreferencesHelper
import com.jigar.me.utils.Constants.PRODUCT_ID_1Year
import com.jigar.me.utils.Constants.PRODUCT_ID_All


object CommonUtils {
    external fun getOneSignalKey() : String
    external fun getOrganizerId() : String
    external fun getDatabaseKey() : String
    external fun getApiBaseUrl() : String

    fun logMultilineString(tag: String, data: String) {
        for (line in data.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
            logLargeString(tag, line)
        }
    }

    fun logLargeString(tag: String, data: String) {
        val CHUNK_SIZE = 4076 // Typical max logcat payload.
        var offset = 0
        while (offset + CHUNK_SIZE <= data.length) {
            Log.e(tag, data.substring(offset, CHUNK_SIZE.let { offset += it; offset }))
        }
        if (offset < data.length) {
            Log.e(tag, data.substring(offset))
        }
    }
    fun htmlToAnnotatedString(html: String): AnnotatedString {
        val spanned = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)

        return buildAnnotatedString {
            var start = 0

            spanned.getSpans(0, spanned.length, Any::class.java).forEach { span ->
                val spanStart = spanned.getSpanStart(span)
                val spanEnd = spanned.getSpanEnd(span)

                if (start < spanStart) {
                    append(spanned.substring(start, spanStart))
                }

                when (span) {
                    is android.text.style.StyleSpan -> {
                        withStyle(
                            SpanStyle(
                                fontWeight = if (span.style == Typeface.BOLD) FontWeight.Bold else FontWeight.Normal,
                                fontFamily =  if (span.style == Typeface.BOLD) FontFamily(Font(R.font.font_bold)) else FontFamily(Font(R.font.font_regular))
                            )
                        ) {
                            append(spanned.substring(spanStart, spanEnd))
                        }
                    }

                    else -> append(spanned.substring(spanStart, spanEnd))
                }

                start = spanEnd
            }

            if (start < spanned.length) {
                append(spanned.substring(start))
            }
        }
    }
    @SuppressLint("RestrictedApi")
    fun setErrorToEditText(textInputLayout: TextInputLayout, validation_message: String?) {
        textInputLayout.error = validation_message
        textInputLayout.requestFocus()
    }
    fun removeError(textInputLayout: TextInputLayout) {
        textInputLayout.error = null
        textInputLayout.isErrorEnabled = false
    }

    fun removeTrailingZero(formattingInput: String): String {
        if (!formattingInput.contains(".")) {
            return formattingInput
        }
        val dotPosition = formattingInput.indexOf(".")
        val newValue = formattingInput.substring(dotPosition, formattingInput.length)
        return if (newValue == ".0") {
            formattingInput.substring(0, dotPosition)
        } else formattingInput
    }

    fun checkLevelIsPurchase(name: String, prefManager: AppPreferencesHelper): Boolean {
//        if (BuildConfig.DEBUG){
//            return true
//        }
        var isPurchased = false
        val loginData = Gson().fromJson(prefManager.getLoginData(), LoginData::class.java)
        if (loginData?.email.equals("abacus@yopmail.com")){
            isPurchased = true
        }else{
            if (prefManager.getCustomParam(Constants.PLAN_ASSIGN_FROM_ADMIN_DATA,"").isNotEmpty()) {
                val planListData : List<PlanAssignFromAdminData> = Gson().fromJson(
                    prefManager.getCustomParam(Constants.PLAN_ASSIGN_FROM_ADMIN_DATA, ""),
                    object : TypeToken<List<PlanAssignFromAdminData>>() {}.type
                )
                planListData.find { it.google_order_id == null &&
                        (it.google_plan_id?.contains(name) == true
                        || it.google_plan_id?.contains(PRODUCT_ID_All) == true
                        || it.google_plan_id?.contains(PRODUCT_ID_1Year) == true)
                }.also {
                    isPurchased = it != null
                }
            }
        }
        return isPurchased
    }

    fun checkPurchaseForExerciseExamCCM(prefManager: AppPreferencesHelper): Boolean {
//        if (BuildConfig.DEBUG){
//            return true
//        }
        val loginData = Gson().fromJson(prefManager.getLoginData(), LoginData::class.java)
        var isPurchased = false
        if (loginData?.email.equals("abacus@yopmail.com")){
            isPurchased = true
        }else{
            if (prefManager.getCustomParam(Constants.PLAN_ASSIGN_FROM_ADMIN_DATA,"").isNotEmpty()) {
                val planListData : List<PlanAssignFromAdminData> = Gson().fromJson(
                    prefManager.getCustomParam(Constants.PLAN_ASSIGN_FROM_ADMIN_DATA, ""),
                    object : TypeToken<List<PlanAssignFromAdminData>>() {}.type
                )
                planListData.find { it.google_order_id == null && (
//                        (it.google_plan_id?.contains("level1") == true) ||
//                        (it.google_plan_id?.contains("level2") == true) ||
                            (it.google_plan_id?.contains("level3") == true) ||
                            (it.google_plan_id?.contains("level4") == true) ||
                            (it.google_plan_id?.contains("level5") == true) ||
                            (it.google_plan_id?.contains("level6") == true) ||
                            (it.google_plan_id?.contains("level7") == true) ||
                            (it.google_plan_id?.contains("level8") == true) ||
                            (it.google_plan_id?.contains(PRODUCT_ID_1Year) == true) ||
                            (it.google_plan_id?.contains(PRODUCT_ID_All) == true)
                        ) }.also {
                    isPurchased = it != null
                }
            }
        }

        return isPurchased
    }
}