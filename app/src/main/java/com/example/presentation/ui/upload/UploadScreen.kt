package com.example.presentation.ui.upload

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.repository.VideoRepository

@Composable
fun UploadScreen(videoRepository: VideoRepository) {
    var selectedMode by remember { mutableStateOf("Shorts") }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        // Top options
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = {}) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
            }
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.DarkGray.copy(alpha = 0.5f))
                    .padding(4.dp)
            ) {
                TextButton(onClick = { /* 15s */ }, contentPadding = PaddingValues(horizontal = 8.dp)) { Text("15s", color = Color.White) }
                TextButton(onClick = { /* 60s */ }, contentPadding = PaddingValues(horizontal = 8.dp)) { Text("60s", color = Color.White) }
            }
            IconButton(onClick = {}) {
                Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.White)
            }
        }

        // Right side options
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            IconButton(onClick = {}) { Icon(Icons.Default.FlipCameraAndroid, contentDescription = "Flip", tint = Color.White) }
            IconButton(onClick = {}) { Icon(Icons.Default.FlashOn, contentDescription = "Flash", tint = Color.White) }
            IconButton(onClick = {}) { Icon(Icons.Default.Timer, contentDescription = "Timer", tint = Color.White) }
            IconButton(onClick = {}) { Icon(Icons.Default.MusicNote, contentDescription = "Audio", tint = Color.White) }
        }

        // Bottom Controls
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Mode selector (Shorts / Video)
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Text(
                    "Video", 
                    color = if (selectedMode == "Video") Color.White else Color.Gray,
                    fontWeight = if (selectedMode == "Video") FontWeight.Bold else FontWeight.Normal
                )
                Text(
                    "Shorts", 
                    color = if (selectedMode == "Shorts") Color.White else Color.Gray,
                    fontWeight = if (selectedMode == "Shorts") FontWeight.Bold else FontWeight.Normal
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Gallery Picker
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.DarkGray)
                            .border(1.dp, Color.White, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Outlined.PhotoLibrary, contentDescription = "Gallery", tint = Color.White)
                    }
                }

                // Record Button
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .border(4.dp, Color.Red, CircleShape)
                        .padding(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(Color.Red)
                    )
                }

                // Checks/Done Spacer
                Box(modifier = Modifier.size(40.dp))
            }
        }
    }
}
