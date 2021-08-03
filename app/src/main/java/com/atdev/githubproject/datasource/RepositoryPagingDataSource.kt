package com.atdev.githubproject.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.atdev.githubproject.model.RepositoryObjectDto
import com.atdev.githubproject.api.ApiService
import kotlinx.coroutines.*
import retrofit2.HttpException

class RepositoryPagingDataSource(
    private val apiService: ApiService,
    private val query: String
) : PagingSource<Int, RepositoryObjectDto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RepositoryObjectDto> {
        if (query.isBlank()) {
            return LoadResult.Page(emptyList(), prevKey = null, nextKey = null)
        }

        var loadResult:LoadResult<Int, RepositoryObjectDto>? = null

        coroutineScope {
            val pageNumber = params.key ?: 1
            val pageSize = params.loadSize.coerceAtMost(20)

            val defResponse:Deferred<LoadResult<Int, RepositoryObjectDto>> =
            async {
                val response = apiService.searchRepos(query)
                return@async if (response.isSuccessful) {
                    val bodyResponse = response.body()!!.items
                    val nextPageNumber = if (bodyResponse.isEmpty()) null else pageNumber + 1
                    val prevPageNumber = if (pageNumber > 1) pageNumber - 1 else null
                    LoadResult.Page(bodyResponse, prevPageNumber, nextPageNumber)
                } else {
                    LoadResult.Error(HttpException(response))
                }
            }
            loadResult = defResponse.await()
        }
        return loadResult!!
    }

    override fun getRefreshKey(state: PagingState<Int, RepositoryObjectDto>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }
}

//        val pageNumber = params.key ?: 1
//        val pageSize = params.loadSize.coerceAtMost(20)
//
//        val response = apiService.searchRepos(query)
//        return if (response.isSuccessful) {
//            val bodyResponse = response.body()!!.items
//            val nextPageNumber = if (bodyResponse.isEmpty()) null else pageNumber + 1
//            val prevPageNumber = if (pageNumber > 1) pageNumber - 1 else null
//            LoadResult.Page(bodyResponse, prevPageNumber, nextPageNumber)
//        } else {
//            LoadResult.Error(HttpException(response))
//        }
//        try {
//
//        } catch (e: HttpException) {
//            return LoadResult.Error(e)
//        } catch (e: Exception) {
//            return LoadResult.Error(e)
//        }