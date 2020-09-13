package com.myha.coin.data.model

import androidx.paging.PagingSource
import com.myha.coin.data.api.ApiHelper
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1

class AnimalPagingSource (private val service: ApiHelper, private val query: String):
    PagingSource<Int, Animal>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Animal> {
        val position = params.key ?: STARTING_PAGE_INDEX
        val apiQuery = query
        return try {
            val response = service.getAnimals(apiQuery, position)
            val animals = response.animals
            LoadResult.Page(
                data = animals,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (animals.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}