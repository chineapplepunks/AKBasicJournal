package com.akb.journal.preference

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import androidx.preference.PreferenceManager
import com.akb.journal.entity.Entry
import java.io.PrintWriter

class Settings(val context: Context) {
    private val settings: SharedPreferences? = PreferenceManager.getDefaultSharedPreferences(context)

    fun getFontSize() : Float {
        val fontSize = settings?.getString("font_size","")
        if(fontSize.isNullOrBlank()) {
            return 16F
        } else {
            return fontSize.toFloat()
        }
    }

    fun export(entries: List<Entry>,
               uri: Uri?) {
        val outputStream = context.contentResolver.openOutputStream(uri!!)
        var export = ""
        val it = entries.iterator()
        while (it.hasNext()) {
            val entry = it.next()
            export += entry.date + "\n"
            export += entry.text + "\n\n"
        }
        val writer = PrintWriter(outputStream!!)
        writer.print(export.dropLast(2))
        writer.close()
    }
}