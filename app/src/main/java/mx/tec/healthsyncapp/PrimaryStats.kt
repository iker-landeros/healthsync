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
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import mx.tec.healthsyncapp.databinding.ActivityPrimaryStatsBinding
import mx.tec.healthsyncapp.model.TicketSummary
import mx.tec.healthsyncapp.utils.SesionUtil
import org.json.JSONArray

class PrimaryStats : AppCompatActivity() {
    private lateinit var binding: ActivityPrimaryStatsBinding
    private lateinit var sesionUtil: SesionUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrimaryStatsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val subdomain = getString(R.string.subdomain)
        val urlPrimaryStats = "$subdomain/tickets/statistics"
        val queue = Volley.newRequestQueue(this)

        sesionUtil = SesionUtil()
        val sharedPref = getSharedPreferences("sesion", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", null) ?: return

        val listener = Response.Listener<JSONArray> { result ->
            if (result.length() > 0) {
                val statistics = result.getJSONObject(0)  // Obtenemos el primer ticket del array
                Log.e("Result my tickets", statistics.toString())

                with(binding) {
                    txtNumTktsTotales.text = statistics.getString("totalTickets")
                    txtNumTktsNoEmpezados.text = statistics.getString("unstartedTickets")
                    txtNumTktsEnProgreso.text = statistics.getString("progressingTickets")
                    txtNumTktsNoRes.text = statistics.getString("notSolvedTickets")
                    txtNumTktsRes.text = statistics.getString("solvedTickets")
                    txtNumTktsEliminados.text = statistics.getString("eliminatedTickets")
                }
            }
        }

        val error = Response.ErrorListener { error ->
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
            Request.Method.GET, urlPrimaryStats,
            null, listener, error
        ){
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $token" // Añadir el token en el header
                return headers
            }
        }

        queue.add(userValidationMyTickets)

        binding.btnMasStats.setOnClickListener{
            val intent = Intent(this@PrimaryStats, SecondaryStats::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener{
            val intent = Intent(this@PrimaryStats, AdminOptions::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}