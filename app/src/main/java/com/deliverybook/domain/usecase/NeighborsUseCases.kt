package com.deliverybook.domain.usecase

import com.deliverybook.domain.repository.ContactsRepository

class AddNeighborUseCase(
    private val repository: ContactsRepository
) {
    suspend operator fun invoke(dni: String, neighbor: String) {
        repository.addNeighbor(dni, neighbor)
    }
}

class UpdateNeighborUseCase(
    private val repository: ContactsRepository
) {
    suspend operator fun invoke(dni: String, index: Int, newValue: String) {
        repository.updateNeighbor(dni, index, newValue)
    }
}

class DeleteNeighborUseCase(
    private val repository: ContactsRepository
) {
    suspend operator fun invoke(dni: String, index: Int) {
        repository.deleteNeighbor(dni, index)
    }
}

