package com.deliverybook.feature.contacts.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.paging.compose.collectAsLazyPagingItems
import com.deliverybook.R
import com.deliverybook.domain.model.Contact
import com.deliverybook.ui.theme.DeliveryBookTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsListScreen(
    viewModel: ContactsListViewModel,
    onContactClick: (String) -> Unit,
    onCreateNewContact: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val pagedContacts = viewModel.pagedContacts.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(80.dp),
                title = {
                    Row(
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_logo_fiorino),
                            contentDescription = stringResource(R.string.app_name),
                            modifier = Modifier.size(44.dp),
                            tint = Color.Unspecified
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = stringResource(R.string.contacts_list_greeting) + " ",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = stringResource(R.string.contacts_list_user_name),
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateNewContact) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.contacts_new_contact)
                )
            }
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
                value = uiState.query,
                onValueChange = viewModel::onQueryChange,
                label = { Text(stringResource(R.string.contacts_search_label)) },
                singleLine = true
            )

            if (uiState.showSearchResults) {
                Text(
                    text = stringResource(R.string.contacts_results_title),
                    style = MaterialTheme.typography.titleMedium
                )
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(pagedContacts.itemCount) { index ->
                        val contact = pagedContacts[index]
                        if (contact != null) {
                            ContactRow(
                                contact = contact,
                                onClick = {
                                    viewModel.onContactClicked(contact.dni)
                                    onContactClick(contact.dni)
                                }
                            )
                        }
                    }
                }
            } else {
                Text(
                    text = stringResource(R.string.contacts_recent_title),
                    style = MaterialTheme.typography.titleMedium
                )
                if (uiState.recentContacts.isEmpty()) {
                    Text(
                        text = stringResource(R.string.contacts_recent_empty),
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        items(uiState.recentContacts) { contact ->
                            ContactRow(
                                contact = contact,
                                onClick = {
                                    viewModel.onContactClicked(contact.dni)
                                    onContactClick(contact.dni)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(name = "ContactRow", showBackground = true)
@Composable
private fun ContactRowPreview() {
    DeliveryBookTheme {
        ContactRow(
            contact = Contact(
                dni = "12345678",
                name = "Juan Pérez",
                address = "Calle Falsa 123",
                neighbors = emptyList(),
                lastSearchedAtMillis = null
            ),
            onClick = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "ContactsListScreen", showBackground = true)
@Composable
private fun ContactsListScreenPreview() {
    val sampleContact = Contact(
        dni = "12345678",
        name = "Juan Pérez",
        address = "Calle Falsa 123",
        neighbors = emptyList(),
        lastSearchedAtMillis = null
    )
    DeliveryBookTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_logo_fiorino),
                                contentDescription = stringResource(R.string.app_name),
                                modifier = Modifier.size(44.dp),
                                tint = Color.Unspecified
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = stringResource(R.string.contacts_list_greeting) + " " + stringResource(R.string.contacts_list_user_name),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.contacts_new_contact)
                    )
                }
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
                    label = { Text(stringResource(R.string.contacts_search_label)) },
                    singleLine = true
                )
                Text(
                    text = stringResource(R.string.contacts_recent_title),
                    style = MaterialTheme.typography.titleMedium
                )
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(listOf(sampleContact)) { contact ->
                        ContactRow(contact = contact, onClick = {})
                    }
                }
            }
        }
    }
}

@Composable
private fun ContactRow(
    contact: Contact,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        onClick = onClick,
        shape = CardDefaults.shape,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Text(
                text = contact.name,
                style = MaterialTheme.typography.titleLarge
            )
            if (contact.address.isNotBlank()) {
                Text(
                    text = contact.address,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(
                text = stringResource(R.string.contact_dni_format, contact.dni),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

