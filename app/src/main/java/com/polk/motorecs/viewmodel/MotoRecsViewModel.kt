/**
 * Description: MotoRecs – Custom "Dream" Motorcycle Builder App
 * This app lets users build and customize their dream motorcycle
 * with options for style, and performance.
 *
 * @author Michael Polk
 * @since May 8, 2025
 */

package com.polk.motorecs.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polk.motorecs.data.BikeDescription
import com.polk.motorecs.data.MotoBuild
import com.polk.motorecs.data.MotoRecsRepository
import kotlinx.coroutines.launch

// Holds the current state of the motorcycle build form
data class MotoRecsUiState(
    val manufacturer: String = "",
    val year: String = "",
    val engineSize: String = "",
    val useType: String = "Track",
    val addOns: Map<String, Boolean> = emptyMap(),
    val deliveryDate: String = "",
    val expressDelivery: Boolean = false,
    val socialHandle: String = "",
    val colorScheme: String = ""
)

class MotoRecsViewModel(private val repository: MotoRecsRepository) : ViewModel() {

    // State for form selections with default add-ons
    var uiState by mutableStateOf(
        MotoRecsUiState(addOns = defaultAddOns.associateWith { false })
    )
        private set

    // List of manufacturers to display in dropdown
    var manufacturerList by mutableStateOf(listOf<String>())
        private set

    // Parsed bike descriptions from XML
    var bikeDescriptions by mutableStateOf(emptyList<BikeDescription>())
        private set

    // Flag to show/hide save confirmation toast
    var saveConfirmationShown by mutableStateOf(false)
        private set

    companion object {
        // Available color options
        val colorOptions = listOf(
            "Black & Red", "Black & Orange", "Blue & White", "Red & White", "Yellow & Black",
            "White & Black", "Blue & Yellow", "Green & Black", "Matte Grey & Red",
            "Carbon Black & Gold", "Silver & Blue", "White & Gold"
        )

        // Available add-on options
        val defaultAddOns = listOf(
            "Exhaust", "Graphics Kit", "Performance ECU", "Suspension",
            "Custom Headlights", "LEDs / HID Halos", "Custom Clip-ons",
            "ABS", "Heated Grips", "Full TFT Dash", "LED Dash",
            "Social Handle Decal"
        )
    }

    // Updates selected manufacturer
    fun updateManufacturer(value: String) {
        uiState = uiState.copy(manufacturer = value)
    }

    // Updates selected year
    fun updateYear(value: String) {
        uiState = uiState.copy(year = value)
    }

    // Updates selected engine size
    fun updateEngineSize(value: String) {
        uiState = uiState.copy(engineSize = value)
    }

    // Updates selected use type (e.g. Track, Street)
    fun updateUseType(value: String) {
        uiState = uiState.copy(useType = value)
    }

    // Toggles an add-on on or off
    fun toggleAddon(label: String) {
        val updatedAddOns = uiState.addOns.toMutableMap()
        updatedAddOns[label] = !(updatedAddOns[label] ?: false)
        uiState = uiState.copy(addOns = updatedAddOns)
    }

    // Updates selected delivery date
    fun updateDeliveryDate(value: String) {
        uiState = uiState.copy(deliveryDate = value)
    }

    // Toggles express delivery on or off
    fun toggleExpress() {
        uiState = uiState.copy(expressDelivery = !uiState.expressDelivery)
    }

    // Updates the user's social media handle
    fun updateSocialHandle(value: String) {
        uiState = uiState.copy(socialHandle = value)
    }

    // Updates selected color scheme
    fun updateColorScheme(value: String) {
        uiState = uiState.copy(colorScheme = value)
    }

    // Resets all selections to default values
    fun resetSelections() {
        uiState = MotoRecsUiState(addOns = defaultAddOns.associateWith { false })
    }

    // Resets the save confirmation toast
    fun acknowledgeSaveToast() {
        saveConfirmationShown = false
    }

    // Saves the current build to the database
    fun saveBuildToRoom() {
        val build = MotoBuild(
            manufacturer = uiState.manufacturer,
            year = uiState.year,
            engineSize = uiState.engineSize,
            useType = uiState.useType,
            addOns = uiState.addOns.filter { it.value }.keys.joinToString(", "),
            deliveryDate = uiState.deliveryDate,
            expressDelivery = uiState.expressDelivery,
            socialHandle = uiState.socialHandle,
            colorScheme = uiState.colorScheme
        )

        viewModelScope.launch {
            repository.saveBuildToRoom(build)
            saveConfirmationShown = true
        }
    }

    // Loads all saved builds and passes them to the UI
    fun loadRecentBuilds(onResult: (List<MotoBuild>) -> Unit) {
        viewModelScope.launch {
            onResult(repository.getAllBuilds())
        }
    }

    // Deletes a selected build and returns the updated list
    fun deleteBuild(build: MotoBuild, onResult: (List<MotoBuild>) -> Unit) {
        viewModelScope.launch {
            repository.deleteBuild(build)
            onResult(repository.getAllBuilds())
        }
    }

    // Loads manufacturers and descriptions from XML
    fun loadManufacturersFromXml() {
        val parsedList = repository.parseBikeDescriptions()
        bikeDescriptions = parsedList
        manufacturerList = parsedList.map { it.key }
    }

    // Retrieves description for selected manufacturer
    fun getManufacturerDescription(key: String): String {
        return bikeDescriptions.firstOrNull { it.key.equals(key, ignoreCase = true) }?.description
            ?: "No description found."
    }

    // Retrieves image name for selected manufacturer
    fun getImageNameForManufacturer(key: String): String {
        return bikeDescriptions.firstOrNull { it.key.equals(key, ignoreCase = true) }?.imageName
            ?: "default_image"
    }
}

