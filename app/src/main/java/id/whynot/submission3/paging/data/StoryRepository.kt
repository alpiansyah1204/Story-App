package id.whynot.submission3.paging.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.*
import id.whynot.submission3.paging.database.StoryDatabase
import id.whynot.submission3.paging.network.ApiService
import id.whynot.submission3.paging.network.StoryResponseItem


class StoryRepository(
    private val context: Context,
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService,
    private val token: String?
) {
    fun getQuote(): LiveData<PagingData<StoryResponseItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 10
            ),
            remoteMediator = StoryRemoteMediator(context, storyDatabase, apiService, token),
            pagingSourceFactory = {
                storyDatabase.quoteDao().getAllQuote()
            }
        ).liveData
    }
}
