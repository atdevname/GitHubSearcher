package com.atdev.githubproject.collection.room

import androidx.room.*
import com.atdev.githubproject.search.model.RepositoryCollectionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RepositoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRep(repositoryCollectionEntity: RepositoryCollectionEntity)

    @Query("SELECT * FROM downloaded_table")
    fun getAllSavedReps(): Flow<List<RepositoryCollectionEntity>>

    @Delete
    suspend fun deleteSavedRep(item: RepositoryCollectionEntity)

    @Query("DELETE FROM downloaded_table")
    suspend fun deleteAllSavedReps()

    @Query("SELECT COUNT(id) FROM downloaded_table")
    fun getCount(): Int

}