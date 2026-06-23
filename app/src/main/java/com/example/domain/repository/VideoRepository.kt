package com.example.domain.repository

import com.example.domain.model.Category
import com.example.domain.model.Comment
import com.example.domain.model.Video
import kotlinx.coroutines.flow.Flow

interface VideoRepository {
    fun getHomeFeed(isShort: Boolean): Flow<List<Video>>
    fun getCategories(): Flow<List<Category>>
    suspend fun getComments(videoId: String): Flow<List<Comment>>
    
    suspend fun uploadVideo(
        title: String, 
        description: String, 
        uri: String, 
        isShort: Boolean, 
        category: String, 
        allowDownload: Boolean
    ): Result<Unit>
    
    suspend fun likeVideo(videoId: String)
    suspend fun saveVideo(videoId: String)
    suspend fun refreshFeed()
}
