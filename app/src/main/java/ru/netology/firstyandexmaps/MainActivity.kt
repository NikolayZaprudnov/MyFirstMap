package ru.netology.firstyandexmaps

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider


class MainActivity : AppCompatActivity() {
    private val MAPKIT_API_KEY = "a1350e43-6644-467b-a3a2-88591bdd73fc" //Вставить свой ключ
    private lateinit var mapView: MapView
    private var placemarkList: ArrayList<UserPoint> = ArrayList()
    private lateinit var listPoint: Button
    lateinit var arrayAdapter: ArrayAdapter<UserPoint>

//    private  var  icon = ImageProvider.fromResource(applicationContext, R.drawable.ic_baseline_place_24)

    private val placemarkTapListener = MapObjectTapListener { mapObject, point ->
        val placemark = mapObject as PlacemarkMapObject
        val userPoint = placemark.userData as UserPoint
        Toast.makeText(
            this@MainActivity,
            "Tapped the point (${point.longitude}, ${point.latitude})", Toast.LENGTH_SHORT).show()
        val dialog = AlertDialog.Builder(this)
            .setTitle("Точка")
            .setPositiveButton("Редактировать") { _, _ ->
                showEditDescriptionDialog(placemark, userPoint)
            }
            .setNegativeButton("Удалить") { _, _ ->
                mapView.mapWindow.map.mapObjects.remove(placemark)
                placemarkList.remove(userPoint)
            }
            .create()

        dialog.show()
        true

    }

    private val inputListener = object : InputListener {
        override fun onMapTap(map: Map, point: Point) {
            val placemark = map.mapObjects.addPlacemark(
                point,
            )
            val drawable =
                ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_baseline_place_24)
            val bitmap = requireNotNull(drawable?.toBitmap())
            val userPoint = UserPoint(placemarkList.size,
                point.latitude,
                point.longitude,
                "Point" + placemarkList.size.toString())
            placemark.setIcon(ImageProvider.fromBitmap(bitmap))
            placemark.setText("Point" + placemarkList.size.toString())
            placemark.addTapListener(placemarkTapListener)
            placemark.userData = userPoint
            placemarkList.add(userPoint)
        }

        override fun onMapLongTap(map: Map, p1: Point) {
            val placemark = map.mapObjects.addPlacemark(p1)
            val drawable =
                ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_baseline_place_24)
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
        val listPointView = findViewById<ListView>(R.id.ListPoints)
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, placemarkList )
        listPointView.adapter = arrayAdapter
        listPoint.findViewById<Button>(R.id.listPoint)
        listPoint.setOnClickListener {
            listPointView.visibility = View.VISIBLE
        }



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

    private fun showEditDescriptionDialog(placemark: PlacemarkMapObject, userPoint: UserPoint) {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_edit_description, null)
        val editTextDescription = view.findViewById<EditText>(R.id.editTextDescription)
        editTextDescription.setText(userPoint.description)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Редактировать описание")
            .setView(view)
            .setPositiveButton("Сохранить") { _, _ ->
                val newDescription = editTextDescription.text.toString()
                userPoint.description = newDescription
                placemark.setText(newDescription)
            }
            .setNegativeButton("Отмена", null)
            .create()

        dialog.show()
    }
    private fun goToPoint(userPoint: UserPoint) {
        val targetPoint = Point(userPoint.latitude, userPoint.longitude)
//        mapView.map.move(
////            CameraPosition(targetPoint, 15.0f),
////            Animation(Animation.Type.SMOOTH, 0f),
////            null
//        )
    }
}

