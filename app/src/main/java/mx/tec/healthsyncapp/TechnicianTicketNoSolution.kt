package mx.tec.healthsyncapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.button.MaterialButton
import mx.tec.healthsyncapp.databinding.ActivityTechnicianTicketNoSolutionBinding
import mx.tec.healthsyncapp.repository.TicketRepository
import mx.tec.healthsyncapp.viewmodel.TicketViewModel
import java.io.ByteArrayOutputStream
import java.io.File

class TechnicianTicketNoSolution : AppCompatActivity() {
    private lateinit var binding: ActivityTechnicianTicketNoSolutionBinding

    private lateinit var tempPhoto: File
    private var encodedImageData: String? = null // Agrega esta variable para almacenar la imagen codificada
    private lateinit var ticketViewModel: TicketViewModel
    private lateinit var ticketRepository: TicketRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTechnicianTicketNoSolutionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedpref = getSharedPreferences("sesion", MODE_PRIVATE)
        val name = sharedpref.getString("name", "#")
        findViewById<TextView>(R.id.txtNombre).text = name

        val uploadButton = findViewById<Button>(R.id.btnSubirFoto)
        uploadButton.setOnClickListener{
            galleryLauncher.launch("image/*")
        }

        val ticketId = intent.getStringExtra("ticketId") ?: return
        val subdomain = getString(R.string.subdomain)
        val queue = Volley.newRequestQueue(this)
        ticketRepository = TicketRepository(queue)
        ticketViewModel = TicketViewModel()


        val btnNoSolution = findViewById<Button>(R.id.btnNoResuelto)
        btnNoSolution.setOnClickListener {
            if (validateInput()) {
                // Obtén el texto de los EditText correctamente
                val failedReason =
                    findViewById<EditText>(R.id.edtMotivoNoResolucion).text.toString().trim()

                // Si ya tienes el dato de la imagen codificada
                val imageData = encodedImageData.toString()


                // Llama al método del ViewModel para enviar los datos
                ticketViewModel.submitEvidenceNoSolution(
                    ticketId, failedReason, imageData, subdomain, ticketRepository, this
                )

                Thread.sleep(1000)

                // Navega a la siguiente actividad
                val intent =
                    Intent(this@TechnicianTicketNoSolution, TechnicianTicketsSummary::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            } else {
                // Muestra un mensaje de error si la validación falla
                Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
            }
        }

        val btnReturn = findViewById<View>(R.id.btnRegresar)
        btnReturn.setOnClickListener{
            val intent = Intent(this@TechnicianTicketNoSolution, TechnicianTicketDetails::class.java)
            intent.putExtra("ticketId", ticketId)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        val btnHome = findViewById<View>(R.id.btnBack)
        btnHome.setOnClickListener{
            val intent =
                Intent(this@TechnicianTicketNoSolution, TechnicianTicketDetails::class.java)
            intent.putExtra("ticketId", ticketId)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
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

    // Función para validar las entradas
    private fun validateInput(): Boolean {
        val failedReason = findViewById<EditText>(R.id.edtMotivoNoResolucion).text.toString().trim()

        // Verificar que el campo no esté vacío y que la imagen esté codificada
        return failedReason.isNotEmpty() && encodedImageData != null
    }

}