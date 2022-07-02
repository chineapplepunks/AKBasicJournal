
package com.akb.journal

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*

/**
 * A bunch of static utility functions.
 * This is considered bad OO design, but I don't care. :-D
 *
 * @author Adam Brooke
 *
 */
class Util {
    companion object {
        /**
         * Hides the keyboard on a fragment.
         */
        fun Fragment.hideKeyboard() {
            view?.let { activity?.hideKeyboard(it) }
        }

        /**
         * Hides the keyboard on an activity.
         */
        fun Activity.hideKeyboard() {
            hideKeyboard(currentFocus ?: View(this))
        }

        /**
         * Hides the keyboard on a view.
         *
         * @param view The view that you want to hide the keyboard from.
         */
        fun Context.hideKeyboard(view: View) {
            val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }

        /**
         * Simple function that formats a string based on a date.
         *
         * @param year The year of the date.
         * @param month The month of the date. (0 Jan - 11 Dec)
         * @param day The day of the month (1 - 31)
         *
         * @return The string formatted in "dd-MM-yyyy" form
         */
        fun formatDate(year: Int, month: Int, day: Int) : String {
            val cal = Calendar.getInstance()
            cal.set(year,month,day)
            val date = cal.time
            val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            return sdf.format(date)
        }

        /**
         * Function that generates a SHA-256 hash based on a plan text string.
         *
         * @param plainText The string to generate the hash from.
         *
         * @return a SHA-256 encoded hash string.
         */
        fun generateHash(plainText: String) : String {
            val hexArray = "0123456789ABCDEF"
            val bytes = MessageDigest
                .getInstance("SHA-256")
                .digest(plainText.toByteArray())
            val hash = StringBuilder(bytes.size * 2)
            bytes.forEach {
                val i = it.toInt()
                hash.append(hexArray[i shr 4 and 0x0f])
                hash.append(hexArray[i and 0x0f])
            }

            return hash.toString()
        }
    }
}