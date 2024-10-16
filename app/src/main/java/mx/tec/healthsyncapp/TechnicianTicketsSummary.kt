package mx.tec.healthsyncapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import mx.tec.healthsyncapp.adapter.TicketSummaryAdapter
import mx.tec.healthsyncapp.databinding.ActivityTechnicianTicketsSummaryBinding
import mx.tec.healthsyncapp.model.TicketSummary
import mx.tec.healthsyncapp.utils.SesionUtil
import org.json.JSONArray
import org.json.JSONObject


class TechnicianTicketsSummary : AppCompatActivity() {
    private lateinit var binding: ActivityTechnicianTicketsSummaryBinding
    private lateinit var sesionUtil: SesionUtil

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTechnicianTicketsSummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sesionUtil = SesionUtil()

        //Obtenemos la id y nombre del usuario que ha iniciado sesion
        val sharedpref = getSharedPreferences("sesion", MODE_PRIVATE)
        val idUser = sharedpref.getString("idUser", "#")
        val name = sharedpref.getString("name", "#")
        val token = sharedpref.getString("token", "#")


        //Mostramos su nombre en pantalla
        binding.txtNombre.text = name

        //Variables para la petición al endpoint del servidor de acuerdo a la id del usuario
        val subdomain = getString(R.string.subdomain)
        val urlMyTickets  = "$subdomain/tickets/$idUser/my-tickets/summary"
        val urlOtherTickets = "$subdomain/tickets/$idUser/not-my-tickets/summary"

        val queue = Volley.newRequestQueue(this)


        //Variables para contenedores de tickets PROPIOS
        val recyclerViewMyTickets: RecyclerView = binding.rvMisTickets
        val myTicketList = mutableListOf<TicketSummary>()

        //Variables para contenedores de tickets AJENOS
        val recyclerViewOtherTickets: RecyclerView = binding.rvTodosTickets
        val otherTicketList = mutableListOf<TicketSummary>()

        //Adaptamos cada uno de los tickets PROPIOS
        val adapterMyTickets= TicketSummaryAdapter(myTicketList) { ticket ->
            //Navegación a pantalla con detalles de ese ticket
            val intent = Intent(this, TechnicianTicketDetails::class.java)
            intent.putExtra("ticketId", ticket.id)
            startActivity(intent)
        }
        recyclerViewMyTickets.layoutManager = LinearLayoutManager(this) // Establece el layout manager
        recyclerViewMyTickets.adapter = adapterMyTickets  // Asigna el adaptador al RecyclerView


        //Adaptamos cada uno de los tickets AJENOS
        val adapterOtherTickets= TicketSummaryAdapter(otherTicketList) { ticket ->
            //Navegación a pantalla con detalles de ese ticket
            val intent = Intent(this, TechnicianTicketDetails::class.java)
            intent.putExtra("ticketId", ticket.id)
            startActivity(intent)
        }
        recyclerViewOtherTickets.layoutManager = LinearLayoutManager(this) // Establece el layout manager
        recyclerViewOtherTickets.adapter = adapterOtherTickets  // Asigna el adaptador al RecyclerView



        // Petición para tickets PROPIOS
        val listenerMyTickets = Response.Listener<JSONArray> { result ->
            Log.e("Result my tickets", result.toString())

            //Recorremos cada objeto y obtenemos la información para construir cada resumen de ticket
            for(i in 0 until result.length()){
                val idTicket = (result.getJSONObject(i).getString("idTicket"))
                val status = (result.getJSONObject(i).getString("status"))
                val dateOpened = (result.getJSONObject(i).getString("dateOpened"))
                val problemName = (result.getJSONObject(i).getString("problemName"))
                myTicketList.add(TicketSummary(idTicket, status, dateOpened, problemName))
            }
            adapterMyTickets.notifyDataSetChanged()
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

        val userValidationMyTickets = object : JsonArrayRequest(
            Request.Method.GET, urlMyTickets,
            null, listenerMyTickets, errorMyTickets
        ) {
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $token" // Añadir el token en el header
                return headers
            }
        }

        queue.add(userValidationMyTickets)

        // Petición para tickets AJENOS
        val listenerOtherTickets = Response.Listener<JSONArray> { result ->
            Log.e("Result other tickets", result.toString())

            //Recorremos cada objeto y obtenemos la información para construir cada resumen de ticket
            for(i in 0 until result.length()){
                val idTicket = (result.getJSONObject(i).getString("idTicket"))
                val status = (result.getJSONObject(i).getString("status"))
                val dateOpened = (result.getJSONObject(i).getString("dateOpened"))
                val problemName = (result.getJSONObject(i).getString("problemName"))
                otherTicketList.add(TicketSummary(idTicket, status, dateOpened, problemName))
            }
            adapterOtherTickets.notifyDataSetChanged()
        }

        val errorOtherTickets = Response.ErrorListener { error ->
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

        val userValidationOtherTickets = object : JsonArrayRequest(
            Request.Method.GET, urlOtherTickets,
            null, listenerOtherTickets, errorOtherTickets
        ) {
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $token" // Añadir el token en el header
                return headers
            }
        }

        queue.add(userValidationOtherTickets)



        // Función del botón de cerrar sesión, limpiando todos los datos de Shared Preferences
        binding.btnLogout.setOnClickListener{
            sesionUtil.logout(this)
        }

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)

        // Set up the swipe refresh layout
        swipeRefreshLayout.setOnRefreshListener {
            // Call recreate to reload the activity
            swipeRefreshLayout.isRefreshing = false
            recreate()
        }
    }
}