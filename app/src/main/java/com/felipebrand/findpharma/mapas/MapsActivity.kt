package com.felipebrand.findpharma.mapas


import android.Manifest
import android.content.IntentSender
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.location.Location.distanceBetween
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.View.INVISIBLE
import android.widget.*
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.felipebrand.findpharma.R
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class MapsActivity : AppCompatActivity(),OnMapReadyCallback {

    //public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    var client: FusedLocationProviderClient? = null
    private lateinit var mMap: GoogleMap
    lateinit var databaseRef: DatabaseReference
    private lateinit var marker: Marker
     var circle: Circle? = null

    private lateinit var home: LatLng
    private lateinit var buttonSearch: Button
    private var farmas = arrayListOf<Farmacia>()
    private lateinit var seekBar: SeekBar
    private lateinit var campo_pesquisa: EditText
    private lateinit var textSize: TextView
    private var fristTime: Boolean = true
    var countAux = 0
    private var markerLoja = mutableListOf<Marker>()
    private var buscador = mutableListOf<Buscador>()
    var positionLoja = 0
    var distanceLoja = mutableListOf<Int>()
    var geofenceSize = 200.0
    var zoom = 15.0f
    lateinit var farmaAux: Farmacia
    lateinit var produtoAux: Produto
    lateinit var markerLocation: LatLng
    private lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Código de permissão

        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        ActivityCompat.requestPermissions(this, permissions, 0)

        //instacio o fragmento do mapa

        setContentView(R.layout.activity_maps)
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        seekBar = findViewById(R.id.seekBar)
        textSize = findViewById(R.id.textSize)
//        buttonSearch = findViewById(R.id.button_search)
        campo_pesquisa = findViewById(R.id.campo_pesquisa)


        //  Iniciar banco de Dados
        databaseRef = Firebase.database.reference.child("pharma")



        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (objSnapshot in snapshot.children) {

                    farmaAux = Farmacia()

                    farmaAux.setNome(objSnapshot.child("nome").value as String)
                    farmaAux.setLat(objSnapshot.child("latitude").value as Double)
                    farmaAux.setLong(objSnapshot.child("longitude").value as Double)
                    farmaAux.setEnd(objSnapshot.child("endereco").value as String)
                    farmaAux.setId(objSnapshot.value.toString())

                    for (postSnapshot in objSnapshot.child("produtos").children) {
                        produtoAux = Produto()
                        // produtoAux.setPop(postSnapshot.child("nome_popular").value as String)
                        produtoAux.setCarac(postSnapshot.child("carac").value as String)
                        produtoAux.setVia(postSnapshot.child("via").value as String)
                        produtoAux.setOriginal(postSnapshot.child("gen_ori").value as String)
                        produtoAux.setValor(postSnapshot.child("valor").value as Double)

                    }
                    farmas.add(farmaAux)
                }
                countAux = snapshot.childrenCount.toInt()
                distanceLoja = mutableListOf(countAux)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    applicationContext,
                    "Não foi possivel ler os dados",
                    Toast.LENGTH_LONG
                ).show()
            }
        })



        fun procurarLojas(item: MenuItem): Boolean {

            var results: List<Float> = listOf()
            lateinit var auxBuscador: Buscador
            if (markerLoja.isNotEmpty()) {
                for (i in 0 until markerLoja.size)
                    markerLoja[i].remove()

                markerLoja.clear()
            }
            if (buscador.isNotEmpty())
                buscador.clear()
            for (i in 0 until countAux) {
                distanceBetween(
                    markerLocation.latitude,
                    markerLocation.longitude,
                    farmas[i].getLat(),
                    farmas[i].getLong(),
                    results as FloatArray
                )
                distanceLoja[i] = results[0].toInt()
                if (results[0] <= geofenceSize) {
                    if (item.itemId == R.id.itemProcurar) {
                        markerLoja.add(
                            mMap.addMarker(
                                MarkerOptions().position(
                                    LatLng(
                                        farmas[i].getLat(),
                                        farmas[i].getLong()
                                    )
                                )
                                    .title(farmas[i].getNome())
                            )!!
                        )
                    }
                    auxBuscador = Buscador()
                    auxBuscador.setDistancia_loja(distanceLoja[i])
                    auxBuscador.setNome_loja(farmas[i].getNome().toString())
                    auxBuscador.setNome_loja(i.toString())
                    buscador.add(auxBuscador)
                }
            }
            if (buscador.isEmpty() && item.itemId != R.id.itemProdutos) {
                Toast.makeText(this, "Nenhuma loja encontrada", Toast.LENGTH_SHORT).show()
                return false
            } else
                return true
        }

        //TODO criar classe buttonpress, CRIAR RECEPTOR NO CAMPO-_PESQUISA


    }

    //TODO IMPLEMENTAR O BUSCADOR, BUTTON BUSCA, BUSCA NO BANCO PALAVRA


    override fun onResume() {
        super.onResume()
        //Condição exigida para ser possível utilizar o metodo getLastLocation

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        client?.lastLocation?.addOnSuccessListener(OnSuccessListener {
            fun onSuccess(location: Location) {
                Log.i("Location", "Location Vazio")
                val my_loc = LatLng(location.latitude, location.longitude)
                home = my_loc

               if (fristTime) {
                    marker =
                        mMap.addMarker(
                            MarkerOptions().position(my_loc).title("Minha Localização")
                        )!!
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(my_loc, zoom))
                    this.circle = mMap.addCircle(
                        CircleOptions().center(my_loc).fillColor(Color.argb(20, 0, 255, 255))
                            .strokeWidth(
                                8F
                            ).radius(geofenceSize).visible(true)
                    )
                    markerLocation = home
                    fristTime = false
                }


            }
        })?.addOnFailureListener(OnFailureListener() {
            fun OnFailure(e: Exception) {

            }
        })


        val loc_req: LocationRequest = LocationRequest.create()
        loc_req.interval = 15 * 1000  //Intervalo de busca de atualização em milisegundos
        loc_req.fastestInterval =
            5 * 1000 // Intervalo de busca em caso de localizações vindas de outros apps
        loc_req.priority =
            LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY // Economia maior de bateria com precisão de 100 m

        //Verifica se as localizações recebidas estão corretas
        val builder: LocationSettingsRequest.Builder =
            LocationSettingsRequest.Builder().addLocationRequest(loc_req)

        val setting_cli: SettingsClient = LocationServices.getSettingsClient(this)
       setting_cli.checkLocationSettings(builder.build()).addOnSuccessListener(OnSuccessListener() {
            fun onSuccess(locationSettingsRequest: LocationSettingsRequest) {
           }
       }).addOnFailureListener(OnFailureListener(){
            fun onFailure(@NonNull e:Exception){
                if(e is ResolvableApiException) try {
                   val resolvable: ResolvableApiException = e as ResolvableApiException
                        resolvable.startResolutionForResult(this,10)


                }catch (sendIntentException: IntentSender.SendIntentException){

                }
            }

        })

        locationCallback = object:LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
