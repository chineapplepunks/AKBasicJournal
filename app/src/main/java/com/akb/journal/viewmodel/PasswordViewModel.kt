package com.akb.journal.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.akb.journal.Repository
import kotlinx.coroutines.*

/**
 * Holds the View Model for Passwords. Communicates between the UI and the repository.
 *
 * @author Adam Brooke
 *
 * @param application The application.
 * @property repository The repository.
 */
class PasswordViewModel (application: Application,
                         private val repository: Repository) : AndroidViewModel(application) {

    /**
     * Returns the password hash from the repository.
     * This function blocks the UI, so it should only be called once when the
     * application starts.
     *
     * @return the hash stored in the repository.
     */
    fun getHash() : String? = runBlocking {
        return@runBlocking repository.getHash()
    }

    /**
     * Saves the password hash into the repository.
     */
    fun saveHash(hash: String) {
        viewModelScope.launch {
            repository.insertHash(hash)
        }
    }

    fun deletePassword() {
        viewModelScope.launch {
            repository.deletePassword()
        }
    }
}