package com.atdev.githubproject.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RepositoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDownloadedRepository(repositoryDownloadedEntity: RepositoryDownloadedEntity)

    @Query("SELECT * FROM downloaded_table")
    fun getAllDownloadedRepository(): Flow<List<RepositoryDownloadedEntity>>

    @Delete
    fun deleteDownloadedRepository(item: RepositoryDownloadedEntity)

    @Query("DELETE FROM downloaded_table")
    suspend fun deleteAllDownloaded()

    @Query("SELECT COUNT(id) FROM downloaded_table")
    fun getCount(): Int

}