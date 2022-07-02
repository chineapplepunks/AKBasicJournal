package com.akb.journal.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class that represents a password.
 *
 * Passwords are stored in the database as a hash.
 */
@Entity(tableName="Password")
data class Password (
    @PrimaryKey
    var hash: String
)
