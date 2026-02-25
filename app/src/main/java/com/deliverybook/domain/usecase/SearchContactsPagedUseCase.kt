package com.deliverybook.domain.usecase

import androidx.paging.PagingData
import com.deliverybook.domain.model.Contact
import com.deliverybook.domain.repository.ContactsRepository
import kotlinx.coroutines.flow.Flow

class SearchContactsPagedUseCase(
    private val repository: ContactsRepository
) {
    operator fun invoke(query: String): Flow<PagingData<Contact>> =
        repository.searchContactsPaged(query)
}

