package com.deliverybook.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import com.deliverybook.core.AppConstants
import com.deliverybook.data.local.ContactEntity
import com.deliverybook.data.local.DeliveryBookDatabase
import com.deliverybook.domain.model.Contact
import com.deliverybook.domain.repository.ContactsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ContactsRepositoryImpl(
    private val database: DeliveryBookDatabase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ContactsRepository {

    private val contactsDao = database.contactsDao()

    override fun observeRecentContacts(limit: Int): Flow<List<Contact>> =
        contactsDao.getRecent(limit).map { list -> list.map { it.toDomain() } }

    override fun observeContactsCount(): Flow<Int> =
        contactsDao.observeContactsCount()

    override fun searchContactsPaged(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = AppConstants.Paging.PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                contactsDao.searchPaged("%$query%")
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }

    override suspend fun getContactByDni(dni: String): Contact? =
        withContext(ioDispatcher) {
            contactsDao.getByDni(dni)?.toDomain()
        }

    override suspend fun upsertContact(contact: Contact) {
        withContext(ioDispatcher) {
            contactsDao.upsert(contact.toEntity())
        }
    }

    override suspend fun deleteContact(dni: String) {
        withContext(ioDispatcher) {
            contactsDao.deleteByDni(dni)
        }
    }

    override suspend fun markContactAsSearched(dni: String, timestampMillis: Long) {
        withContext(ioDispatcher) {
            val existing = contactsDao.getByDni(dni) ?: return@withContext
            contactsDao.upsert(
                existing.copy(
                    lastSearchedAtMillis = timestampMillis
                )
            )
        }
    }

    override suspend fun removeFromRecent(dni: String) {
        withContext(ioDispatcher) {
            contactsDao.clearLastSearched(dni)
        }
    }

    override suspend fun clearAllRecents() {
        withContext(ioDispatcher) {
            contactsDao.clearAllLastSearched()
        }
    }

    override suspend fun addNeighbor(dni: String, neighbor: String) {
        if (neighbor.isBlank()) return
        withContext(ioDispatcher) {
            val existing = contactsDao.getByDni(dni) ?: return@withContext
            val updated = existing.neighbors + neighbor
            contactsDao.upsert(existing.copy(neighbors = updated))
        }
    }

    override suspend fun updateNeighbor(dni: String, index: Int, newValue: String) {
        if (newValue.isBlank()) return
        withContext(ioDispatcher) {
            val existing = contactsDao.getByDni(dni) ?: return@withContext
            if (index !in existing.neighbors.indices) return@withContext
            val mutable = existing.neighbors.toMutableList()
            mutable[index] = newValue
            contactsDao.upsert(existing.copy(neighbors = mutable.toList()))
        }
    }

    override suspend fun deleteNeighbor(dni: String, index: Int) {
        withContext(ioDispatcher) {
            val existing = contactsDao.getByDni(dni) ?: return@withContext
            if (index !in existing.neighbors.indices) return@withContext
            val mutable = existing.neighbors.toMutableList()
            mutable.removeAt(index)
            contactsDao.upsert(existing.copy(neighbors = mutable.toList()))
        }
    }
}

private fun ContactEntity.toDomain(): Contact =
    Contact(
        dni = dni,
        name = name,
        address = address,
        neighbors = neighbors,
        lastSearchedAtMillis = lastSearchedAtMillis
    )

private fun Contact.toEntity(): ContactEntity =
    ContactEntity(
        dni = dni,
        name = name,
        address = address,
        neighbors = neighbors,
        lastSearchedAtMillis = lastSearchedAtMillis
    )

