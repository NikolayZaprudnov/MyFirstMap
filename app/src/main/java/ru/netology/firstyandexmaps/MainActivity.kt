package ru.netology.firstyandexmaps

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider


class MainActivity : AppCompatActivity() {
    private val MAPKIT_API_KEY = "a1350e43-6644-467b-a3a2-88591bdd73fc" //Вставить свой ключ
    private lateinit var mapView: MapView
//    private  var  icon = ImageProvider.fromResource(applicationContext, R.drawable.ic_baseline_place_24)

    private val placemarkTapListener = MapObjectTapListener { _, point ->
        Toast.makeText(
            this@MainActivity,
            "Tapped the point (${point.longitude}, ${point.latitude})", Toast.LENGTH_SHORT).show()
        true
    }

    private val inputListener = object : InputListener {
        override fun onMapTap(map: Map, point: Point) {
            val placemark = map.mapObjects.addPlacemark(point,
                )
            val drawable = ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_baseline_place_24)
            val bitmap = requireNotNull(drawable?.toBitmap())
            placemark.setIcon(ImageProvider.fromBitmap(bitmap))
            placemark.setText("Point")
            placemark.addTapListener(placemarkTapListener)
        }

        override fun onMapLongTap(map: Map, p1: Point) {
            val placemark = map.mapObjects.addPlacemark(p1, )
            val drawable = ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_baseline_place_24)
            val bitmap = requireNotNull(drawable?.toBitmap())
            placemark.setIcon(ImageProvider.fromBitmap(bitmap))
            placemark.addTapListener(placemarkTapListener)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey(MAPKIT_API_KEY)
        MapKitFactory.initialize(this)
        setContentView(R.layout.activity_main)
        mapView = findViewById(R.id.map)
        val map = mapView.mapWindow.map
        map.addInputListener(inputListener)


    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }


    override fun onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop()
    }

}