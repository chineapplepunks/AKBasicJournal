package com.akb.journal

import android.content.Context
import com.akb.journal.entity.Entry
import com.akb.journal.entity.Password

/**
 * Repository class.
 *
 * Allows us to access data from an external source.
 * In this case we are simply accessing the Android's local SQLite database.
 *
 * @author Adam Brooke
 *
 * @param context The application context.
 */
class Repository(context: Context) {
    // The database.
    private var dbase = EntryDatabase.create(context)
    // The DAO associated with diary entries.
    private var entryDao = dbase.entryDao()
    // The DAO associated with passwords.
    private var passwordDao = dbase.passwordDao()

    /**
     * Retrieves an entry from the database.
     *
     * @param date The date of the entry to retrieve.
     *
     * @return The entry.
     */
    suspend fun getEntry(date: String) : Entry? {
        return entryDao.getEntry(date)
    }

    suspend fun getEntries() : List<Entry> {
        return entryDao.getEntries()
    }

    /**
     * Saves an entry into the database.
     *
     * @param entry The entry to save.
     */
    suspend fun saveEntry(entry: Entry) {
        return if(entryDao.getEntry(entry.date) != null) {
            entryDao.update(entry)
        } else {
            entryDao.insert(entry)
        }
    }

    /**
     * Deletes all diary entries from the database.
     */
    suspend fun deleteAllEntries() {
        return entryDao.deleteAll()
    }

    /**
     * Deletes a single entry from the database.
     *
     * @param entry The entry to delete.
     */
    suspend fun deleteEntry(entry: Entry) {
        return entryDao.delete(entry)
    }

    /**
     * Retrieves the password hash from the database.
     *
     * @return The hash.
     */
    suspend fun getHash(): String? {
        return passwordDao.getHash()
    }

    /**
     * Stores a password hash into the database.
     *
     * @param hash The password hash to store in the database.
     */
    suspend fun insertHash(hash: String) {
        val password = Password(hash)
        if(passwordDao.getPassword() != null) {
            passwordDao.replace(password.hash)
        } else {
            passwordDao.insert(password)
        }
    }

    /**
     * Deletes the password from the database.
     */
    suspend fun deletePassword() {
        passwordDao.deleteAll()
    }
}
