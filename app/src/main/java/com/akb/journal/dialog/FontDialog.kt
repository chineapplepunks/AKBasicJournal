package com.akb.journal.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import com.akb.journal.R
import com.akb.journal.databinding.LayoutFontBinding
import com.akb.journal.viewmodel.SettingsViewModel

class FontDialog(
    context: Context,
    private val settings: SettingsViewModel) {

    private var binding = LayoutFontBinding.inflate(LayoutInflater.from(context))
    private var alert = AlertDialog.Builder(context).create()
    private var textArray: Array<String>
    private var valueArray: Array<String>

    init {
        alert.setView(binding.root)

        val rg: RadioGroup = binding.radioGroup

        textArray = context.resources.getStringArray(R.array.pref_font_size)
        valueArray = context.resources.getStringArray(R.array.pref_font_value)

        for(text in textArray) {
            val rb = RadioButton(context)
            rb.text = text
            rg.addView(rb)
        }

        for((index, value) in valueArray.withIndex()) {
            if(value.toFloat() == settings.getFontSize()) {
                val rb: RadioButton = rg[index] as RadioButton
                rb.isChecked = true
            }
        }
    }
    fun create() {
        val rg = binding.radioGroup

        binding.button3.setOnClickListener {
            for((index,_) in textArray.withIndex()) {
                val radioButton = rg[index] as RadioButton
                if(radioButton.isChecked) {
                    settings.setFontSize(valueArray[index])
                }
            }
            dismiss()
        }

        binding.button2.setOnClickListener {
            dismiss()
        }
    }

    fun show() {
        alert.show()
    }

    fun dismiss() {
        alert.dismiss()
    }
}