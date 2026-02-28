package com.deliverybook.domain.usecase

import com.deliverybook.domain.model.Contact
import com.deliverybook.domain.repository.ContactsRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class UseCaseDelegationTests {

    private val repository: ContactsRepository = mockk(relaxed = true)

    @Test
    fun deleteContact_delegatesToRepository() = runTest {
        DeleteContactUseCase(repository)("123")

        coVerify(exactly = 1) { repository.deleteContact("123") }
    }

    @Test
    fun upsertContact_delegatesToRepository() = runTest {
        val contact = Contact(
            dni = "1",
            name = "Juan",
            address = "Calle 1",
            neighbors = emptyList(),
            lastSearchedAtMillis = null
        )

        UpsertContactUseCase(repository)(contact)

        coVerify(exactly = 1) { repository.upsertContact(contact) }
    }

    @Test
    fun markContactAsSearched_delegatesToRepository_withTimestamp() = runTest {
        MarkContactAsSearchedUseCase(repository)("999")

        // Ojo: repository tiene default param -> se invoca con timestamp real
        coVerify(exactly = 1) { repository.markContactAsSearched("999", any()) }
    }
}
