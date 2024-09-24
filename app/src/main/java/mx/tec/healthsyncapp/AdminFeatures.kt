package mx.tec.healthsyncapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import org.json.JSONArray

class AdminFeatures : AppCompatActivity() {
    private lateinit var binding: ActivityAdminFeaturesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminFeaturesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Obtenemos la id del usuario que ha iniciado sesion
        val sharedpref = getSharedPreferences("sesion", MODE_PRIVATE)
        val idUser = sharedpref.getString("idUser", "#")

        //Variables para la petición al endpoint del servidor de acuerdo a la id del usuario
        val urlAllTickets = "http://10.0.2.2:3001/tickets"

        val queue = Volley.newRequestQueue(this)

        val recyclerViewAllTickets: RecyclerView = binding.recyclerTodosTickets

        val allTicketList = mutableListOf<TicketSummary>()

        val adapterAllTickets= TicketSummaryAdapter(allTicketList)
        recyclerViewAllTickets.layoutManager = LinearLayoutManager(this) // Establece el layout manager
        recyclerViewAllTickets.adapter = adapterAllTickets  // Asigna el adaptador al RecyclerView

        // Petición para tickets PROPIOS
        val listenerMyTickets = Response.Listener<JSONArray> { result ->
            Log.e("Result", result.toString())

            //Recorremos cada objeto y obtenemos la información para construir cada resumen de ticket
            for(i in 0 until result.length()){
                val status = (result.getJSONObject(i).getString("status"))
                val date = (result.getJSONObject(i).getString("dateOpened"))
                val problemType = (result.getJSONObject(i).getString("description"))
                allTicketList.add(TicketSummary(status, date, problemType))
            }
            adapterAllTickets.notifyDataSetChanged()
        }

        val errorMyTickets = Response.ErrorListener { error ->
            Log.e("Error from My Tickets", error.message.toString())
        }

        val userValidationMyTickets = JsonArrayRequest(
            Request.Method.GET, urlAllTickets,
            null, listenerMyTickets, errorMyTickets)

        queue.add(userValidationMyTickets)


        binding.btnHome.setOnClickListener{
            val intent = Intent(this@AdminFeatures, AdminOptions::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

    }
}