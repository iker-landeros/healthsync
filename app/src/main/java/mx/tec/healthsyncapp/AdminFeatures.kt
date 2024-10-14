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
import mx.tec.healthsyncapp.adapter.TicketSummaryAdapter
import mx.tec.healthsyncapp.databinding.ActivityAdminFeaturesBinding
import mx.tec.healthsyncapp.model.TicketSummary
import mx.tec.healthsyncapp.utils.SesionUtil
import org.json.JSONArray

class AdminFeatures : AppCompatActivity() {
    private lateinit var binding: ActivityAdminFeaturesBinding
    private lateinit var sesionUtil: SesionUtil


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminFeaturesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Obtenemos la id del usuario que ha iniciado sesion
        sesionUtil = SesionUtil()
        val sharedPref = getSharedPreferences("sesion", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", null) ?: return


        //Variables para la petición al endpoint del servidor de acuerdo a la id del usuario
        val subdomain = getString(R.string.subdomain)
        val urlAllTickets = "$subdomain/tickets"

        val queue = Volley.newRequestQueue(this)

        val recyclerViewAllTickets: RecyclerView = binding.rvTodosTickets

        val allTicketList = mutableListOf<TicketSummary>()

        val adapterAllTickets= TicketSummaryAdapter(allTicketList) {ticket ->
            val intent = Intent(this, AdminTicketDetails::class.java)
            intent.putExtra("ticketId", ticket.id)
            startActivity(intent)
        }
        recyclerViewAllTickets.layoutManager = LinearLayoutManager(this) // Establece el layout manager
        recyclerViewAllTickets.adapter = adapterAllTickets  // Asigna el adaptador al RecyclerView

        // Petición para tickets PROPIOS
        val listenerMyTickets = Response.Listener<JSONArray> { result ->
            Log.e("Result", result.toString())

            //Recorremos cada objeto y obtenemos la información para construir cada resumen de ticket
            for(i in 0 until result.length()){
                val idTicket = (result.getJSONObject(i).getString("idTicket"))
                val status = (result.getJSONObject(i).getString("status"))
                val date = (result.getJSONObject(i).getString("dateOpened"))
                val problemType = (result.getJSONObject(i).getString("problemName"))
                allTicketList.add(TicketSummary(idTicket, status, date, problemType))
            }
            adapterAllTickets.notifyDataSetChanged()
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
            Request.Method.GET, urlAllTickets,
            null, listenerMyTickets, errorMyTickets
        ){
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $token" // Añadir el token en el header
                return headers
            }
        }

        queue.add(userValidationMyTickets)


        binding.btnHome.setOnClickListener{
            val intent = Intent(this@AdminFeatures, AdminOptions::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

    }
}