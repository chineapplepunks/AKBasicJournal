package com.akb.journal.dao

import androidx.room.*
import com.akb.journal.entity.Password

@Dao
interface PasswordDao {
    @Insert
    suspend fun insert(password: Password)
    @Update
    suspend fun update(password: Password)
    @Delete
    suspend fun delete(password: Password)
    @Query ("SELECT * FROM Password LIMIT 1")
    suspend fun getPassword() : Password?
    @Query ("SELECT hash FROM Password LIMIT 1")
    suspend fun getHash(): String?
    @Query ("DELETE FROM Password")
    suspend fun deleteAll()
    @Query ("UPDATE Password SET hash = :hash")
    suspend fun replace(hash: String)
}