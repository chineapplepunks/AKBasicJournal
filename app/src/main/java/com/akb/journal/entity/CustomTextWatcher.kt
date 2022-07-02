package com.akb.journal.entity

import android.text.TextWatcher

abstract class CustomTextWatcher : TextWatcher {
    var beforeChange: CharSequence? = null
    var afterChange: CharSequence? = null

        fun reset() {
            beforeChange = null
            afterChange = null
        }
}