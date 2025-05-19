/**
 * Description: MotoRecs – Custom "Dream" Motorcycle Builder App
 * This app lets users build and customize their dream motorcycle
 * with options for style, and performance.
 *
 * @author Michael Polk
 * @since May 8, 2025
 */

package com.polk.motorecs.ui.screens

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.polk.motorecs.R
import com.polk.motorecs.viewmodel.MotoRecsViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainBuilderScreen(
    viewModel: MotoRecsViewModel,
    onReviewClicked: () -> Unit,
    onSavedBuildsClicked: () -> Unit,
    onBackToHomeClicked: () -> Unit,
    onHomeClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState = viewModel.uiState
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Show toast if save was successful
    LaunchedEffect(viewModel.saveConfirmationShown) {
        if (viewModel.saveConfirmationShown) {
            Toast.makeText(context, "Congrats on building your dream bike!", Toast.LENGTH_SHORT).show()
            viewModel.acknowledgeSaveToast()
        }
    }

    // Load manufacturer data if not already loaded
    LaunchedEffect(Unit) {
        if (viewModel.manufacturerList.isEmpty()) {
            viewModel.loadManufacturersFromXml()
        }
    }

    // Default delivery date (90 days ahead)
    val calendar = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 90) }
    val defaultDate = SimpleDateFormat("MM/dd/yyyy", Locale.US).format(calendar.time)

    // Date picker dialog setup
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, day -> viewModel.updateDeliveryDate("${month + 1}/$day/$year") },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MotoRecs – Build Your Dream Bike") },
                navigationIcon = {
                    IconButton(onClick = onBackToHomeClicked) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")

                    }
                }
            )
        },
        modifier = modifier
    ) { innerPadding ->

        Box(modifier = Modifier.fillMaxSize()) {
            // Background image for subtle visual flair
            Image(
                painter = painterResource(id = R.drawable.indian_background),
                contentDescription = "Builder Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                alpha = 0.15f
            )

            // Main form content
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Manufacturer dropdown
                DropdownField("Manufacturer", uiState.manufacturer, viewModel.manufacturerList) { selected ->
                    viewModel.updateManufacturer(selected)
                }

                // Year dropdown
                DropdownField("Year", uiState.year, (2000..2025).map { it.toString() }) { selected ->
                    viewModel.updateYear(selected)
                }

                // Engine size dropdown
                DropdownField("Engine Size", uiState.engineSize, listOf(
                    "125cc", "250cc", "300cc", "400cc", "600cc", "650cc",
                    "700cc", "750cc", "900cc", "1000cc", "1100cc", "1200cc", "1400cc"
                )) { selected ->
                    viewModel.updateEngineSize(selected)
                }

                // Intended use selection (Track, Street, Dirt)
                Text("Intended Use:")
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    listOf("Track", "Street", "Dirt").forEach { type ->
                        FilterChip(
                            selected = uiState.useType == type,
                            onClick = { viewModel.updateUseType(type) },
                            label = { Text(type) }
                        )
                    }
                }

                // Add-on options as toggles
                Text("Add-ons:")
                uiState.addOns.forEach { (label, selected) ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .toggleable(
                                value = selected,
                                onValueChange = { viewModel.toggleAddon(label) }
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(checked = selected, onCheckedChange = null)
                        Text(text = label)
                    }
                }

                // Show handle input only if decal add-on is selected
                if (uiState.addOns["Social Handle Decal"] == true) {
                    OutlinedTextField(
                        value = TextFieldValue(uiState.socialHandle),
                        onValueChange = { newValue -> viewModel.updateSocialHandle(newValue.text) },
                        label = { Text("Your @Handle") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Color scheme dropdown
                DropdownField("Bike Color Scheme", uiState.colorScheme, MotoRecsViewModel.colorOptions) { selected ->
                    viewModel.updateColorScheme(selected)
                }

                // Delivery date picker field (readonly)
                OutlinedTextField(
                    value = uiState.deliveryDate.ifEmpty { defaultDate },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Delivery Date (90+ days)") },
                    trailingIcon = {
                        IconButton(onClick = { datePickerDialog.show() }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Pick date")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // Express delivery checkbox
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = uiState.expressDelivery,
                        onCheckedChange = { viewModel.toggleExpress() }
                    )
                    Text("Express Delivery (30 days, adds cost)")
                }

                Spacer(Modifier.height(16.dp))

                // Button actions
                Button(onClick = onReviewClicked, modifier = Modifier.fillMaxWidth()) {
                    Text("Review Build")
                }

                Button(onClick = { viewModel.saveBuildToRoom() }, modifier = Modifier.fillMaxWidth()) {
                    Text("Save Build")
                }

                OutlinedButton(onClick = { viewModel.resetSelections() }, modifier = Modifier.fillMaxWidth()) {
                    Text("Start Over")
                }

                OutlinedButton(onClick = onSavedBuildsClicked, modifier = Modifier.fillMaxWidth()) {
                    Text("Recent Builds")
                }

                OutlinedButton(onClick = onBackToHomeClicked, modifier = Modifier.fillMaxWidth()) {
                    Text("Back to Home")
                }
            }
        }
    }
}

@Composable
fun DropdownField(
    label: String,
    value: String,
    options: List<String>,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    // Custom dropdown input field
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Expand dropdown")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Shows dropdown menu below the field
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

