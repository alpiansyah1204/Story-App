package id.whynot.submission3.view.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import id.whynot.submission3.databinding.ActivityDetailBinding
import id.whynot.submission3.model.Post

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = intent.getParcelableExtra<Post>(EXTRA_POST) as Post
        binding.tvItemName.text = post.name.toString()
        binding.tvItemDescription.text = post.description.toString()
        Glide.with(applicationContext)
            .load(post.photoUrl)
            .centerCrop()
            .dontTransform()
            .apply(RequestOptions().override(600, 600))
            .into(binding.imgItemPhoto)
    }

    companion object {
        const val EXTRA_POST = "extra_post"
    }
}