package id.whynot.submission3.paging.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import id.whynot.submission3.paging.data.StoryRepository
import id.whynot.submission3.paging.di.Injection
import id.whynot.submission3.paging.network.StoryResponseItem


class MainViewModel(storyRepository: StoryRepository) : ViewModel() {

    val story: LiveData<PagingData<StoryResponseItem>> =
        storyRepository.getQuote().cachedIn(viewModelScope)
}

class ViewModelFactory(private val context: Context, private val token: String?) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(Injection.provideRepository(context, token)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}