/**
 * Description: MotoRecs – Custom "Dream" Motorcycle Builder App
 * This app lets users build and customize their dream motorcycle
 * with options for style, and performance.
 *
 * @author Michael Polk
 * @since May 8, 2025
 */

package com.polk.motorecs.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.polk.motorecs.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MotoRecsHomeScreen(
    onStartBuild: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        //  Background image
        Image(
            painter = painterResource(id = R.drawable.bike_home_screen),
            contentDescription = "Bike Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Semi-transparent overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.6f),
                            Color.Black.copy(alpha = 0.9f)
                        )
                    )
                )
        )

        // Foreground content
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "MotoRecs",
                color = Color(0xFFFFA500),
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 34.sp)
            )
            Text(
                text = "Build Your Dream Bike!",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onStartBuild,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Start Building")
            }
        }
    }
}
