package com.deliverybook.feature.contacts.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.deliverybook.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactDetailScreen(
    viewModel: ContactDetailViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (uiState.isExistingContact) {
                            stringResource(R.string.contact_detail_edit_title)
                        } else {
                            stringResource(R.string.contact_detail_new_title)
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.action_back)
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.dni,
                onValueChange = viewModel::onDniChange,
                label = { Text(stringResource(R.string.contact_field_dni)) },
                singleLine = true,
                enabled = !uiState.isExistingContact
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.name,
                onValueChange = viewModel::onNameChange,
                label = { Text(stringResource(R.string.contact_field_name)) },
                singleLine = true
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.address,
                onValueChange = viewModel::onAddressChange,
                label = { Text(stringResource(R.string.contact_field_address)) },
                singleLine = true
            )

            Text(
                text = stringResource(R.string.neighbors_title),
                style = MaterialTheme.typography.titleMedium
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.newNeighborText,
                onValueChange = viewModel::onNewNeighborTextChange,
                label = { Text(stringResource(R.string.neighbors_add_label)) },
                singleLine = true
            )

            Button(
                onClick = viewModel::onAddNeighbor,
                enabled = uiState.newNeighborText.isNotBlank() && uiState.dni.isNotBlank()
            ) {
                Text(stringResource(R.string.action_add))
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(uiState.neighbors) { index, neighbor ->
                    NeighborRow(
                        neighbor = neighbor,
                        onEdit = { newValue -> viewModel.onEditNeighbor(index, newValue) },
                        onDelete = { viewModel.onDeleteNeighbor(index) }
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.onSave(onBack) }
                ) {
                    Text(text = stringResource(R.string.action_save))
                }

                if (uiState.isExistingContact) {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.onDelete(onBack) }
                    ) {
                        Text(text = stringResource(R.string.action_delete))
                    }
                }
            }
        }
    }
}

@Preview(name = "NeighborRow", showBackground = true)
@Composable
private fun NeighborRowPreview() {
    com.deliverybook.ui.theme.DeliveryBookTheme {
        NeighborRow(
            neighbor = "Vecino de ejemplo",
            onEdit = {},
            onDelete = {}
        )
    }
}

@Preview(name = "ContactDetailScreen (new)", showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContactDetailScreenNewPreview() {
    com.deliverybook.ui.theme.DeliveryBookTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.contact_detail_new_title)) },
                    navigationIcon = {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = stringResource(R.string.action_back)
                            )
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = "",
                    onValueChange = {},
                    label = { Text(stringResource(R.string.contact_field_dni)) },
                    singleLine = true
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = "",
                    onValueChange = {},
                    label = { Text(stringResource(R.string.contact_field_name)) },
                    singleLine = true
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = "",
                    onValueChange = {},
                    label = { Text(stringResource(R.string.contact_field_address)) },
                    singleLine = true
                )
                Text(
                    text = stringResource(R.string.neighbors_title),
                    style = MaterialTheme.typography.titleMedium
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = "",
                    onValueChange = {},
                    label = { Text(stringResource(R.string.neighbors_add_label)) },
                    singleLine = true
                )
                Button(onClick = {}) {
                    Text(stringResource(R.string.action_add))
                }
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(listOf("Vecino 1", "Vecino 2")) { index, neighbor ->
                        NeighborRow(
                            neighbor = neighbor,
                            onEdit = {},
                            onDelete = {}
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(modifier = Modifier.weight(1f), onClick = {}) {
                        Text(text = stringResource(R.string.action_save))
                    }
                }
            }
        }
    }
}

@Composable
private fun NeighborRow(
    neighbor: String,
    onEdit: (String) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isEditing = remember { mutableStateOf(false) }
    val editText = remember { mutableStateOf(neighbor) }

    if (isEditing.value) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = editText.value,
                onValueChange = { editText.value = it },
                singleLine = true
            )
            IconButton(
                onClick = {
                    onEdit(editText.value)
                    isEditing.value = false
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.action_save_change)
                )
            }
        }
    } else {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable { isEditing.value = true }
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = neighbor, style = MaterialTheme.typography.bodyMedium)
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.action_delete_neighbor)
                )
            }
        }
    }
}

