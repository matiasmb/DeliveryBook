package com.deliverybook.domain.usecase

import com.deliverybook.domain.repository.ContactsRepository
import kotlinx.coroutines.flow.Flow

class ObserveContactsCountUseCase(
    private val repository: ContactsRepository
) {
    operator fun invoke(): Flow<Int> =
        repository.observeContactsCount()
}
