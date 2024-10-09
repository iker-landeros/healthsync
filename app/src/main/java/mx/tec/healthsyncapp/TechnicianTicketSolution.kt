package mx.tec.healthsyncapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import mx.tec.healthsyncapp.databinding.ActivityTechnicianTicketSolutionBinding
import java.io.ByteArrayOutputStream
import java.io.File
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.button.MaterialButton
import mx.tec.healthsyncapp.repository.TicketRepository
import mx.tec.healthsyncapp.viewmodel.TicketViewModel



class TechnicianTicketSolution : AppCompatActivity() {
    private  lateinit var  binding: ActivityTechnicianTicketSolutionBinding
    private lateinit var tempPhoto: File
    private var encodedImageData: String? = null // Agrega esta variable para almacenar la imagen codificada
    private lateinit var ticketViewModel: TicketViewModel
    private lateinit var ticketRepository: TicketRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTechnicianTicketSolutionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val uploadButton = findViewById<MaterialButton>(R.id.btnSubirFoto)
        uploadButton.setOnClickListener{
            galleryLauncher.launch("image/*")
        }

        val ticketId = intent.getStringExtra("ticketId") ?: return
        val subdomain = getString(R.string.subdomain2)
        val queue = Volley.newRequestQueue(this)
        ticketRepository = TicketRepository(queue)
        ticketViewModel = TicketViewModel()

        val btnFinish = findViewById<Button>(R.id.btnFinalizarTicket)
        btnFinish.setOnClickListener {
            // Obtén el texto de los EditText correctamente
            val revisionProcess = findViewById<EditText>(R.id.edtProcesoRevision).text.toString().trim()
            val diagnosis = findViewById<EditText>(R.id.edtDiagnostico).text.toString().trim()
            val solutionProcess = findViewById<EditText>(R.id.edtProcesoResolucion).text.toString().trim()

            // Si ya tienes el dato de la imagen codificada
            val imageData = encodedImageData.toString()

            // Valor de componentes (puedes ajustarlo según tus necesidades)
            val components = ""

            // Llama al método del ViewModel para enviar los datos
            ticketViewModel.submitEvidenceSolution(
                ticketId, revisionProcess, diagnosis, solutionProcess, imageData, components, subdomain, ticketRepository
            )

            // Navega a la siguiente actividad
            val intent = Intent(this@TechnicianTicketSolution, TechnicianTicketsSummary::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }


        val btnGoBack = findViewById<ImageButton>(R.id.btnLogout)
        btnGoBack.setOnClickListener{
            val intent = Intent(this@TechnicianTicketSolution, TechnicianTicketsSummary::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

    }

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
                            val imgUploaded = findViewById<ImageView>(R.id.imgUploaded)
                            imgUploaded.setImageBitmap(resource)
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
