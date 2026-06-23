package com.example.presentation.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.Video
import com.example.domain.repository.VideoRepository
import com.example.presentation.components.VideoPlayer

@Composable
fun VideoDetailsScreen(videoId: String, videoRepository: VideoRepository, onBack: () -> Unit) {
    // In a real app we'd fetch the exact video, for now we find it dynamically in feed
    val feed by videoRepository.getHomeFeed(false).collectAsState(initial = emptyList())
    val video = feed.find { it.id == videoId }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        // Video Player Header Area
        Box(modifier = Modifier.fillMaxWidth().aspectRatio(16f / 9f).background(Color.Black)) {
            if (video != null) {
                VideoPlayer(videoUrl = video.url, modifier = Modifier.fillMaxSize())
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
            
            IconButton(
                onClick = onBack, 
                modifier = Modifier.align(Alignment.TopStart).padding(8.dp)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            
            Row(modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)) {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.ScreenRotation, contentDescription = "Rotate", tint = Color.White)
                }
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.White)
                }
            }
        }

        LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f)) {
            if (video != null) {
                item {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(video.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("${video.views}K views • 2 days ago", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Creator Info
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.Gray))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(video.creatorName, fontWeight = FontWeight.Bold)
                                Text("120K followers", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Button(onClick = {}) {
                                Text("Follow")
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Buttons Horizontal Scroll
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            ActionButton(icon = Icons.Default.ThumbUp, label = "Like")
                            ActionButton(icon = Icons.Default.ThumbDown, label = "Dislike")
                            ActionButton(icon = Icons.Default.Share, label = "Share")
                            ActionButton(icon = Icons.Default.Download, label = "Download")
                            ActionButton(icon = Icons.Default.Bookmark, label = "Save")
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        Divider()
                        
                        // Comments Preview
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Comments 142", fontWeight = FontWeight.Bold)
                                Text("Great tutorial! Keep it up.", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Expand Comments")
                        }
                        Divider()
                    }
                }
            }
            
            item {
                Text(
                    "Related Videos",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            
            items(feed.size) { index ->
                val recommendedVideo = feed[index]
                if (video == null || recommendedVideo.id != video.id) {
                    LongVideoCard(recommendedVideo)
                }
            }
        }
    }
}

@Composable
fun ActionButton(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { }) {
        Icon(icon, contentDescription = label, tint = MaterialTheme.colorScheme.onSurface)
        Spacer(Modifier.height(4.dp))
        Text(label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
    }
}
