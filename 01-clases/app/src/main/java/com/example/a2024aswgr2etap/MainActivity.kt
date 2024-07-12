package com.example.a2024aswgr2etap

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Telephony
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val botonCicloVida = findViewById<Button>(
            R.id.btn_ciclo_vida
        )
        botonCicloVida
            .setOnClickListener { irActividad(ACicloVida::class.java) }

        val botonIrListView = findViewById<Button>(
            R.id.btn_ir_list_view
        )
        botonIrListView
            .setOnClickListener {
                irActividad(BListView::class.java)
            }
        val botonIntentImplicito = findViewById<Button>(
            R.id.btn_ir_intent_implicito
        )
        botonIntentImplicito
            .setOnClickListener {
                val intentConRespuesta = Intent(
                    Intent.ACTION_PICK,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                )
                callbackContenidoIntentImplicito.launch(intentConRespuesta)
            }
        val botonIntentExplicito = findViewById<Button>(
            R.id.btn_ir_intent_explicito
        )
        botonIntentExplicito
            .setOnClickListener {
                val intentExplicito = Intent(
                    this,
                    CIntentExplicitoParametros::class.java
                )
                intentExplicito.putExtra("nombre", "Erika")
                intentExplicito.putExtra("apellido", "Anrrango")
                intentExplicito.putExtra("edad", 23)
                intentExplicito.putExtra(
                    "entrenador",
                    BEntrenador(10, "Erika", "Anrrango")
                )
                callbackContenidoIntentExplicito.launch(intentExplicito)
            }

        // Inicializar Base de Datos
        EBaseDeDatos. tablaEntrenador = ESqliteHelperEntrenador(
            this
        )
        val botonSqlite = findViewById<Button>(R.id.btn_sqlite)
        botonSqlite. setOnClickListener {
            irActividad(ECrudEntrenador::class.java)
        }
    }

    fun irActividad(
        clase: Class<*>
    ) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }

    fun mostrarSnackbar(texto:String){
        val snack = Snackbar.make(
            findViewById(R.id.id_layout_main),
            texto,
            Snackbar.LENGTH_INDEFINITE
        )
        snack.show()
    }
    val callbackContenidoIntentExplicito =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            result ->
            if(result.resultCode == Activity.RESULT_OK){
                if(result.data != null){
                    //logica de negocio
                    val data = result.data
                    mostrarSnackbar(
                        "${data?.getStringExtra("nombreModificado")}"
                    )
                }
            }
        }

    val callbackContenidoIntentImplicito =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
                result ->
            if(result.resultCode == Activity.RESULT_OK){
                if(result.data != null){
                    //logica de negocio
                    if(result.data!!.data != null){

                        val uri: Uri = result.data!!.data!!
                        val cursor = contentResolver.query(
                            uri, null, null, null, null, null
                        )
                        cursor?.moveToFirst()
                        val indiceTelefono = cursor?.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER
                        )
                        val telefono = cursor?.getString(indiceTelefono!!)
                        cursor?.close()
                        mostrarSnackbar("Telefono $telefono")
                    }
                }
            }
        }
}