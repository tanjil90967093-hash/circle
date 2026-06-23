package com.example.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.di.AppContainer
import com.example.presentation.components.CircleBottomNavigation
import com.example.presentation.ui.home.HomeScreen
import com.example.presentation.ui.home.VideoDetailsScreen
import com.example.presentation.ui.onboarding.OnboardingScreen
import com.example.presentation.ui.auth.ForgotPasswordScreen
import com.example.presentation.ui.auth.LoginScreen
import com.example.presentation.ui.auth.SignupScreen
import com.example.presentation.ui.shorts.ShortsScreen
import com.example.presentation.ui.upload.UploadScreen
import com.example.presentation.ui.following.FollowingScreen
import com.example.presentation.ui.notifications.NotificationsScreen
import com.example.presentation.ui.profile.ProfileScreen
import com.example.presentation.ui.profile.SettingsScreen

@Composable
fun CircleApp(container: AppContainer) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavRoutes = listOf(
        "home", "shorts", "upload", "following", "notifications"
    )

    Scaffold(
        bottomBar = {
            if (currentRoute in bottomNavRoutes) {
                CircleBottomNavigation(
                    currentRoute = currentRoute ?: "home",
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "onboarding",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("onboarding") {
                OnboardingScreen(
                    onGetStarted = { navController.navigate("login") { popUpTo("onboarding") { inclusive = true } } }
                )
            }
            composable("login") {
                LoginScreen(
                    authRepository = container.authRepository,
                    onLoginSuccess = { navController.navigate("home") { popUpTo("login") { inclusive = true } } },
                    onNavigateSignup = { navController.navigate("signup") },
                    onNavigateForgotPassword = { navController.navigate("forgot_password") }
                )
            }
            composable("forgot_password") {
                ForgotPasswordScreen(
                    authRepository = container.authRepository,
                    onNavigateLogin = { navController.navigate("login") { popUpTo("login") { inclusive = true } } }
                )
            }
            composable("signup") {
                SignupScreen(
                    authRepository = container.authRepository,
                    onSignupSuccess = { navController.navigate("home") { popUpTo("signup") { inclusive = true } } },
                    onNavigateLogin = { navController.navigate("login") { popUpTo("signup") { inclusive = true } } }
                )
            }
            composable("home") {
                HomeScreen(
                    videoRepository = container.videoRepository,
                    authRepository = container.authRepository,
                    onVideoClick = { video ->
                        if (video.isShort) {
                            navController.navigate("shorts?videoId=${video.id}")
                        } else {
                            navController.navigate("video/${video.id}")
                        }
                    },
                    onProfileClick = {
                        navController.navigate("profile")
                    },
                    onSearchClick = {
                        navController.navigate("search")
                    }
                )
            }
            composable("search") {
                com.example.presentation.ui.search.SearchScreen(
                    videoRepository = container.videoRepository,
                    onBackClick = { navController.popBackStack() },
                    onVideoClick = { video ->
                        if (video.isShort) {
                            navController.navigate("shorts?videoId=${video.id}")
                        } else {
                            navController.navigate("video/${video.id}")
                        }
                    }
                )
            }
            composable("profile") {
                ProfileScreen(
                    authRepository = container.authRepository,
                    onBack = { navController.popBackStack() },
                    onLogout = {
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onSettingsClick = {
                        navController.navigate("settings")
                    }
                )
            }
            composable("settings") {
                SettingsScreen(
                    authRepository = container.authRepository,
                    onBack = { navController.popBackStack() },
                    onLogout = {
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
            composable("video/{videoId}") { backStackEntry ->
                val videoId = backStackEntry.arguments?.getString("videoId") ?: return@composable
                VideoDetailsScreen(
                    videoId = videoId,
                    videoRepository = container.videoRepository,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(
                "shorts?videoId={videoId}",
                arguments = listOf(androidx.navigation.navArgument("videoId") { nullable = true })
            ) { backStackEntry ->
                val videoId = backStackEntry.arguments?.getString("videoId")
                ShortsScreen(videoRepository = container.videoRepository, initialVideoId = videoId)
            }
            composable("upload") {
                UploadScreen(
                    videoRepository = container.videoRepository,
                    uploadRepository = container.uploadRepository
                )
            }
            composable("following") {
                FollowingScreen(videoRepository = container.videoRepository)
            }
            composable("notifications") {
                NotificationsScreen()
            }
        }
    }
}
