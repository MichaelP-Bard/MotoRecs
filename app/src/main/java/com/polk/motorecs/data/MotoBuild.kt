/**
 * Description: MotoRecs – Custom "Dream" Motorcycle Builder App
 * This app lets users build and customize their dream motorcycle
 * with options for style, and performance.
 *
 * @author Michael Polk
 * @since May 8, 2025
 */

package com.polk.motorecs.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// Represents a saved motorcycle build in the database
@Entity(tableName = "moto_build")
data class MotoBuild(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Auto-generated unique ID
    val manufacturer: String,
    val year: String,
    val engineSize: String,
    val useType: String,
    val addOns: String,
    val deliveryDate: String,
    val expressDelivery: Boolean,
    val socialHandle: String = "",
    val colorScheme: String = ""
)

