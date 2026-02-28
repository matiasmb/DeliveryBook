package com.deliverybook.feature.contacts.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.deliverybook.core.AppConstants
import com.deliverybook.domain.model.Contact
import com.deliverybook.domain.usecase.ClearAllRecentsUseCase
import com.deliverybook.domain.usecase.MarkContactAsSearchedUseCase
import com.deliverybook.domain.usecase.ObserveContactsCountUseCase
import com.deliverybook.domain.usecase.ObserveRecentContactsUseCase
import com.deliverybook.domain.usecase.RemoveFromRecentUseCase
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
    observeContactsCount: ObserveContactsCountUseCase,
    private val searchContactsPaged: SearchContactsPagedUseCase,
    private val markContactAsSearched: MarkContactAsSearchedUseCase,
    private val removeFromRecent: RemoveFromRecentUseCase,
    private val clearAllRecents: ClearAllRecentsUseCase
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")
    private val isRecentEditMode = MutableStateFlow(false)

    private val recentContacts: StateFlow<List<Contact>> =
        observeRecentContacts()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    private val contactsCount: StateFlow<Int> =
        observeContactsCount()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = 0
            )

    val uiState: StateFlow<ContactsListUiState> =
        combine(searchQuery, recentContacts, isRecentEditMode, contactsCount) { query, recent, editMode, count ->
            ContactsListUiState(
                query = query,
                recentContacts = recent,
                showSearchResults = query.length >= AppConstants.Search.MIN_LENGTH,
                isRecentEditMode = editMode,
                contactsCount = count
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ContactsListUiState(
                query = "",
                recentContacts = emptyList(),
                showSearchResults = false,
                isRecentEditMode = false,
                contactsCount = 0
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

    fun onRecentLongPress() {
        isRecentEditMode.value = true
    }

    fun onExitRecentEditMode() {
        isRecentEditMode.value = false
    }

    fun onRemoveFromRecent(dni: String) {
        viewModelScope.launch {
            removeFromRecent(dni)
        }
    }

    fun onClearAllRecents() {
        viewModelScope.launch {
            clearAllRecents()
            isRecentEditMode.value = false
        }
    }
}

data class ContactsListUiState(
    val query: String,
    val recentContacts: List<Contact>,
    val showSearchResults: Boolean,
    val isRecentEditMode: Boolean = false,
    val contactsCount: Int = 0
)

