package com.example.data.repository

import com.example.domain.model.User
import com.example.domain.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MockAuthRepositoryImpl : AuthRepository {
    private val _currentUser = MutableStateFlow<User?>(null)

    override fun getCurrentUser(): Flow<User?> = _currentUser.asStateFlow()

    override suspend fun login(email: String, password: String): Result<User> {
        delay(1000)
        val user = User("1", "John", "Doe", email)
        _currentUser.value = user
        return Result.success(user)
    }

    override suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Result<User> {
        delay(1000)
        val user = User("1", firstName, lastName, email)
        _currentUser.value = user
        return Result.success(user)
    }

    override suspend fun logout() {
        delay(500)
        _currentUser.value = null
    }

    override suspend fun loginWithGoogle(idToken: String): Result<User> {
        delay(1000)
        val user = User("1", "Google", "User", "google@temp.com")
        _currentUser.value = user
        return Result.success(user)
    }

    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        delay(1000)
        return Result.success(Unit)
    }

    override suspend fun verifyPasswordResetOtp(email: String, otp: String): Result<Unit> {
        delay(1000)
        return if (otp == "123456") {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Invalid OTP"))
        }
    }

    override suspend fun updatePassword(password: String): Result<Unit> {
        delay(1000)
        return Result.success(Unit)
    }
}
