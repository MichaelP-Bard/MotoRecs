/**
 * Description: MotoRecs – Custom "Dream" Motorcycle Builder App
 * This app lets users build and customize their dream motorcycle
 * with options for style, and performance.
 *
 * @author Michael Polk
 * @since May 8, 2025
 */

package com.polk.motorecs.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MotoBuildDao {

    // Insert a new build into the database
    @Insert
    suspend fun insertBuild(build: MotoBuild)

    // Get all saved builds, most recent first
    @Query("SELECT * FROM moto_build ORDER BY id DESC")
    suspend fun getAllBuilds(): List<MotoBuild>

    // Delete a specific build
    @Delete
    suspend fun deleteBuild(build: MotoBuild)
}

