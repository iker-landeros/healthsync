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
import mx.tec.healthsyncapp.adapter.TechnicianStatsAdapter
import mx.tec.healthsyncapp.adapter.TicketSummaryAdapter
import mx.tec.healthsyncapp.databinding.ActivitySecondaryStatsBinding
import mx.tec.healthsyncapp.model.TechnicianStats
import mx.tec.healthsyncapp.model.TicketSummary
import org.json.JSONArray

class SecondaryStats : AppCompatActivity() {
    private lateinit var binding: ActivitySecondaryStatsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondaryStatsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Variables para la petición al endpoint del servidor de acuerdo a la id del usuario
        val urlMyTickets  = "http://10.0.2.2:3001/technicians/statistics"

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
                technicians.add(TechnicianStats(name, resolvedTickets, avgResolutionTime))
            }
            adapterTechnician.notifyDataSetChanged()
        }

        val errorMyTickets = Response.ErrorListener { error ->
            Log.e("Error from Technician", error.message.toString())
        }

        val userValidationMyTickets = JsonArrayRequest(
            Request.Method.GET, urlMyTickets,
            null, listenerTechnicians, errorMyTickets)

        queue.add(userValidationMyTickets)


        // Función del botón de cerrar sesión, limpiando todos los datos de Shared Preferences
        binding.btnLogout.setOnClickListener{
            val intent = Intent(this@SecondaryStats, PrimaryStats::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}