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
import android.content.res.XmlResourceParser
import com.polk.motorecs.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParser

// Holds manufacturer details parsed from XML
data class BikeDescription(
    val key: String,
    val description: String,
    val imageName: String
)

class MotoRecsRepository(private val context: Context) {

    private val db = MotoRecsDatabase.getDatabase(context)
    private val dao = db.buildDao()

    // Save a build to the Room database
    suspend fun saveBuildToRoom(build: MotoBuild) = withContext(Dispatchers.IO) {
        dao.insertBuild(build)
    }

    // Fetch all saved builds from Room
    suspend fun getAllBuilds(): List<MotoBuild> = withContext(Dispatchers.IO) {
        dao.getAllBuilds()
    }

    // Delete a specific build from Room
    suspend fun deleteBuild(build: MotoBuild) = withContext(Dispatchers.IO) {
        dao.deleteBuild(build)
    }

    // Parse bike manufacturer data from XML file in res/xml
    fun parseBikeDescriptions(): List<BikeDescription> {
        val result = mutableListOf<BikeDescription>()
        val parser: XmlResourceParser = context.resources.getXml(R.xml.bike_descriptions)

        var key: String? = null
        var descriptions = mutableListOf<String>()
        var image: String? = null

        try {
            var eventType = parser.eventType
            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> when (parser.name) {
                        "key" -> key = parser.nextText()
                        "description" -> descriptions.add(parser.nextText())
                        "image" -> image = parser.nextText()
                    }
                    XmlPullParser.END_TAG -> if (parser.name == "bike") {
                        // Choose one description at random if available
                        val finalDescription = descriptions.randomOrNull()
                        if (!key.isNullOrBlank() && !finalDescription.isNullOrBlank() && !image.isNullOrBlank()) {
                            result.add(BikeDescription(key, finalDescription, image))
                        }
                        // Reset values for the next <bike>
                        key = null
                        descriptions = mutableListOf()
                        image = null
                    }
                }
                eventType = parser.next()
            }
        } catch (e: Exception) {
            e.printStackTrace() // Log any XML parsing errors
        } finally {
            parser.close() // Always close parser after use
        }

        return result
    }
}
