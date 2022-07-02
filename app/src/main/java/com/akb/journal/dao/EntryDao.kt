package com.akb.journal.dao

import androidx.room.*
import com.akb.journal.entity.Entry

@Dao
interface EntryDao {
    @Insert
    suspend fun insert(entry: Entry)
    @Delete
    suspend fun delete(entry: Entry)
    @Update
    suspend fun update(entry: Entry)
    @Query ("SELECT * FROM Entry WHERE date = :date")
    suspend fun getEntry(date: String) : Entry?
    @Query ("SELECT * FROM Entry")
    suspend fun getEntries() : List<Entry>
    @Query ("DELETE FROM Entry")
    suspend fun deleteAll()
}