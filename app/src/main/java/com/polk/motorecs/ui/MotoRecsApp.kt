/**
 * Description: MotoRecs – Custom "Dream" Motorcycle Builder App
 * This app lets users build and customize their dream motorcycle
 * with options for style, and performance.
 *
 * @author Michael Polk
 * @since May 8, 2025
 */

package com.polk.motorecs.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.polk.motorecs.ui.screens.MainBuilderScreen
import com.polk.motorecs.ui.screens.MotoRecsHomeScreen
import com.polk.motorecs.ui.screens.RecentBuildsScreen
import com.polk.motorecs.ui.screens.SummaryScreen
import com.polk.motorecs.viewmodel.MotoRecsViewModel

@Composable
fun MotoRecsApp(viewModel: MotoRecsViewModel) {
    // Navigation controller to manage app screen transitions
    val navController: NavHostController = rememberNavController()

    // Define navigation graph with screen routes
    NavHost(
        navController = navController,
        startDestination = "home" // Home is the entry point
    ) {
        // Home screen: App entry point
        composable("home") {
            MotoRecsHomeScreen(
                onStartBuild = { navController.navigate("builder") }
            )
        }

        // Builder screen: Users select options to create a bike
        composable("builder") {
            MainBuilderScreen(
                viewModel = viewModel,
                onReviewClicked = { navController.navigate("summary") },
                onSavedBuildsClicked = { navController.navigate("recent") },
                onHomeClicked = { navController.navigate("home") },
                onBackToHomeClicked = { navController.popBackStack("home", inclusive = false) }
            )
        }

        // Summary screen: Final review and share before saving
        composable("summary") {
            SummaryScreen(
                viewModel = viewModel,
                onBackClicked = { navController.popBackStack() }
            )
        }

        // Recent builds screen: View and delete saved builds
        composable("recent") {
            RecentBuildsScreen(
                viewModel = viewModel,
                onBackToHomeClicked = { navController.navigate("home") },
                onGoBackClicked = { navController.popBackStack() }
            )
        }
    }
}


