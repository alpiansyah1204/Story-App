package id.whynot.submission3.view.maps

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import id.whynot.submission3.R
import id.whynot.submission3.view.detail.DetailActivity
import id.whynot.submission3.databinding.ActivityMapsBinding
import id.whynot.submission3.model.ModelUserPreferences
import id.whynot.submission3.model.Post
import id.whynot.submission3.preference.Userprefreference
import id.whynot.submission3.viewmodel.StoryViewModel


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var userprefreference: Userprefreference
    private lateinit var modelUserPreferences: ModelUserPreferences
    private lateinit var story: Post
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var viewModel: StoryViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[StoryViewModel::class.java]
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        story = Post()
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        userprefreference = Userprefreference(this)
        modelUserPreferences = userprefreference.getUser()
        getdata("Bearer ${modelUserPreferences.token}")

    }

    private fun getdata(token: String) {
        viewModel.setStoryMaps(this@MapsActivity, token)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        viewModel.getStoryMaps().observe(this@MapsActivity) { post ->
            for (posisition in post.indices) {
                Log.e("onMapReady: ", post[posisition].toString())
                mMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(post[posisition].lat!!, post[posisition].lon!!))
                        .title(post[posisition].name)
                        .snippet(post[posisition].description)

                )

                mMap.moveCamera(
                    CameraUpdateFactory.newLatLng(
                        LatLng(
                            post[posisition].lat!!,
                            post[posisition].lon!!
                        )
                    )
                )

            }
            mMap.setOnInfoWindowClickListener {
                Log.e("harus ready: ", "${it.title}")
                val intent = Intent(this@MapsActivity, DetailActivity::class.java)
                for (posisition in post.indices) {
                    if (it.title == post[posisition].name && it.snippet == post[posisition].description) {
                        Log.e("harus diklik: ", "${it.title}")
                        intent.putExtra(DetailActivity.EXTRA_POST, post[posisition])
                    }
                }
                startActivity(intent)
            }
        }
        setMapStyle()
        getMyLocation()
        getMyLastLocation()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
                getMyLastLocation()

            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true


        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    Log.e("getMyLastLocation: ", "${location.longitude}")
                    Log.e("getMyLastLocation: ", "${location.latitude}")
                } else {
                    Toast.makeText(
                        this@MapsActivity,
                        "Location is not found. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e("Style parsing failed.", "failed")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e("Can't find style : ", exception.toString())
        }
    }
}
