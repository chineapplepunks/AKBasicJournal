package com.akb.journal

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.akb.journal.dao.EntryDao
import com.akb.journal.dao.PasswordDao
import com.akb.journal.entity.Entry
import com.akb.journal.entity.Password
import java.io.OutputStream

@Database(
    entities = [Entry::class, Password::class],
    version = 1
)

/**
 * The database.
 *
 * Holds a table for journal entries and a table for passwords.
 *
 * Uses the Room API to ensure data persistence, and the DAO interface to
 * interact with the SQLite database.
 */
abstract class EntryDatabase : RoomDatabase() {
    abstract fun entryDao() : EntryDao
    abstract fun passwordDao(): PasswordDao

    companion object {
        private lateinit var instance: EntryDatabase

        /**
         * Builds the database.
         *
         * @param context The application context.
         */
        private const val DB_NAME = "journal.db"

        fun create(context: Context): EntryDatabase {
            instance = Room.databaseBuilder(
                context,
                EntryDatabase::class.java,
                DB_NAME
            ).setJournalMode(JournalMode.TRUNCATE).build()

            return instance
        }


        /**
         * Exports (backup really) the database.
         *
         * @param context The application context.
         * @param uri The Uri of the file to export to.
         */
        fun export(context: Context, uri: Uri?) {
            val currentDB = context.getDatabasePath(DB_NAME).absoluteFile
            val inputStream = context.contentResolver.openInputStream(currentDB.toUri())
            val outputStream: OutputStream? = context.contentResolver.openOutputStream(uri!!)

            instance.close()

            inputStream.use { input ->
                outputStream.use{ output ->
                    input?.copyTo(output!!)
                }
            }

            outputStream?.flush()
            outputStream?.close()
            inputStream?.close()
        }

        /**
         * Imports (restores) the database from a file.
         *
         * @param context The application context.
         * @param uri The Uri of the file to restore from.
         */
        fun import(context: Context, uri: Uri?) {
            val currentDB = context.getDatabasePath(DB_NAME).absoluteFile
            val inputStream = context.contentResolver.openInputStream(uri!!)
            val outputStream = context.contentResolver.openOutputStream(currentDB.toUri())

            instance.close()

            inputStream.use { input ->
                outputStream.use{ output ->
                    input?.copyTo(output!!)
                }
            }

            outputStream?.flush()
            outputStream?.close()
            inputStream?.close()
        }
    }
}