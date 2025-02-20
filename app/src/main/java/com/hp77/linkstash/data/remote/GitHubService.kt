package com.hp77.linkstash.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.Response
import retrofit2.http.*

interface GitHubService {
    @GET("user")
    suspend fun getCurrentUser(
        @Header("Authorization") token: String
    ): Response<GitHubUser>

    @GET("repos/{owner}/{repo}")
    suspend fun getRepository(
        @Header("Authorization") token: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Response<GitHubRepo>

    @POST("user/repos")
    suspend fun createRepository(
        @Header("Authorization") token: String,
        @Body repo: CreateRepoRequest
    ): Response<GitHubRepo>

    @GET("repos/{owner}/{repo}/contents/{path}")
    suspend fun getFileContent(
        @Header("Authorization") token: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("path") path: String
    ): Response<GitHubContent>

    @GET("repos/{owner}/{repo}/contents/{path}")
    suspend fun listDirectoryContents(
        @Header("Authorization") token: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("path") path: String
    ): Response<List<GitHubContent>>

    @PUT("repos/{owner}/{repo}/contents/{path}")
    suspend fun updateFile(
        @Header("Authorization") token: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("path") path: String,
        @Body request: UpdateFileRequest
    ): Response<GitHubUpdateResponse>
}

@JsonClass(generateAdapter = true)
data class GitHubUser(
    val login: String,
    val id: Long,
    val name: String?,
    @Json(name = "avatar_url") val avatarUrl: String,
    val bio: String?,
    val location: String?,
    @Json(name = "public_repos") val publicRepos: Int,
    val followers: Int,
    val following: Int,
    @Json(name = "created_at") val createdAt: String
)

@JsonClass(generateAdapter = true)
data class GitHubRepo(
    val id: Long,
    val name: String,
    @Json(name = "full_name") val fullName: String,
    val private: Boolean,
    val description: String?
)

@JsonClass(generateAdapter = true)
data class GitHubContent(
    val type: String,
    val encoding: String?,
    val size: Long,
    val name: String,
    val path: String,
    val content: String?,
    val sha: String
)

@JsonClass(generateAdapter = true)
data class CreateRepoRequest(
    val name: String,
    val description: String?,
    val private: Boolean = true,
    @Json(name = "auto_init") val autoInit: Boolean = true
)

@JsonClass(generateAdapter = true)
data class UpdateFileRequest(
    val message: String,
    val content: String, // Base64 encoded content
    val sha: String? = null // Required for updating existing files
)

@JsonClass(generateAdapter = true)
data class GitHubUpdateResponse(
    val content: GitHubContent,
    val commit: GitHubCommit
)

@JsonClass(generateAdapter = true)
data class GitHubCommit(
    val sha: String,
    val message: String
)
