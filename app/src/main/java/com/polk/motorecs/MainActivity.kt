/**
 * Description: MotoRecs – Custom "Dream" Motorcycle Builder App
 * This app lets users build and customize their dream motorcycle
 * with options for style, and performance.
 *
 * @author Michael Polk
 * @since May 8, 2025
 */

package com.polk.motorecs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.polk.motorecs.data.MotoRecsRepository
import com.polk.motorecs.ui.MotoRecsApp
import com.polk.motorecs.ui.theme.MotoRecsTheme
import com.polk.motorecs.viewmodel.MotoRecsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize repository and viewmodel manually
        val repository = MotoRecsRepository(applicationContext)
        val viewModel = MotoRecsViewModel(repository)

        // Set the Compose content with your theme and app entry point
        setContent {
            MotoRecsTheme(darkTheme = true) {
                MotoRecsApp(viewModel = viewModel)
            }
        }
    }
}



