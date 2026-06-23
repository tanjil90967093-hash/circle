package com.example.presentation.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.repository.AuthRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    authRepository: AuthRepository,
    onBack: () -> Unit,
    onLogout: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val currentUser by authRepository.getCurrentUser().collectAsState(initial = null)
    val scope = rememberCoroutineScope()

    val initials = currentUser?.let {
        val first = it.firstName.take(1).uppercase()
        val last = it.lastName.take(1).uppercase()
        if (first.isNotBlank() || last.isNotBlank()) "$first$last" else "U"
    } ?: "U"

    val fullName = currentUser?.let {
        if (it.firstName.isNotBlank() || it.lastName.isNotBlank()) "${it.firstName} ${it.lastName}"
        else "Unknown User"
    } ?: "Loading..."

    val email = currentUser?.email ?: "loading..."

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Channel/Profile Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(initials, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(fullName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text(email, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Create Channel", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { })
                    }
                    IconButton(onClick = { /* Switch Account/Channel */ }) {
                        Icon(Icons.Default.SwapHoriz, contentDescription = "Switch Channel")
                    }
                }
                Divider()
            }

            // Stats/Lists Options
            item {
                ProfileOptionItem(icon = Icons.Outlined.History, title = "History") {}
                ProfileOptionItem(icon = Icons.Outlined.ThumbUp, title = "Liked Videos") {}
                ProfileOptionItem(icon = Icons.Outlined.BookmarkBorder, title = "Saved Videos") {}
                ProfileOptionItem(icon = Icons.Outlined.Download, title = "Downloads") {}
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                ProfileOptionItem(icon = Icons.Default.VideoLibrary, title = "Your Videos") {}
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                ProfileOptionItem(icon = Icons.Outlined.Logout, title = "Logout", tint = MaterialTheme.colorScheme.error) {
                    scope.launch {
                        authRepository.logout()
                        onLogout()
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileOptionItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    tint: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = title, tint = tint)
        Spacer(modifier = Modifier.width(24.dp))
        Text(title, style = MaterialTheme.typography.bodyLarge, color = if (tint == MaterialTheme.colorScheme.error) tint else Color.Unspecified)
    }
}
