package com.akb.journal.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.akb.journal.Repository
import com.akb.journal.entity.Entry
import kotlinx.coroutines.launch

/**
 * Holds the View Model for entries. Communicates between the UI and the Repository.
 *
 * @author Adam Brooke
 *
 * @param application The application.
 * @property repository The repository.
 *
 */
class EntryViewModel(application: Application,
                     private val repository: Repository) : AndroidViewModel(application) {
    /**
     * entry is a live data type that holds an entry retrieved from the repository.
     */
    var entry: MutableLiveData<Entry?> = MutableLiveData<Entry?>()
        private set
    var entries: MutableLiveData<List<Entry>> = MutableLiveData<List<Entry>>()
        private set
    /**
     * Retrieves an entry from the repository, and stores it in our local entry variable.
     *
     * @param date The date of the entry to retrieve.
     */
    fun getEntry(date: String) {
        viewModelScope.launch {
            entry.setValue(repository.getEntry(date))
        }
    }

    fun getEntries() {
        viewModelScope.launch {
            entries.setValue(repository.getEntries())
        }
    }

    /**
     * Saves an entry into the repository.
     *
     * @param entry The entry to save.
     */
    fun saveEntry(entry: Entry) {
        viewModelScope.launch {
            repository.saveEntry(entry)
        }
    }

    /**
     * Deletes an entry out of the repository.
     *
     * @param entry The entry to delete.
     */
    fun delete(entry: Entry) {
        viewModelScope.launch {
            repository.deleteEntry(entry)
        }
    }

    /**
     * Deletes all the entries from the repository.
     */
    fun deleteAll() {
        viewModelScope.launch {
            repository.deleteAllEntries()
        }
    }
}