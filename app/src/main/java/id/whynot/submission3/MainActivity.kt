package id.whynot.submission3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import id.whynot.submission3.view.addpost.AddActivity
import id.whynot.submission3.view.maps.MapsActivity
import id.whynot.submission3.view.welcome.WellcomeActivity
import id.whynot.submission3.databinding.ActivityMainBinding
import id.whynot.submission3.model.ModelUserPreferences
import id.whynot.submission3.model.ModelImagePreference
import id.whynot.submission3.paging.adapter.LoadingStateAdapter
import id.whynot.submission3.paging.adapter.StoryListAdapter
import id.whynot.submission3.paging.ui.MainViewModel
import id.whynot.submission3.paging.ui.ViewModelFactory
import id.whynot.submission3.preference.Imagepreference
import id.whynot.submission3.preference.Userprefreference

class MainActivity : AppCompatActivity() {
    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_open
        )
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_close
        )
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.from_bottom
        )
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.to_bottom
        )
    }


    private var clicked = false

    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory(this, modelUserPreferences.token)
    }

    private lateinit var imagePreference: Imagepreference
    private lateinit var userPreference: Userprefreference
    private lateinit var binding: ActivityMainBinding
    private lateinit var modelUserPreferences: ModelUserPreferences
    private lateinit var modelImagePreference: ModelImagePreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userPreference = Userprefreference(this)
        imagePreference = Imagepreference(this)
        modelImagePreference = imagePreference.getUser()
        Log.e("onCreate: image1 ", modelImagePreference.image1.toString())
        refreshApp()

        binding.apply {
            rvList.layoutManager = LinearLayoutManager(this@MainActivity)
//            rvList.adapter = adapterList
            rvList.setHasFixedSize(true)
            actionfloat.setOnClickListener {
                floatButtonClick()
            }
            floatingbtn.setOnClickListener {
                floatButtonClick()
                val intent = Intent(this@MainActivity, AddActivity::class.java)
                intent.putExtra(AddActivity.EXTRA_TOKEN, modelUserPreferences.token)
                startActivity(intent)
            }
            MapsBtn.setOnClickListener {
                floatButtonClick()
                val intent = Intent(this@MainActivity, MapsActivity::class.java)
                startActivity(intent)
            }
        }
        showloading(true)
        checkUserexist()
        getData()
    }

    private fun checkUserexist() {
        modelUserPreferences = userPreference.getUser()
        if (modelUserPreferences.name.toString().isEmpty()) {

            val intent = Intent(this@MainActivity, WellcomeActivity::class.java)
            startActivity(intent)
            finish()
        } else {
//            listPost("Bearer ${modelUserPreferences.token}")
            Log.e("checkUserexist: ", "${modelUserPreferences.name}")
            Log.e("checkUserexist: ", "${modelUserPreferences.token}")
        }

    }

    private fun hapus() {
        val user = ModelUserPreferences(
            "",
            ""
        )
        userPreference.setUser(user)
        Log.e("saveuser:", "kosong")
        val intent = Intent(this@MainActivity, WellcomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.setting -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            R.id.quit -> {
                hapus()
                true
            }
            else -> {
                false
            }
        }
    }

    private fun showloading(istrue: Boolean) {
        if (istrue) {
            binding.blackscreen.visibility = View.VISIBLE
            binding.progressbar.visibility = View.VISIBLE
        } else {
            binding.blackscreen.visibility = View.GONE
            binding.progressbar.visibility = View.GONE
        }
    }

    private fun refreshApp() {
        binding.swipeToRefresh.setOnRefreshListener {
            getData()
            binding.swipeToRefresh.isRefreshing = false
        }
    }

    private fun getData() {
        val adapter = StoryListAdapter()
        binding.rvList.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        mainViewModel.story.observe(this) {
            showloading(false)
            adapter.submitData(lifecycle, it)
        }
    }

    private fun floatButtonClick() {
        visibilityBtn(clicked)
        animationBtn(clicked)
        clicked = !clicked
    }

    private fun visibilityBtn(clicked: Boolean) {
        if (!clicked) {
            binding.floatingbtn.visibility = View.VISIBLE
            binding.MapsBtn.visibility = View.VISIBLE
        } else {
            binding.floatingbtn.visibility = View.INVISIBLE
            binding.MapsBtn.visibility = View.INVISIBLE
        }
    }

    private fun animationBtn(clicked: Boolean) {
        if (!clicked) {
            binding.floatingbtn.startAnimation(fromBottom)
            binding.MapsBtn.startAnimation(fromBottom)
            binding.actionfloat.startAnimation(rotateOpen)
        } else {
            binding.floatingbtn.startAnimation(toBottom)
            binding.MapsBtn.startAnimation(toBottom)
            binding.actionfloat.startAnimation(rotateClose)
        }
    }
}


