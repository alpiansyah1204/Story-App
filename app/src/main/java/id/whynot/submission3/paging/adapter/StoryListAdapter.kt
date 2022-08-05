package id.whynot.submission3.paging.adapter

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import id.whynot.submission3.view.detail.DetailActivity
import id.whynot.submission3.databinding.ItemPostRowBinding
import id.whynot.submission3.model.Post
import id.whynot.submission3.paging.network.StoryResponseItem


class StoryListAdapter :
    PagingDataAdapter<StoryResponseItem, StoryListAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemPostRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    class MyViewHolder(private val binding: ItemPostRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: StoryResponseItem) {
            binding.tvItemName.text = data.name
            Glide.with(itemView.context)
                .load(data.photoUrl)
                .centerCrop()
                .dontTransform()
                .apply(RequestOptions().override(600, 600))
                .into(binding.imgItemPhoto)

            itemView.setOnClickListener {
                Log.e("bind: ", data.name)
                val intent = Intent(itemView.context, DetailActivity::class.java)
                val post = Post(data.name, data.description, data.photoUrl)
                intent.putExtra(DetailActivity.EXTRA_POST, post)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.tvItemName, "username"),
                        Pair(binding.imgItemPhoto, "image")
                    )
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryResponseItem>() {
            override fun areItemsTheSame(
                oldItem: StoryResponseItem,
                newItem: StoryResponseItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: StoryResponseItem,
                newItem: StoryResponseItem
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}