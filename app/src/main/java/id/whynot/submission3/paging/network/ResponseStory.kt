package id.whynot.submission3.paging.network

import com.google.gson.annotations.SerializedName

data class ResponseStory(
    @field:SerializedName("error")
    val error: String,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("listStory")
    val listStory: List<StoryResponseItem>
)