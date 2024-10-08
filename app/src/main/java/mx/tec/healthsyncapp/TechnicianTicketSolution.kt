package mx.tec.healthsyncapp

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import mx.tec.healthsyncapp.databinding.ActivityTechnicianTicketSolutionBinding
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import com.bumptech.glide.request.transition.Transition


class TechnicianTicketSolution : AppCompatActivity() {
    private  lateinit var  binding: ActivityTechnicianTicketSolutionBinding
    private lateinit var tempPhoto: File
    private var encodedImageData: String? = null // Agrega esta variable para almacenar la imagen codificada

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTechnicianTicketSolutionBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val uploadButton = findViewById<Button>(R.id.btnReclamarTicket)
        uploadButton.setOnClickListener{
            galleryLauncher.launch("image/*")
        }

        val btnFinish = findViewById<ImageButton>(R.id.btnFinish)
        btnFinish.setOnClickListener{
            //   revisionProcess, diagnosis, solutionProcess, imageData, components
            val revisionProcess = findViewById<EditText>(R.id.edtProcesoRevision)
            val diagnosis = findViewById<EditText>(R.id.edtDiagnostico)
            val solutionProcess = findViewById<EditText>(R.id.edtProcesoResolucion)
            val imageData = encodedImageData.toString()
            Log.e("ENCODED IMAGE", encodedImageData.toString())

            val body = JSONObject() //Creamos y agregamos datos al cuerpo para la petición de validación
            body.put("revisionProcess", revisionProcess)
            body.put("diagnosis", diagnosis)
            body.put("solutionProcess", solutionProcess)
            body.put("components", null)
            body.put("imageData", imageData)



            val ticketId = intent.getStringExtra("ticketId")
            Log.e("ID TICKET: ", ticketId.toString())
            val urlPostSolution = "http://10.0.2.2:3001/tickets/$ticketId/solved"
            val queue = Volley.newRequestQueue(this)


            val listener = Response.Listener<JSONObject> { result ->
                Log.e("Result", result.toString())

            }
            val error = Response.ErrorListener { error ->
                Log.e("Error", error.message.toString())
            }

            val userValidation = JsonObjectRequest(
                Request.Method.POST, urlPostSolution,
                body, listener, error)

            queue.add(userValidation)

        }
    }
    // Launcher para abrir la galería y seleccionar una imagen
    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            // Verifica si el resultado no es nulo y contiene una URI válida
            result?.let { uri ->
                // Usa Glide para cargar la imagen seleccionada desde la URI, transformándola en un Bitmap
                Glide.with(this)
                    .asBitmap() // Indica que queremos un Bitmap, no la imagen completa
                    .load(uri) // Carga la imagen desde la URI seleccionada
                    .override(600, 600) // Redimensiona la imagen a 600x600 píxeles
                    .into(object : CustomTarget<Bitmap>() { // Define un destino personalizado para la imagen cargada
                        // Cuando la imagen está lista, este método es llamado
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            // Crea un flujo de salida de bytes para almacenar la imagen comprimida
                            val byteArrayOutputStream = ByteArrayOutputStream()
                            // Comprime el Bitmap en formato JPEG con una calidad del 80%
                            resource.compress(
                                Bitmap.CompressFormat.JPEG,
                                80,
                                byteArrayOutputStream
                            )
                            // Convierte el flujo de bytes a un array de bytes
                            val byteArray = byteArrayOutputStream.toByteArray()
                            // Codifica el array de bytes en Base64 y lo almacena en 'encodedImageData'
                            encodedImageData = Base64.encodeToString(byteArray, Base64.NO_WRAP)
                        }

                        // Método requerido para limpiar recursos cuando el objetivo ya no es necesario
                        override fun onLoadCleared(placeholder: Drawable?) {
                        }
                    })
            }
        }

    // Función que recibe un Bitmap y lo codifica en Base64
    fun encodeImageToBase64(bitmap: Bitmap): String {
        // Crea un flujo de salida de bytes para almacenar la imagen comprimida
        val byteArrayOutputStream = ByteArrayOutputStream()
        // Comprime el Bitmap en formato JPEG con una calidad del 100%
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        // Convierte el flujo de bytes a un array de bytes
        val byteArray = byteArrayOutputStream.toByteArray()
        // Devuelve el array de bytes codificado en Base64
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }


}
