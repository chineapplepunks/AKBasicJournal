package com.akb.journal

import androidx.lifecycle.ViewModel
import com.akb.journal.viewmodel.SettingsViewModel
import com.akb.journal.viewmodel.EntryViewModel
import com.akb.journal.viewmodel.PasswordViewModel
import java.util.*

/**
 * ViewModel that allows data to be managed across Fragments.
 *
 * Holds the EntryViewModel, PasswordViewModel and the login hash.
 *
 * @author Adam Brooke
 */
class Communicator : ViewModel() {
    lateinit var entryViewModel: EntryViewModel
    lateinit var passwordViewModel: PasswordViewModel
    lateinit var settingsViewModel: SettingsViewModel
    lateinit var date: Date
    var loginHash: String? = null
}