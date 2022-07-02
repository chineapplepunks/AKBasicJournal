package com.akb.journal.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class that represents an entry.
 *
 * An entry consists of a date with an associated string of text, which forms the entry.
 */
@Entity(tableName = "Entry")
data class Entry(
    @PrimaryKey
    var date: String,
    var text: String
)
