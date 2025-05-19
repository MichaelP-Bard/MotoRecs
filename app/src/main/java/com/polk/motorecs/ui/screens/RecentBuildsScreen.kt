package com.polk.motorecs.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.polk.motorecs.data.MotoBuild
import com.polk.motorecs.viewmodel.MotoRecsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecentBuildsScreen(
    viewModel: MotoRecsViewModel,
    onBackToHomeClicked: () -> Unit,
    onGoBackClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    var builds by remember { mutableStateOf<List<MotoBuild>>(emptyList()) }

    // Load builds from database once screen launches
    LaunchedEffect(Unit) {
        viewModel.loadRecentBuilds { builds = it }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Recent Builds") })
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()), // Enables full scroll
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (builds.isEmpty()) {
                Text("No saved builds yet.", color = Color(0xFFFFA500))
            } else {
                builds.forEach { build ->
                    BuildCard(
                        build = build,
                        onDelete = {
                            viewModel.deleteBuild(build) { updated ->
                                builds = updated
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Go Back to previous screen
            OutlinedButton(
                onClick = onGoBackClicked,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Go Back")
            }

            // Return to Home
            OutlinedButton(
                onClick = onBackToHomeClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text("Back to Home")
            }
        }
    }
}

@Composable
fun BuildCard(build: MotoBuild, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Main build details
            Text(
                "${build.manufacturer} (${build.year}) - ${build.engineSize}",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFFFFA500)
            )
            Text("Use: ${build.useType}", color = Color(0xFFFFA500))
            Text(
                "Add-ons: ${if (build.addOns.isNotBlank()) build.addOns else "None"}",
                color = Color(0xFFFFA500)
            )
            Text(
                "Delivery: ${build.deliveryDate} (${if (build.expressDelivery) "Express" else "Standard"})",
                color = Color(0xFFFFA500)
            )

            // Optional details
            if (build.socialHandle.isNotBlank()) {
                Text("@Handle: ${build.socialHandle}", color = Color(0xFFFFA500))
            }
            if (build.colorScheme.isNotBlank()) {
                Text("Color Scheme: ${build.colorScheme}", color = Color(0xFFFFA500))
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Delete button
            OutlinedButton(
                onClick = onDelete,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Delete")
            }
        }
    }
}