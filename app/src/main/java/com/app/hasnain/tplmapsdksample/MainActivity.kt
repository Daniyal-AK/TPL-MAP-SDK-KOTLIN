package com.app.hasnain.tplmapsdksample

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.tplmaps.sdk.places.LngLat
import com.tplmaps.sdk.places.OnSearchResult
import com.tplmaps.sdk.places.Params
import com.tplmaps.sdk.places.Place
import com.tplmaps.sdk.places.SearchManager
import com.tplmaps3d.CameraPosition
import com.tplmaps3d.MapController
import com.tplmaps3d.MapView
import com.tplmaps3d.sdk.utils.CommonUtils

class MainActivity : AppCompatActivity(), MapView.OnMapReadyCallback, OnSearchResult {

    private lateinit var mMapView: MapView
    private lateinit var tvFullAdd: TextView
    private lateinit var searchManager: SearchManager
    private lateinit var etSearch: EditText
    private var searchLngLat: LngLat? = null
    private var isSearch = false
    private lateinit var mMapController: MapController

    private lateinit var recyclerView: RecyclerView
    private lateinit var placesAdapter: PlacesAdapter

    private lateinit var ibZoomIn: ImageButton
    private lateinit var ibZoomOut: ImageButton
    private lateinit var ibCurrentLocation: ImageButton
    private lateinit var cardViewList:MaterialCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI elements
        mMapView = findViewById(R.id.map)
        tvFullAdd = findViewById(R.id.tv_full_add)
        recyclerView = findViewById(R.id.recyclerView)
        cardViewList = findViewById(R.id.cardViewList)
        ibZoomIn = findViewById(R.id.ibZoomIn)
        ibZoomOut = findViewById(R.id.ibZoomOut)
        ibCurrentLocation = findViewById(R.id.ibLocateMe)
        etSearch = findViewById(R.id.etSearch)

        searchManager = SearchManager(this)

        // Set up map
        mMapView.onCreate(savedInstanceState)
        mMapView.loadMapAsync(this)

        // Set up search functionality
        etSearch.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                actionId == EditorInfo.IME_ACTION_DONE ||
                event?.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER
            ) {
                if (etSearch.text.toString().isNotEmpty()) {
                    searchManager.requestOptimizeSearch(
                        Params.builder()
                            .query(etSearch.text.toString())
                            .location(searchLngLat)
                            .build(), this
                    )
                    isSearch = true
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(etSearch.windowToken, 0)
                }
                true
            } else {
                false
            }
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isEmpty()) {
                    if (recyclerView.visibility == View.VISIBLE) {
                        recyclerView.visibility = View.GONE
                        cardViewList.visibility = View.GONE
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Set up zoom and location buttons
        ibZoomIn.setOnClickListener {
            mMapController.setZoomBy(mMapController.mapCameraPosition.zoom + 1)
        }

        ibZoomOut.setOnClickListener {
            mMapController.setZoomBy(mMapController.mapCameraPosition.zoom - 1)
        }

        ibCurrentLocation.setOnClickListener {
            val myLocation = mMapController.getMyLocation(mMapView)
            val cameraPosition = CameraPosition(
                mMapController, com.tplmaps3d.LngLat(
                    myLocation.longitude, myLocation.latitude
                ), 14.0f, 0.0f, 0.0f
            )
            mMapController.animateCamera(cameraPosition, 1000)
        }
    }

    override fun onMapReady(mapController: MapController) {
        CommonUtils.showToast(this, "Map Ready", Toast.LENGTH_SHORT, false)

        mapController.removeCurrentLocationMarker()
        mMapController = mapController
        mapController.setMaxTilt(85F)
        mapController.uiSettings.showZoomControls(false)
        mapController.uiSettings.showMyLocationButton(false)
        mapController.setMyLocationEnabled(true, MapController.MyLocationArg.ZOOM_LOCATION_ON_FIRST_FIX)

        mapController.setOnCameraChangeEndListener { cameraPosition ->
            runOnUiThread {
                searchLngLat = LngLat(cameraPosition.position.longitude, cameraPosition.position.latitude)

                tvFullAdd.text = String.format("%.4f", cameraPosition.position.latitude) + " ; " +
                        String.format("%.4f", cameraPosition.position.longitude)

                searchManager.requestReverse(
                    Params.builder()
                        .location(LngLat(cameraPosition.position.latitude, cameraPosition.position.longitude))
                        .build(), this
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mMapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mMapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mMapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView.onLowMemory()
    }

    override fun onSearchResult(results: ArrayList<Place>) {
        Log.d("TAG", "onSearchResult: $results")
        if (isSearch) {
            populateRecyclerView(results)
            isSearch = false
        } else {
            tvFullAdd.text = results[0].address
        }
    }

    override fun onSearchResultNotFound(params: Params, requestTimeInMS: Long) {}

    override fun onSearchRequestFailure(e: Exception) {}

    override fun onSearchRequestCancel(params: Params, requestTimeInMS: Long) {}

    override fun onSearchRequestSuspended(errorMessage: String, params: Params, requestTimeInMS: Long) {}

    private fun populateRecyclerView(results: ArrayList<Place>?) {

        recyclerView.visibility = View.VISIBLE
        cardViewList.visibility = View.VISIBLE
        recyclerView.layoutManager = LinearLayoutManager(this)

        placesAdapter = results?.let {
            PlacesAdapter(it) { place ->
                // Handle item click
                val strLocation = "${place.name}\n${place.y},${place.x}"

                mMapController.setLngLatZoom(
                    com.tplmaps3d.LngLat(place.x.toDouble(), place.y.toDouble()), 15.0f
                )
                tvFullAdd.text = place.address

                clearRecyclerView()
            }
        }!!

        recyclerView.adapter = placesAdapter
    }

    private fun clearRecyclerView() {
        placesAdapter = PlacesAdapter(emptyList()) {}
        recyclerView.adapter = placesAdapter
        etSearch.text.clear()
        recyclerView.visibility = View.GONE
        cardViewList.visibility = View.GONE
    }
}
