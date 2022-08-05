package id.whynot.submission3

import id.whynot.submission3.paging.network.StoryResponseItem

object DataDummy {

    fun generateDummyStoryResponse(): List<StoryResponseItem> {
        val items: MutableList<StoryResponseItem> = arrayListOf()
        for (i in 0..100) {
            val story = StoryResponseItem(
                i.toString(),
                "name + $i",
                "description $i",
                "https://story-api.dicoding.dev/images/stories/photos-1651035964652_0AavsGjW.jpg"
            )
            items.add(story)
        }
        return items
    }
}