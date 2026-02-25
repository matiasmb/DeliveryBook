package com.deliverybook.data

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.room.Room
import com.google.common.truth.Truth.assertThat
import com.deliverybook.data.local.DeliveryBookDatabase
import com.deliverybook.data.repository.ContactsRepositoryImpl
import com.deliverybook.domain.model.Contact
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
@Config(manifest = Config.NONE)
class ContactsRepositoryImplTest {

    private lateinit var database: DeliveryBookDatabase
    private lateinit var repository: ContactsRepositoryImpl

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Application>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            DeliveryBookDatabase::class.java
        ).allowMainThreadQueries().build()

        repository = ContactsRepositoryImpl(database)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun upsertAndGetContact_roundTrip() {
        runBlocking {
            val contact = Contact(
                dni = "12345678",
                name = "Juan Pérez",
                address = "Calle Falsa 123",
                neighbors = listOf("Vecino 1", "Vecino 2"),
                lastSearchedAtMillis = null
            )

            repository.upsertContact(contact)

            val loaded = repository.getContactByDni("12345678")

            assertThat(loaded).isNotNull()
            assertThat(loaded?.dni).isEqualTo(contact.dni)
            assertThat(loaded?.neighbors).containsExactlyElementsIn(contact.neighbors)
        }
    }
}
