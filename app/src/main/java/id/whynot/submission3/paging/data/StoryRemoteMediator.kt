package id.whynot.submission3.paging.data


import android.content.Context
import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import id.whynot.submission3.model.ModelImagePreference
import id.whynot.submission3.paging.database.RemoteKeys
import id.whynot.submission3.paging.database.StoryDatabase
import id.whynot.submission3.paging.network.ApiService
import id.whynot.submission3.paging.network.StoryResponseItem
import id.whynot.submission3.preference.Imagepreference


@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val context: Context,
    private val database: StoryDatabase,
    private val apiService: ApiService,
    private val token: String?,
) : RemoteMediator<Int, StoryResponseItem>() {


    private companion object {
        const val INITIAL_PAGE_INDEX = 1
        lateinit var imagePreference: Imagepreference
    }

    override suspend fun initialize(): InitializeAction {

        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StoryResponseItem>
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val responseData = apiService.getQuote(
                "Bearer $token", page, state.config.pageSize
            )
            imagePreference = Imagepreference(context)
            Log.e("loadss: ", "mengambil ")
            val endOfPaginationReached = responseData.listStory.isEmpty()
            val image = ModelImagePreference(
                responseData.listStory[0].photoUrl,
                responseData.listStory[1].photoUrl,
                responseData.listStory[2].photoUrl,
                responseData.listStory[3].photoUrl,
            )
            imagePreference.setUser(image)
            Log.e("load: ", image.toString())
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao().deleteRemoteKeys()
                    database.quoteDao().deleteAll()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = responseData.listStory.map {
                    RemoteKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                database.remoteKeysDao().insertAll(keys)
                database.quoteDao().insertQuote(responseData.listStory)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, StoryResponseItem>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, StoryResponseItem>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, StoryResponseItem>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteKeysDao().getRemoteKeysId(id)
            }
        }
    }

}