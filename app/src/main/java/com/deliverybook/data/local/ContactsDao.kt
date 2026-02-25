package com.deliverybook.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactsDao {

    @Query("SELECT * FROM contacts WHERE dni = :dni LIMIT 1")
    suspend fun getByDni(dni: String): ContactEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(contact: ContactEntity)

    @Query("DELETE FROM contacts WHERE dni = :dni")
    suspend fun deleteByDni(dni: String)

    @Query(
        """
        SELECT * FROM contacts
        WHERE dni LIKE :query
           OR name LIKE :query
           OR address LIKE :query
        ORDER BY name ASC
        """
    )
    fun searchPaged(query: String): PagingSource<Int, ContactEntity>

    @Query(
        """
        SELECT * FROM contacts
        WHERE lastSearchedAtMillis IS NOT NULL
        ORDER BY lastSearchedAtMillis DESC
        LIMIT :limit
        """
    )
    fun getRecent(limit: Int): Flow<List<ContactEntity>>
}

