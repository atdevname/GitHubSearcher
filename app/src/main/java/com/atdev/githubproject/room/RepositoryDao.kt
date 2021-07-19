package com.atdev.githubproject.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface RepositoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRepository(repository: RepositoryEntity)

    @Query("SELECT * FROM repository_table")
    fun getAllRepository(): Flow<List<RepositoryEntity>>

    @Query("SELECT * FROM repository_table")
    fun getAllRepositoryList(): List<RepositoryEntity>

    @Query("SELECT COUNT(id) FROM repository_table")
    fun getCount(): Int

    @Query("DELETE FROM repository_table")
    suspend fun deleteAll()

}