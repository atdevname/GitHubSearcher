package com.atdev.githubproject.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RepositoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRepository(repository: RepositoryEntity)

    @Query("SELECT * FROM repository_table")
    fun getAllRepository(): Flow<List<RepositoryEntity>>

    @Query("SELECT * FROM repository_table")
    fun getAllRepositoryList(): List<RepositoryEntity>

    @Delete
    fun deleteRepository(item: RepositoryEntity)

    @Query("DELETE FROM repository_table")
    suspend fun deleteAll()

    @Query("SELECT COUNT(id) FROM repository_table")
    fun getCount(): Int
}