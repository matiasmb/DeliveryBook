package com.deliverybook.domain.usecase

import com.deliverybook.domain.model.Contact
import com.deliverybook.domain.repository.ContactsRepository

class UpsertContactUseCase(
    private val repository: ContactsRepository
) {
    suspend operator fun invoke(contact: Contact) {
        repository.upsertContact(contact)
    }
}

