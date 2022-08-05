package id.whynot.submission3.paging.di

import android.content.Context
import id.whynot.submission3.paging.data.StoryRepository
import id.whynot.submission3.paging.database.StoryDatabase
import id.whynot.submission3.paging.network.ApiConfig

object Injection {

    fun provideRepository(context: Context, token: String?): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository(context, database, apiService, token)
    }
}