package com.example.data.repository

import com.example.data.local.VideoDao
import com.example.data.local.toDomain
import com.example.data.local.toEntity
import com.example.domain.model.Category
import com.example.domain.model.Comment
import com.example.domain.model.Video
import com.example.domain.repository.VideoRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class MockVideoRepositoryImpl(
    private val dao: VideoDao
) : VideoRepository {

    override fun getHomeFeed(isShort: Boolean): Flow<List<Video>> {
        return dao.getVideos(isShort).map { entities -> entities.map { it.toDomain() } }
    }

    override fun getCategories(): Flow<List<Category>> {
        return flow {
            emit(listOf(
                Category("1", "Music"),
                Category("2", "Movies"),
                Category("3", "News"),
                Category("4", "Gaming"),
                Category("5", "Education"),
                Category("6", "Sports")
            ))
        }
    }

    override suspend fun getComments(videoId: String): Flow<List<Comment>> {
        return flow {
            emit(listOf(
                Comment("1", videoId, "u1", "Alice", null, "Awesome video!"),
                Comment("2", videoId, "u2", "Bob", null, "I agree.", replies = listOf(
                    Comment("3", videoId, "u3", "Charlie", null, "Definitely.")
                ))
            ))
        }
    }

    override suspend fun uploadVideo(
        title: String,
        description: String,
        uri: String,
        isShort: Boolean,
        category: String,
        allowDownload: Boolean
    ): Result<Unit> {
        delay(2000)
        return Result.success(Unit)
    }

    override suspend fun likeVideo(videoId: String) {
        // mock
    }

    override suspend fun saveVideo(videoId: String) {
        // mock
    }

    override suspend fun refreshFeed() {
        val mockShorts = (1..10).map {
            Video(
                id = "short_$it",
                title = "Captelation short video $it",
                description = "Captelation short video explaining the topic briefly",
                url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
                thumbnailUrl = "https://storage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerBlazes.jpg",
                creatorId = "c1",
                creatorName = "Captelation Creator",
                creatorProfilePic = null,
                isShort = true,
                category = "Education"
            )
        }
        val mockLongs = (1..10).map {
            Video(
                id = "long_$it",
                title = "Captelation video $it",
                description = "Long Captelation video discussing the topic in depth.",
                url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                thumbnailUrl = "https://storage.googleapis.com/gtv-videos-bucket/sample/images/BigBuckBunny.jpg",
                creatorId = "c2",
                creatorName = "Captelation Expert",
                creatorProfilePic = null,
                isShort = false,
                category = "Education"
            )
        }
        
        dao.insertVideos(mockShorts.map { it.toEntity() })
        dao.insertVideos(mockLongs.map { it.toEntity() })
    }
}
