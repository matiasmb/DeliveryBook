package com.deliverybook.feature.contacts.list

import androidx.paging.PagingData
import com.google.common.truth.Truth.assertThat
import com.deliverybook.domain.model.Contact
import com.deliverybook.domain.usecase.MarkContactAsSearchedUseCase
import com.deliverybook.domain.usecase.ObserveRecentContactsUseCase
import com.deliverybook.domain.usecase.SearchContactsPagedUseCase
import com.deliverybook.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ContactsListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun queryLessThan3_showsRecent_andDoesNotTriggerSearchFlow() = runTest {
        val observeRecent = mockk<ObserveRecentContactsUseCase>()
        val searchPaged = mockk<SearchContactsPagedUseCase>()
        val markSearched = mockk<MarkContactAsSearchedUseCase>()

        val recent = listOf(
            Contact("1", "Juan", "Calle 1", emptyList(), null)
        )

        every { observeRecent.invoke(any()) } returns flowOf(recent)
        every { searchPaged.invoke(any()) } returns flowOf(PagingData.empty())
        coEvery { markSearched.invoke(any()) } returns Unit

        val vm = ContactsListViewModel(observeRecent, searchPaged, markSearched)

        vm.onQueryChange("Jo")
        advanceUntilIdle()

        val ui = vm.uiState.first { it.query == "Jo" && it.recentContacts.isNotEmpty() }
        assertThat(ui.showSearchResults).isFalse()
        assertThat(ui.recentContacts).containsExactlyElementsIn(recent)

        // Act: collect once to trigger pagedContacts branch
        vm.pagedContacts.first()
        verify(exactly = 0) { searchPaged.invoke(any()) }
    }

    @Test
    fun queryAtLeast3_setsFlag_andTriggersSearchFlowWhenCollected() = runTest {
        val observeRecent = mockk<ObserveRecentContactsUseCase>()
        val searchPaged = mockk<SearchContactsPagedUseCase>()
        val markSearched = mockk<MarkContactAsSearchedUseCase>()

        every { observeRecent.invoke(any()) } returns flowOf(emptyList())
        every { searchPaged.invoke("Jor") } returns flowOf(PagingData.empty())
        coEvery { markSearched.invoke(any()) } returns Unit

        val vm = ContactsListViewModel(observeRecent, searchPaged, markSearched)

        vm.onQueryChange("Jor")
        advanceUntilIdle()

        val ui = vm.uiState.first { it.query == "Jor" }
        assertThat(ui.showSearchResults).isTrue()

        vm.pagedContacts.first()
        verify(exactly = 1) { searchPaged.invoke("Jor") }
    }

    @Test
    fun onContactClicked_callsUseCase() = runTest {
        val observeRecent = mockk<ObserveRecentContactsUseCase>()
        val searchPaged = mockk<SearchContactsPagedUseCase>()
        val markSearched = mockk<MarkContactAsSearchedUseCase>()

        every { observeRecent.invoke(any()) } returns flowOf(emptyList())
        every { searchPaged.invoke(any()) } returns flowOf(PagingData.empty())
        coEvery { markSearched.invoke("123") } returns Unit

        val vm = ContactsListViewModel(observeRecent, searchPaged, markSearched)

        vm.onContactClicked("123")
        advanceUntilIdle()

        coVerify(exactly = 1) { markSearched.invoke("123") }
    }
}
