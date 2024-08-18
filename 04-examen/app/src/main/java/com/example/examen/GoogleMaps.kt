package com.example.examen

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar

class GoogleMaps : AppCompatActivity() {
    private lateinit var mapa: GoogleMap
    var permisos = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_maps)
        solicitarPermisos()
        configurarBotonSucursal()
        iniciarLogicaMapa()
    }
    private fun configurarBotonSucursal() {
        findViewById<Button>(R.id.btn_ir_sucursal).setOnClickListener {
            val sucursal = LatLng(-0.16558673054407697, -78.32279228229712)
            val zoom = 17f
            moverCamaraConZoom(sucursal, zoom)
        }
    }

    fun solicitarPermisos(){
        val contexto = this.applicationContext
        val nombrePermisoFine = android.Manifest.permission.ACCESS_FINE_LOCATION
        val nombrePermisoCoarse = android.Manifest.permission.ACCESS_COARSE_LOCATION
        val permisoFine = ContextCompat.checkSelfPermission(contexto, nombrePermisoFine)
        val permisoCoarse = ContextCompat.checkSelfPermission(contexto, nombrePermisoCoarse)
        val tienePermisos = permisoFine == PackageManager.PERMISSION_GRANTED &&
                permisoCoarse == PackageManager.PERMISSION_GRANTED
        if(tienePermisos){
            permisos = true
        }else{
            ActivityCompat.requestPermissions(
                this, arrayOf(nombrePermisoFine,nombrePermisoCoarse), 1
            )
        }
    }
    fun iniciarLogicaMapa() {
        val fragmentoMapa = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        fragmentoMapa.getMapAsync { googleMap ->
            with(googleMap) {
                mapa = googleMap
                establecerConfiguracionMapa()
                mover()

                // Obtener productos de la base de datos
                val productos = obtenerProductos() // Debes implementar esta función para obtener los productos
                agregarMarcadoresDeProductos(productos)
            }
        }
    }
    private fun obtenerProductos(): List<EntrenadorProducto> {
        val sqliteHelperProducto = SqliteHelperProducto(this)
        return sqliteHelperProducto.obtenerTodosLosProductos()
    }
    fun agregarMarcadoresDeProductos(productos: List<EntrenadorProducto>) {
        val zoom = 17f
        productos.forEach { producto ->
            val coordenadas = generarCoordenadasAleatorias()
            val marcador = anadirMarcador(coordenadas, producto.nombre)
            marcador.tag = producto.nombre
            moverCamaraConZoom(coordenadas, zoom)
        }
    }
    fun generarCoordenadasAleatorias(): LatLng {
        val latitudMin = -0.1662304579963688
        val latitudMax = -0.16266849916610404
        val longitudMin = -78.32186960234705
        val longitudMax = -78.31393026324182

        val latitudAleatoria = latitudMin + Math.random() * (latitudMax - latitudMin)
        val longitudAleatoria = longitudMin + Math.random() * (longitudMax - longitudMin)

        return LatLng(latitudAleatoria, longitudAleatoria)
    }
    fun mover() {
        val zoom = 17f
        val sucursal = LatLng(
            -0.1652219516451772, -78.3187153243782
        )
        val titulo = "Sucursal "
        val markQuicentro = anadirMarcador(sucursal, titulo)
        markQuicentro.tag = titulo
        moverCamaraConZoom(sucursal, zoom)
    }
    fun establecerConfiguracionMapa() {
        val contexto = this.applicationContext
        with(mapa) {
            val nombrePermisoFine = android.Manifest.permission.ACCESS_FINE_LOCATION
            val nombrePermisoCoarse = android.Manifest.permission.ACCESS_COARSE_LOCATION
            val permisoFine = androidx.core.content.ContextCompat.checkSelfPermission(contexto, nombrePermisoFine)
            val permisoCoarse = androidx.core.content.ContextCompat.checkSelfPermission(contexto, nombrePermisoCoarse)
            val tienePermisos = permisoFine == android.content.pm.PackageManager.PERMISSION_GRANTED &&
                    permisoCoarse == android.content.pm.PackageManager.PERMISSION_GRANTED
            if (tienePermisos) {
                mapa.isMyLocationEnabled = true
                uiSettings.isMyLocationButtonEnabled = true
            }
            uiSettings.isZoomControlsEnabled = true
        }
    }
    fun moverCamaraConZoom(latLang: LatLng, zoom: Float = 10f){
        mapa.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                latLang, zoom
            ))
    }
    fun anadirMarcador(latLang: LatLng, title: String): Marker {
        return mapa.addMarker(MarkerOptions().position(latLang).title(title))!!
    }
    fun mostrarSnackbar(texto:String){
        val snack = Snackbar.make(
            findViewById(R.id.cl_google_maps),
            texto,
            Snackbar.LENGTH_INDEFINITE
        )
        snack.show()
    }
}