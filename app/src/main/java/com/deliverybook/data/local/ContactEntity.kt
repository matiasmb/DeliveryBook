package com.deliverybook.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class ContactEntity(
    @PrimaryKey val dni: String,
    val name: String,
    val address: String,
    val neighbors: List<String>,
    val lastSearchedAtMillis: Long?
)

