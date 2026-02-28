package com.deliverybook.feature.contacts.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deliverybook.domain.model.Contact
import com.deliverybook.domain.usecase.AddNeighborUseCase
import com.deliverybook.domain.usecase.DeleteContactUseCase
import com.deliverybook.domain.usecase.DeleteNeighborUseCase
import com.deliverybook.domain.usecase.GetContactByDniUseCase
import com.deliverybook.domain.usecase.UpsertContactUseCase
import com.deliverybook.domain.usecase.UpdateNeighborUseCase
import com.deliverybook.core.AppConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ContactDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getContactByDni: GetContactByDniUseCase,
    private val upsertContact: UpsertContactUseCase,
    private val deleteContact: DeleteContactUseCase,
    private val addNeighbor: AddNeighborUseCase,
    private val updateNeighbor: UpdateNeighborUseCase,
    private val deleteNeighbor: DeleteNeighborUseCase
) : ViewModel() {

    private val dniArg: String? = savedStateHandle.get<String>(AppConstants.Navigation.ARG_DNI)

    private val _uiState = MutableStateFlow(
        ContactDetailUiState(
            dni = dniArg.orEmpty()
        )
    )
    val uiState: StateFlow<ContactDetailUiState> = _uiState.asStateFlow()

    init {
        if (!dniArg.isNullOrBlank()) {
            viewModelScope.launch {
                val contact = getContactByDni(dniArg)
                if (contact != null) {
                    _uiState.update {
                        it.copy(
                            dni = contact.dni,
                            name = contact.name,
                            address = contact.address,
                            neighbors = contact.neighbors,
                            isExistingContact = true
                        )
                    }
                }
            }
        }
    }

    fun onDniChange(newDni: String) {
        val numericOnly = newDni.filter { it.isDigit() }
        _uiState.update { it.copy(dni = numericOnly) }
    }

    fun onNameChange(newName: String) {
        _uiState.update { it.copy(name = newName) }
    }

    fun onAddressChange(newAddress: String) {
        _uiState.update { it.copy(address = newAddress) }
    }

    fun onNewNeighborTextChange(text: String) {
        _uiState.update { it.copy(newNeighborText = text) }
    }

    fun onAddNeighbor() {
        val current = _uiState.value
        val neighbor = current.newNeighborText.trim()
        if (neighbor.isEmpty() || current.dni.isBlank()) return

        viewModelScope.launch {
            addNeighbor(current.dni, neighbor)
            _uiState.update {
                it.copy(
                    neighbors = it.neighbors + neighbor,
                    newNeighborText = ""
                )
            }
        }
    }

    fun onEditNeighbor(index: Int, newValue: String) {
        val current = _uiState.value
        if (current.dni.isBlank()) return
        viewModelScope.launch {
            updateNeighbor(current.dni, index, newValue)
            val updated = current.neighbors.toMutableList()
            if (index in updated.indices) {
                updated[index] = newValue
            }
            _uiState.update { it.copy(neighbors = updated.toList()) }
        }
    }

    fun onDeleteNeighbor(index: Int) {
        val current = _uiState.value
        if (current.dni.isBlank()) return
        viewModelScope.launch {
            deleteNeighbor(current.dni, index)
            val updated = current.neighbors.toMutableList()
            if (index in updated.indices) {
                updated.removeAt(index)
            }
            _uiState.update { it.copy(neighbors = updated.toList()) }
        }
    }

    fun onSave(onFinished: () -> Unit) {
        val current = _uiState.value
        if (current.dni.isBlank() || current.name.isBlank() || current.address.isBlank()) {
            return
        }

        viewModelScope.launch {
            val contact = Contact(
                dni = current.dni,
                name = current.name.toTitleCase(),
                address = current.address.toTitleCase(),
                neighbors = current.neighbors,
                lastSearchedAtMillis = null
            )
            upsertContact(contact)
            onFinished()
        }
    }

    private fun String.toTitleCase(): String =
        trim().split("\\s+".toRegex()).joinToString(" ") { word ->
            word.replaceFirstChar { c -> c.uppercaseChar() }
        }

    fun onDelete(onFinished: () -> Unit) {
        val current = _uiState.value
        if (!current.isExistingContact || current.dni.isBlank()) {
            return
        }

        viewModelScope.launch {
            deleteContact(current.dni)
            onFinished()
        }
    }
}

data class ContactDetailUiState(
    val dni: String = "",
    val name: String = "",
    val address: String = "",
    val neighbors: List<String> = emptyList(),
    val newNeighborText: String = "",
    val isExistingContact: Boolean = false
)

