package com.atdev.githubproject.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RepositoryDao {

    /* Downloaded Table*/
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


    /* BySearch Table*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBySearchRepository(entityRepositoryBySearch: EntityRepositoryBySearch)

    @Query("SELECT * FROM bySearch_table")
    fun getAllBySearchRepository(): Flow<List<EntityRepositoryBySearch>>

    @Query("DELETE FROM bySearch_table")
    suspend fun deleteAllBySearch()


    /* ByUser Table*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addByUserRepository(entityRepositoryByUser: EntityRepositoryByUser)

    @Query("SELECT * FROM byUser_table")
    fun getAllByUserRepository(): Flow<List<EntityRepositoryByUser>>

    @Query("DELETE FROM byUser_table")
    suspend fun deleteAllByUser()
}