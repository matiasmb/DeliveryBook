package com.deliverybook.domain.usecase

import com.deliverybook.domain.model.Contact
import com.deliverybook.domain.repository.ContactsRepository
import kotlinx.coroutines.flow.Flow

class ObserveRecentContactsUseCase(
    private val repository: ContactsRepository
) {
    operator fun invoke(limit: Int = 10): Flow<List<Contact>> =
        repository.observeRecentContacts(limit)
}

