package com.deliverybook.domain.usecase

import com.deliverybook.domain.repository.ContactsRepository

class MarkContactAsSearchedUseCase(
    private val repository: ContactsRepository
) {
    suspend operator fun invoke(dni: String) {
        repository.markContactAsSearched(dni)
    }
}

