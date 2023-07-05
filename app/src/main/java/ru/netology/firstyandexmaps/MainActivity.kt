package ru.netology.firstyandexmaps

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider


class MainActivity : AppCompatActivity() {
    private val MAPKIT_API_KEY = "" //Вставить свой ключ
    private var mapView: MapView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        mapView = findViewById(R.id.map)
        val placemarkTapListener = MapObjectTapListener { _, point ->
            Toast.makeText(
                this@MainActivity,
                "Tapped the point (${point.longitude}, ${point.latitude})", Toast.LENGTH_SHORT).show()
            true
        }
        val icon = ImageProvider.fromResource(this, R.drawable.ic_baseline_place_24)
        MapKitFactory.setApiKey(MAPKIT_API_KEY)
        MapKitFactory.initialize(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val inputListener = object : InputListener{
            override fun onMapTap(p0: Map, p1: Point) {
                val placemark = mapView?.map?.mapObjects?.addPlacemark(p1,icon )
                placemark?.addTapListener(placemarkTapListener)
            }

            override fun onMapLongTap(p0: Map, p1: Point) {
                val placemark = mapView?.map?.mapObjects?.addPlacemark(p1,icon )
                placemark?.addTapListener(placemarkTapListener)
            }

        }
        mapView?.map?.addInputListener(inputListener)


    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart();
        mapView?.onStart();
    }


    override fun onStop() {
        mapView?.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop()
    }
}