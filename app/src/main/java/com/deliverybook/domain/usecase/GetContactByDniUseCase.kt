package com.deliverybook.domain.usecase

import com.deliverybook.domain.model.Contact
import com.deliverybook.domain.repository.ContactsRepository

class GetContactByDniUseCase(
    private val repository: ContactsRepository
) {
    suspend operator fun invoke(dni: String): Contact? =
        repository.getContactByDni(dni)
}

