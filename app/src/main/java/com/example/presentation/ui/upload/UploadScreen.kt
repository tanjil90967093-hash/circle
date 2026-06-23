package com.example.presentation.ui.upload

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.repository.UploadRepository
import com.example.domain.repository.VideoRepository
import kotlinx.coroutines.launch

@Composable
fun UploadScreen(
    videoRepository: VideoRepository,
    uploadRepository: UploadRepository
) {
    var selectedMode by remember { mutableStateOf("Shorts") }
    
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isUploading by remember { mutableStateOf(false) }
    var uploadStatus by remember { mutableStateOf("") }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            isUploading = true
            uploadStatus = "Uploading..."
            scope.launch {
                val fileName = "upload_${System.currentTimeMillis()}.mp4"
                val mimeType = context.contentResolver.getType(uri) ?: "video/mp4"
                val result = uploadRepository.uploadFile(uri, fileName, mimeType)
                isUploading = false
                uploadStatus = result.fold(
                    onSuccess = { "Upload successful!" },
                    onFailure = { "Upload failed: ${it.message}" }
                )
            }
        }
    }

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
        
        // Status indicator
        if (isUploading || uploadStatus.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(8.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (isUploading) {
                        CircularProgressIndicator(color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    Text(uploadStatus, color = Color.White)
                }
            }
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
                    fontWeight = if (selectedMode == "Video") FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier.clickable { selectedMode = "Video" }
                )
                Text(
                    "Shorts", 
                    color = if (selectedMode == "Shorts") Color.White else Color.Gray,
                    fontWeight = if (selectedMode == "Shorts") FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier.clickable { selectedMode = "Shorts" }
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
                            .border(1.dp, Color.White, RoundedCornerShape(8.dp))
                            .clickable {
                                launcher.launch("video/*")
                            },
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
