/**
 * Description: MotoRecs – Custom "Dream" Motorcycle Builder App
 * This app lets users build and customize their dream motorcycle
 * with options for style, and performance.
 *
 * @author Michael Polk
 * @since May 8, 2025
 */

package com.polk.motorecs.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Room database for storing MotoBuild entries
@Database(entities = [MotoBuild::class], version = 1, exportSchema = false)
abstract class MotoRecsDatabase : RoomDatabase() {

    // Data access object for MotoBuild
    abstract fun buildDao(): MotoBuildDao

    companion object {
        @Volatile
        private var INSTANCE: MotoRecsDatabase? = null

        // Returns the singleton instance of the database
        fun getDatabase(context: Context): MotoRecsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MotoRecsDatabase::class.java,
                    "moto_recs_database"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

