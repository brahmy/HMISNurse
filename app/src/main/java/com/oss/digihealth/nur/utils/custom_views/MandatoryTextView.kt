package com.oss.digihealth.nur.utils.custom_views

import android.content.Context
import android.os.Build
import android.text.Html
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class MandatoryTextView(context: Context, attributeSet: AttributeSet) :
    AppCompatTextView(context, attributeSet) {

    init {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            this.text = Html.fromHtml(
                "<font>${this.text}</font><font color=\"#FF0000\">*</font>",
                Html.FROM_HTML_MODE_COMPACT
            )
        } else {
            this.text = Html.fromHtml(
                "<font>$this.text</font><font color=\"#FF0000\">*</font>"
            )
        }
    }
}
