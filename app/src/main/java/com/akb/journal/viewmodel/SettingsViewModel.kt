package com.akb.journal.viewmodel

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.preference.PreferenceManager

/**
 * Holds the settings view model. These are settings that can be viewed across
 * the fragments.
 *
 * @author Adam Brooke
 */
class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private var settings: SharedPreferences? = PreferenceManager.getDefaultSharedPreferences(
        application.applicationContext)

    /**
     * Retrieves the current font size stored in the preference manager.
     *
     * @return The font size.
     */
    fun getFontSize() : Float {
        val fontSize = settings?.getString("font_size","")
        if(fontSize.isNullOrBlank()) {
            return 16F
        } else {
            return fontSize.toFloat()
        }
    }

    fun setFontSize(size: String) {
        settings?.edit()?.putString("font_size", size)?.commit()
    }
}