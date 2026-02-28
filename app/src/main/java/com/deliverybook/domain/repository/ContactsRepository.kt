package com.deliverybook.domain.repository

import androidx.paging.PagingData
import com.deliverybook.domain.model.Contact
import kotlinx.coroutines.flow.Flow

interface ContactsRepository {
    fun observeRecentContacts(limit: Int = 10): Flow<List<Contact>>
    fun observeContactsCount(): Flow<Int>
    fun searchContactsPaged(query: String): Flow<PagingData<Contact>>

    suspend fun getContactByDni(dni: String): Contact?
    suspend fun upsertContact(contact: Contact)
    suspend fun deleteContact(dni: String)

    suspend fun markContactAsSearched(dni: String, timestampMillis: Long = System.currentTimeMillis())
    suspend fun removeFromRecent(dni: String)
    suspend fun clearAllRecents()

    suspend fun addNeighbor(dni: String, neighbor: String)
    suspend fun updateNeighbor(dni: String, index: Int, newValue: String)
    suspend fun deleteNeighbor(dni: String, index: Int)
}

