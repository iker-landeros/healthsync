package mx.tec.healthsyncapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import mx.tec.healthsyncapp.databinding.ActivityTechnicianTicketsSummaryBinding
import mx.tec.healthsyncapp.model.TicketSummary
import org.json.JSONArray


class TechnicianTicketsSummary : AppCompatActivity() {
    private lateinit var binding: ActivityTechnicianTicketsSummaryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTechnicianTicketsSummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Obtenemos la id del usuario que ha iniciado sesion
        val sharedpref = getSharedPreferences("sesion", MODE_PRIVATE)
        val idUser = sharedpref.getString("idUser", "#")

        //Variables para la petición al endpoint del servidor de acuerdo a la id del usuario
        val urlOtherTickets = "http://10.0.2.2:3001/tickets/$idUser/not-my-tickets/summary"
        val urlMyTickets  = "http://10.0.2.2:3001/tickets/$idUser/my-tickets/summary"

        val queue = Volley.newRequestQueue(this)


        //Variables para contenedores de tickets PROPIOS
        val recyclerViewMyTickets: RecyclerView = binding.recyclerMisTickets
        val myTicketList = mutableListOf<TicketSummary>()

        //Variables para contenedores de tickets AJENOS
        val recyclerViewOtherTickets: RecyclerView = binding.recyclerTodosTickets
        val otherTicketList = mutableListOf<TicketSummary>()

        //Adaptamos cada uno de los tickets PROPIOS
        val adapterMyTickets= TicketSummaryAdapter(myTicketList)
        recyclerViewMyTickets.layoutManager = LinearLayoutManager(this) // Establece el layout manager
        recyclerViewMyTickets.adapter = adapterMyTickets  // Asigna el adaptador al RecyclerView


        //Adaptamos cada uno de los tickets AJENOS
        val adapterOtherTickets= TicketSummaryAdapter(otherTicketList)
        recyclerViewOtherTickets.layoutManager = LinearLayoutManager(this) // Establece el layout manager
        recyclerViewOtherTickets.adapter = adapterOtherTickets  // Asigna el adaptador al RecyclerView



        // Petición para tickets PROPIOS
        val listenerMyTickets = Response.Listener<JSONArray> { result ->
            Log.e("Result", result.toString())

            //Recorremos cada objeto y obtenemos la información para construir cada resumen de ticket
            for(i in 0 until result.length()){
                val status = (result.getJSONObject(i).getString("status"))
                val date = (result.getJSONObject(i).getString("dateOpened"))
                val problemType = (result.getJSONObject(i).getString("description"))
                myTicketList.add(TicketSummary(status, date, problemType))
            }
            adapterMyTickets.notifyDataSetChanged()
        }

        val errorMyTickets = Response.ErrorListener { error ->
            Log.e("Error from My Tickets", error.message.toString())
        }

        val userValidationMyTickets = JsonArrayRequest(
            Request.Method.GET, urlMyTickets,
            null, listenerMyTickets, errorMyTickets)

        queue.add(userValidationMyTickets)

        // Petición para tickets AJENOS
        val listenerOtherTickets = Response.Listener<JSONArray> { result ->
            Log.e("Result", result.toString())

            //Recorremos cada objeto y obtenemos la información para construir cada resumen de ticket
            for(i in 0 until result.length()){
                val status = (result.getJSONObject(i).getString("status"))
                val date = (result.getJSONObject(i).getString("dateOpened"))
                val problemType = (result.getJSONObject(i).getString("description"))
                otherTicketList.add(TicketSummary(status, date, problemType))
            }
            adapterOtherTickets.notifyDataSetChanged()
        }

        val errorOtherTickets = Response.ErrorListener { error ->
            Log.e("Error from My Tickets", error.message.toString())
        }

        val userValidationOtherTickets = JsonArrayRequest(
            Request.Method.GET, urlOtherTickets,
            null, listenerOtherTickets, errorOtherTickets)

        queue.add(userValidationOtherTickets)


        // Función del botón de cerrar sesión, limpiando todos los datos de Shared Preferences
        binding.btnLogout.setOnClickListener{
            val sharedpref = getSharedPreferences("sesion", MODE_PRIVATE)
            val editor = sharedpref.edit()
            editor.clear()
            editor.apply()
            //Después de limpiar los datos, enviamos al usuario a la pantalla de inicio de sesión
            val intent = Intent(this@TechnicianTicketsSummary, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}