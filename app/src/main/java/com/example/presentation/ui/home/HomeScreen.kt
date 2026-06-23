package com.example.presentation.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.domain.model.Category
import com.example.domain.model.Video
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.VideoRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    videoRepository: VideoRepository,
    authRepository: AuthRepository,
    onVideoClick: (Video) -> Unit,
    onProfileClick: () -> Unit = {},
    onSearchClick: () -> Unit = {}
) {
    val categories by videoRepository.getCategories().collectAsState(initial = emptyList())
    val shorts by videoRepository.getHomeFeed(isShort = true).collectAsState(initial = emptyList())
    val longs by videoRepository.getHomeFeed(isShort = false).collectAsState(initial = emptyList())

    val currentUser by authRepository.getCurrentUser().collectAsState(initial = null)
    
    val initials = currentUser?.let {
        val first = it.firstName.take(1).uppercase()
        val last = it.lastName.take(1).uppercase()
        if (first.isNotBlank() || last.isNotBlank()) "$first$last" else "U"
    } ?: "U"

    LaunchedEffect(Unit) {
        videoRepository.refreshFeed()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        com.example.presentation.components.CircleAppLogo(
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            "Circle", 
                            style = MaterialTheme.typography.titleLarge, 
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onSearchClick) {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Box(
                        modifier = Modifier
                            .padding(end = 16.dp, start = 8.dp)
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.outline) // Slate-400 equivalent for avatar placeholder
                            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
                            .clickable(onClick = onProfileClick),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(initials, color = Color.White, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            item {
                LazyRow(
                    modifier = Modifier.padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    item {
                        CategoryChip("All", selected = true)
                    }
                    items(categories) { category ->
                        CategoryChip(category.name, selected = false)
                    }
                }
            }

            if (shorts.isNotEmpty()) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            "SHORTS",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            "See all",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp)
                    ) {
                        items(shorts) { video ->
                            ShortVideoCard(video = video, onClick = { onVideoClick(video) })
                        }
                    }
                }
            }
            
            item {
                Text(
                    "FOR YOU",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            items(longs) { video ->
                LongVideoCard(video, onClick = { onVideoClick(video) })
            }
        }
    }
}

@Composable
fun CategoryChip(label: String, selected: Boolean) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface)
            .border(
                width = 1.dp,
                color = if (selected) Color.Transparent else MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = if (selected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun ShortVideoCard(video: Video, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .size(112.dp, 176.dp)
            .clickable(onClick = onClick), // Approx w-28 h-44
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = video.thumbnailUrl,
                contentDescription = video.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            // Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f)),
                            startY = 100f
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
            ) {
                Text(
                    video.title,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    maxLines = 2
                )
                Text(
                    "@${video.creatorName}",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                    color = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}

@Composable
fun LongVideoCard(video: Video, onClick: () -> Unit = {}) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = onClick)
        .padding(bottom = 16.dp)) {
        Box {
            AsyncImage(
                model = video.thumbnailUrl,
                contentDescription = video.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
            // Time chip
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 24.dp, bottom = 8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.Black.copy(alpha = 0.8f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text("12:45", color = Color.White, fontSize = 10.sp, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
            }
        }
        Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    video.title, 
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 2
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    "${video.creatorName} • ${video.views}K views • 2 days ago", 
                    style = MaterialTheme.typography.bodySmall, 
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
