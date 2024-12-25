package com.hp77.linkstash.presentation.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAbout: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Navigate back"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // GitHub Section
            Card {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Code, contentDescription = "GitHub")
                        Text(
                            text = "GitHub Sync",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Text(
                        text = "Backup your links to a private GitHub repository",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (state.isGitHubAuthenticated) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = state.githubRepoName,
                                    onValueChange = { viewModel.onEvent(SettingsScreenEvent.UpdateGitHubRepoName(it)) },
                                    label = { Text("Repository Name") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )
                                OutlinedTextField(
                                    value = state.githubRepoOwner,
                                    onValueChange = { viewModel.onEvent(SettingsScreenEvent.UpdateGitHubRepoOwner(it)) },
                                    label = { Text("Repository Owner") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = { viewModel.onEvent(SettingsScreenEvent.SyncToGitHub) },
                                        enabled = !state.isGitHubSyncing,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(Icons.Default.Sync, contentDescription = null)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(if (state.isGitHubSyncing) "Syncing..." else "Sync Now")
                                    }
                                    OutlinedButton(
                                        onClick = { viewModel.onEvent(SettingsScreenEvent.DisconnectGitHub) },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Disconnect")
                                    }
                                }
                            }
                        } else {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = state.githubToken,
                                    onValueChange = { viewModel.onEvent(SettingsScreenEvent.UpdateGitHubToken(it)) },
                                    label = { Text("GitHub Personal Access Token") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )
                                Button(
                                    onClick = { viewModel.onEvent(SettingsScreenEvent.ConnectGitHub) },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Connect GitHub")
                                }
                            }
                        }
                    }
                }
            }

            // About Section
            Card {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Info, contentDescription = "About")
                        Text(
                            text = "About",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Text(
                        text = "App information and features",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Button(
                        onClick = onNavigateToAbout,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("View Details")
                    }
                }
            }

            // HackerNews Section
            Card {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Link, contentDescription = "HackerNews")
                        Text(
                            text = "HackerNews Integration",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Text(
                        text = "Share your links directly to HackerNews",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    if (state.isHackerNewsAuthenticated) {
                        OutlinedButton(
                            onClick = { viewModel.onEvent(SettingsScreenEvent.LogoutFromHackerNews) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Disconnect")
                        }
                    } else {
                        Button(
                            onClick = { viewModel.onEvent(SettingsScreenEvent.ShowHackerNewsLogin) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Connect HackerNews")
                        }
                    }
                }
            }
        }
    }

    // HackerNews Login Dialog
    if (state.showHackerNewsLoginDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(SettingsScreenEvent.HideHackerNewsLogin) },
            title = { Text("Login to HackerNews") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = state.hackerNewsUsername,
                        onValueChange = { viewModel.onEvent(SettingsScreenEvent.UpdateHackerNewsUsername(it)) },
                        label = { Text("Username") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = state.hackerNewsPassword,
                        onValueChange = { viewModel.onEvent(SettingsScreenEvent.UpdateHackerNewsPassword(it)) },
                        label = { Text("Password") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.onEvent(SettingsScreenEvent.LoginToHackerNews) },
                    enabled = !state.isLoading
                ) {
                    Text(if (state.isLoading) "Logging in..." else "Login")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onEvent(SettingsScreenEvent.HideHackerNewsLogin) }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Show error in snackbar if present
    state.error?.let { error ->
        LaunchedEffect(error) {
            snackbarHostState.showSnackbar(
                message = error,
                duration = SnackbarDuration.Short,
                withDismissAction = true
            )
            viewModel.onEvent(SettingsScreenEvent.DismissError)
        }
    }
}