//                if (locationResult is null) {
//
//                    Log.i("LocationResult", "locationResult vazio")
//                    return
//                }
                for (location: Location in locationResult.locations) {
                    if (!Geocoder.isPresent()) {
                        return
                    }
                }
            }
        }
        client?.requestLocationUpdates(loc_req,locationCallback, Looper.myLooper()!!)

}
        override fun onMapReady(googleMap: GoogleMap) {
            try {
                val success: Boolean =
                    googleMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                            this,
                            R.raw.style_json
                        )
                    )
                if (!success) {
                    Log.e("Estilo", "Style parsing failed.")
                }

            } catch (e: Resources.NotFoundException) {
                Log.e("Estilo", "Can't find style. Error", e)
            }
            mMap = googleMap

            mMap.setMinZoomPreference(2.0f)
            mMap.setMaxZoomPreference(20.0f)

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            mMap.isMyLocationEnabled
            mMap.uiSettings.isMyLocationButtonEnabled
            mMap.uiSettings.isZoomControlsEnabled

            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                var minRadius: Int = 0
                var zoomOut: Float = 0.0f
              //  val m = " m"

                override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                    minRadius = i + 200
                    val aux = i/1500
                    zoomOut = zoom - aux.toFloat() //(i / 1500).toFloat()
                    circle?.radius = minRadius.toDouble()
                    if (zoomOut > 12)
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(zoomOut))
                    geofenceSize = minRadius.toDouble()
                    //textSize.setText(minRadius.toInt()) // USAR O "M"

                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {
                }
            })
            mMap.setOnMapLongClickListener (GoogleMap.OnMapLongClickListener(){

                fun onMapLongClick(latLng: LatLng) {
                    geofenceMarker(latLng)
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
                    markerLocation = latLng
                }
            })

            mMap.setOnMyLocationButtonClickListener {
                geofenceMarker(home)
                markerLocation = home
                return@setOnMyLocationButtonClickListener false

            }
            mMap.setOnMyLocationButtonClickListener {
                geofenceMarker(home)
                markerLocation = home
                false
            }

            mMap.setOnMarkerClickListener { markerClick ->
                if (markerClick.title != marker.title) {
                    buttonSearch.visibility = android.view.View.VISIBLE
                } else {
                    buttonSearch.visibility = INVISIBLE
                }
                for (i in 0 until countAux) {
                    if (markerClick.title == farmas[i].getNome()) {
                        positionLoja = i
                    }
                }
                false
            }

//        buttonSearch.setOnClickListener(android.view.View.OnClickListener {
//
//            fun onClick(view: android.view.View) {
//                val intent = Intent(applicationContext, ProductsActivity::class.java)
//                // intent.putExtra("loja",farmas.get(positionLoja))
//                intent.putExtra("distancia", distanceLoja[positionLoja])
//                intent.putExtra("id", R.id.itemProcurar)
//                startActivity(intent)
//            }
//        })
        }

        fun geofenceMarker(latLng: LatLng) {
            circle?.remove()
            marker.remove()
            if (latLng == home) {
                marker =
                    mMap.addMarker(MarkerOptions().position(latLng).title("Minha Localização"))!!
            } else
                marker = mMap.addMarker(
                    MarkerOptions().position(latLng).title("Marcador Personalizado")
                )!!
            circle = mMap.addCircle(
                CircleOptions().center(latLng)
                    .fillColor(Color.argb(20, 0, 255, 255))
                    .strokeWidth(8F).radius(geofenceSize).visible(true)
            )
        }


} // final










private operator fun Int.iterator() {

}

private operator fun Any.iterator() {
}

