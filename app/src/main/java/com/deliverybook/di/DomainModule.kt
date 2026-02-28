package com.deliverybook.di

import com.deliverybook.domain.repository.ContactsRepository
import com.deliverybook.domain.usecase.AddNeighborUseCase
import com.deliverybook.domain.usecase.ClearAllRecentsUseCase
import com.deliverybook.domain.usecase.DeleteContactUseCase
import com.deliverybook.domain.usecase.DeleteNeighborUseCase
import com.deliverybook.domain.usecase.GetContactByDniUseCase
import com.deliverybook.domain.usecase.MarkContactAsSearchedUseCase
import com.deliverybook.domain.usecase.ObserveContactsCountUseCase
import com.deliverybook.domain.usecase.ObserveRecentContactsUseCase
import com.deliverybook.domain.usecase.RemoveFromRecentUseCase
import com.deliverybook.domain.usecase.SearchContactsPagedUseCase
import com.deliverybook.domain.usecase.UpdateNeighborUseCase
import com.deliverybook.domain.usecase.UpsertContactUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    fun provideObserveRecentContactsUseCase(
        repository: ContactsRepository
    ): ObserveRecentContactsUseCase = ObserveRecentContactsUseCase(repository)

    @Provides
    fun provideObserveContactsCountUseCase(
        repository: ContactsRepository
    ): ObserveContactsCountUseCase = ObserveContactsCountUseCase(repository)

    @Provides
    fun provideSearchContactsPagedUseCase(
        repository: ContactsRepository
    ): SearchContactsPagedUseCase = SearchContactsPagedUseCase(repository)

    @Provides
    fun provideUpsertContactUseCase(
        repository: ContactsRepository
    ): UpsertContactUseCase = UpsertContactUseCase(repository)

    @Provides
    fun provideDeleteContactUseCase(
        repository: ContactsRepository
    ): DeleteContactUseCase = DeleteContactUseCase(repository)

    @Provides
    fun provideGetContactByDniUseCase(
        repository: ContactsRepository
    ): GetContactByDniUseCase = GetContactByDniUseCase(repository)

    @Provides
    fun provideMarkContactAsSearchedUseCase(
        repository: ContactsRepository
    ): MarkContactAsSearchedUseCase = MarkContactAsSearchedUseCase(repository)

    @Provides
    fun provideRemoveFromRecentUseCase(
        repository: ContactsRepository
    ): RemoveFromRecentUseCase = RemoveFromRecentUseCase(repository)

    @Provides
    fun provideClearAllRecentsUseCase(
        repository: ContactsRepository
    ): ClearAllRecentsUseCase = ClearAllRecentsUseCase(repository)

    @Provides
    fun provideAddNeighborUseCase(
        repository: ContactsRepository
    ): AddNeighborUseCase = AddNeighborUseCase(repository)

    @Provides
    fun provideUpdateNeighborUseCase(
        repository: ContactsRepository
    ): UpdateNeighborUseCase = UpdateNeighborUseCase(repository)

    @Provides
    fun provideDeleteNeighborUseCase(
        repository: ContactsRepository
    ): DeleteNeighborUseCase = DeleteNeighborUseCase(repository)
}

