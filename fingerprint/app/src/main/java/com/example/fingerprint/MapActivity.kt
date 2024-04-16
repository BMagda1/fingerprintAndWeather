package com.example.fingerprint

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // Inițializează fragmentul de hartă și așteaptă până când harta este gata pentru utilizare
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Setează centrul hărții pe România
        val romania = LatLng(45.9432, 24.9668)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(romania, 6f))

        // Adaugă un eveniment de clic pe hartă pentru a selecta o locație
        mMap.setOnMapClickListener { latLng ->
            val intent = Intent()
            intent.putExtra("latitude", latLng.latitude)
            intent.putExtra("longitude", latLng.longitude)
            setResult(Activity.RESULT_OK, intent)
            finish() // Termină activitatea și trimite înapoi rezultatul către activitatea anterioară
        }
    }
}
