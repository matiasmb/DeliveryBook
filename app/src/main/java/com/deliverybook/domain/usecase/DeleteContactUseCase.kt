package com.deliverybook.domain.usecase

import com.deliverybook.domain.repository.ContactsRepository

class DeleteContactUseCase(
    private val repository: ContactsRepository
) {
    suspend operator fun invoke(dni: String) {
        repository.deleteContact(dni)
    }
}

