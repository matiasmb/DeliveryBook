package com.deliverybook.domain.usecase

import com.deliverybook.domain.repository.ContactsRepository

class RemoveFromRecentUseCase(
    private val repository: ContactsRepository
) {
    suspend operator fun invoke(dni: String) {
        repository.removeFromRecent(dni)
    }
}
