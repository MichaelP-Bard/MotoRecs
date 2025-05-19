/**
 * Description: MotoRecs – Custom "Dream" Motorcycle Builder App
 * This app lets users build and customize their dream motorcycle
 * with options for style, and performance.
 *
 * @author Michael Polk
 * @since May 8, 2025
 */

package com.polk.motorecs.ui.screens

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.polk.motorecs.R
import com.polk.motorecs.viewmodel.MotoRecsViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummaryScreen(
    viewModel: MotoRecsViewModel,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState = viewModel.uiState
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Get manufacturer description and image name
    val description = viewModel.getManufacturerDescription(uiState.manufacturer)
    val imageName = viewModel.getImageNameForManufacturer(uiState.manufacturer)

    // Load image resource ID from name
    val imageResId = context.resources.getIdentifier(imageName, "drawable", context.packageName)

    // Estimate total price
    val basePrice = when (uiState.engineSize) {
        "125cc" -> 3500
        "250cc" -> 5000
        "300cc" -> 5400
        "400cc" -> 6500
        "600cc" -> 8000
        "650cc" -> 8500
        "700cc" -> 8900
        "750cc" -> 9500
        "900cc" -> 10000
        "1000cc" -> 11000
        "1100cc" -> 11500
        "1200cc" -> 12500
        "1400cc" -> 13500
        else -> 0
    }

    val addOnCost = uiState.addOns.filterValues { it }.size * 500
    val expressFee = if (uiState.expressDelivery) 750 else 0
    val totalPrice = basePrice + addOnCost + expressFee
    val formattedPrice = NumberFormat.getCurrencyInstance(Locale.US).format(totalPrice)
    val deliveryEstimate = if (uiState.expressDelivery) "30 days" else "90 days"

    // Build summary text for sharing
    val summaryText = buildString {
        appendLine("MotoRecs Build Summary")
        appendLine("Manufacturer: ${uiState.manufacturer}")
        appendLine("Description: $description")
        appendLine("Year: ${uiState.year}")
        appendLine("Engine Size: ${uiState.engineSize}")
        appendLine("Use Type: ${uiState.useType}")
        appendLine("Add-ons: ${if (uiState.addOns.any { it.value }) uiState.addOns.filter { it.value }.keys.joinToString() else "None"}")
        appendLine("Delivery Date: ${uiState.deliveryDate} ($deliveryEstimate)")
        if (uiState.socialHandle.isNotBlank()) appendLine("Social Handle: ${uiState.socialHandle}")
        appendLine("Color Scheme: ${uiState.colorScheme}")
        appendLine("Estimated Price: $formattedPrice")
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Build Summary") })
        },
        modifier = modifier
    ) { innerPadding ->

        Box(modifier = Modifier.fillMaxSize()) {
            // Background image
            Image(
                painter = painterResource(id = R.drawable.summary_screen),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(20.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.Start
            ) {
                // Manufacturer-specific bike image (if found)
                if (imageResId != 0) {
                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = "${uiState.manufacturer} Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                SummaryRow("Manufacturer", uiState.manufacturer)
                Text("Description: $description", color = Color(0xFFFFA500))

                SummaryRow("Year", uiState.year)
                SummaryRow("Engine Size", uiState.engineSize)
                SummaryRow("Use Type", uiState.useType)

                SummaryRow(
                    "Add-ons",
                    if (uiState.addOns.any { it.value }) uiState.addOns.filter { it.value }.keys.joinToString()
                    else "None"
                )

                SummaryRow("Delivery Date", uiState.deliveryDate)
                SummaryRow("Delivery Speed", deliveryEstimate)

                if (uiState.expressDelivery) {
                    Text("Express Delivery Fee: +$750", color = Color(0xFFFFA500))
                }

                if (uiState.socialHandle.isNotBlank()) {
                    SummaryRow("Social Handle", uiState.socialHandle)
                }

                SummaryRow("Color Scheme", uiState.colorScheme.ifBlank { "Default" })

                HorizontalDivider(thickness = 1.dp, color = Color.Gray)

                Text(
                    text = "Estimated Price: $formattedPrice",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFFFFA500)
                )

                Spacer(Modifier.height(24.dp))

                Button(onClick = onBackClicked, modifier = Modifier.fillMaxWidth()) {
                    Text("Back to Builder")
                }

                Button(
                    onClick = {
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, summaryText)
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, "Share your build")
                        context.startActivity(shareIntent)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Share Build")
                }
            }
        }
    }
}

@Composable
fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = Color(0xFFFFA500))
        Text(value, style = MaterialTheme.typography.bodyLarge, color = Color(0xFFFFA500))
    }
}



