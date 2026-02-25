package com.deliverybook.domain.model

data class Contact(
    val dni: String,
    val name: String,
    val address: String,
    val neighbors: List<String>,
    val lastSearchedAtMillis: Long?
)

