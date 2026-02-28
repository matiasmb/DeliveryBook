package com.deliverybook.domain.usecase

import com.deliverybook.domain.repository.ContactsRepository

class ClearAllRecentsUseCase(
    private val repository: ContactsRepository
) {
    suspend operator fun invoke() {
        repository.clearAllRecents()
    }
}
