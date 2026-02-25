package com.deliverybook.feature.contacts.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.deliverybook.core.AppConstants
import com.deliverybook.domain.model.Contact
import com.deliverybook.domain.usecase.MarkContactAsSearchedUseCase
import com.deliverybook.domain.usecase.ObserveRecentContactsUseCase
import com.deliverybook.domain.usecase.SearchContactsPagedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ContactsListViewModel @Inject constructor(
    observeRecentContacts: ObserveRecentContactsUseCase,
    private val searchContactsPaged: SearchContactsPagedUseCase,
    private val markContactAsSearched: MarkContactAsSearchedUseCase
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")

    private val recentContacts: StateFlow<List<Contact>> =
        observeRecentContacts()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val uiState: StateFlow<ContactsListUiState> =
        combine(searchQuery, recentContacts) { query, recent ->
            ContactsListUiState(
                query = query,
                recentContacts = recent,
                showSearchResults = query.length >= AppConstants.Search.MIN_LENGTH
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ContactsListUiState(
                query = "",
                recentContacts = emptyList(),
                showSearchResults = false
            )
        )

    val pagedContacts: Flow<PagingData<Contact>> =
        searchQuery
            .flatMapLatest { query ->
                if (query.length < AppConstants.Search.MIN_LENGTH) {
                    flowOf(PagingData.empty())
                } else {
                    searchContactsPaged(query)
                }
            }
            .cachedIn(viewModelScope)

    fun onQueryChange(newQuery: String) {
        searchQuery.value = newQuery
    }

    fun onContactClicked(dni: String) {
        viewModelScope.launch {
            markContactAsSearched(dni)
        }
    }
}

data class ContactsListUiState(
    val query: String,
    val recentContacts: List<Contact>,
    val showSearchResults: Boolean
)

