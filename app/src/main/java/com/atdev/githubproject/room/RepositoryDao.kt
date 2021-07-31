package com.atdev.githubproject.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RepositoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDownloadedRepository(repositoryDownloaded: EntityRepositoryDownloaded)

    @Query("SELECT * FROM downloaded_table")
    fun getAllDownloadedRepository(): Flow<List<EntityRepositoryDownloaded>>

    @Delete
    fun deleteDownloadedRepository(item: EntityRepositoryDownloaded)

    @Query("DELETE FROM downloaded_table")
    suspend fun deleteAllDownloaded()

    @Query("SELECT COUNT(id) FROM downloaded_table")
    fun getCount(): Int

}