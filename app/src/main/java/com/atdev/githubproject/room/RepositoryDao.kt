package com.atdev.githubproject.room

import androidx.room.*
import com.atdev.githubproject.model.RepositoryCollectionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RepositoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDownloadedRepository(repositoryCollectionEntity: RepositoryCollectionEntity)

    @Query("SELECT * FROM downloaded_table")
    fun getAllDownloadedRepository(): Flow<List<RepositoryCollectionEntity>>

    @Delete
    suspend fun deleteDownloadedRepository(item: RepositoryCollectionEntity)

    @Query("DELETE FROM downloaded_table")
    suspend fun deleteAllDownloaded()

    @Query("SELECT COUNT(id) FROM downloaded_table")
    fun getCount(): Int

}