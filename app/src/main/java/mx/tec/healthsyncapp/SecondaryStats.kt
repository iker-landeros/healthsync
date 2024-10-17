package mx.tec.healthsyncapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import mx.tec.healthsyncapp.adapter.TechnicianStatsAdapter
import mx.tec.healthsyncapp.adapter.TicketSummaryAdapter
import mx.tec.healthsyncapp.databinding.ActivitySecondaryStatsBinding
import mx.tec.healthsyncapp.model.TechnicianStats
import mx.tec.healthsyncapp.model.TicketSummary
import mx.tec.healthsyncapp.utils.SesionUtil
import org.json.JSONArray
import java.math.RoundingMode
import java.text.DecimalFormat

class SecondaryStats : AppCompatActivity() {
    private lateinit var binding: ActivitySecondaryStatsBinding
    private lateinit var sesionUtil: SesionUtil


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondaryStatsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Variables para la petición al endpoint del servidor de acuerdo a la id del usuario
        val subdomain = getString(R.string.subdomain)
        val urlTechnicianStats  = "$subdomain/technicians/statistics"
        sesionUtil = SesionUtil()
        val sharedPref = getSharedPreferences("sesion", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", null) ?: return

        val queue = Volley.newRequestQueue(this)


        //Variables para contenedores de tickets PROPIOS
        val recyclerViewTechnicians: RecyclerView = binding.rvTechnicianStats
        val technicians = mutableListOf<TechnicianStats>()


        //Adaptamos cada uno de los tickets PROPIOS
        val adapterTechnician = TechnicianStatsAdapter(technicians)
        recyclerViewTechnicians.layoutManager = LinearLayoutManager(this) // Establece el layout manager
        recyclerViewTechnicians.adapter = adapterTechnician  // Asigna el adaptador al RecyclerView




        // Petición para tickets PROPIOS
        val listenerTechnicians = Response.Listener<JSONArray> { result ->
            Log.e("Result technicians", result.toString())

            //Recorremos cada objeto y obtenemos la información para construir cada resumen de ticket
            for(i in 0 until result.length()){
                val name = (result.getJSONObject(i).getString("name"))
                val resolvedTickets = (result.getJSONObject(i).getString("resolvedTicketsCount"))
                val avgResolutionTime = (result.getJSONObject(i).getString("avgResolutionTimeHours"))
                val avgResolutionTimeFormated = formatToTwoDecimals(avgResolutionTime)
                technicians.add(TechnicianStats(name, resolvedTickets, avgResolutionTimeFormated))
            }
            adapterTechnician.notifyDataSetChanged()
        }

        val errorMyTickets = Response.ErrorListener { error ->
            val networkResponse = error.networkResponse
            if (networkResponse != null) {
                when (networkResponse.statusCode) {
                    401 -> {
                        Toast.makeText(this, "Error al obtener datos", Toast.LENGTH_SHORT).show()
                        Log.e("Error 401", "Error al obtener datos")
                    }
                    403 -> {
                        Toast.makeText(this, "Token no válido", Toast.LENGTH_SHORT).show()
                        Log.e("Error 403", "Token no válido")
                        sesionUtil.logout(this)
                    }
                    else -> {
                        Toast.makeText(this, "Error desconocido", Toast.LENGTH_SHORT).show()
                        Log.e("Error general", error.message.toString())
                    }
                }
            } else {
                Toast.makeText(this, "Error de red desconocido", Toast.LENGTH_SHORT).show()
                Log.e("Error desconocido", error.message.toString())
            }
        }

        val userValidationMyTickets = object: JsonArrayRequest(
            Request.Method.GET, urlTechnicianStats,
            null, listenerTechnicians, errorMyTickets
        ){
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $token" // Añadir el token en el header
                return headers
            }
        }

        queue.add(userValidationMyTickets)


        // Función del botón de cerrar sesión, limpiando todos los datos de Shared Preferences
        binding.btnBack.setOnClickListener{
            val intent = Intent(this@SecondaryStats, AdminOptions::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun formatToTwoDecimals(value: String): String {
        return try {
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.HALF_UP
            df.format(value.toDouble())
        } catch (e: NumberFormatException) {
            value // Si no es un número válido, retorna el valor original
        }
    }
}