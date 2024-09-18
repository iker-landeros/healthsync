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
import mx.tec.healthsyncapp.databinding.ActivityTechnicianTicketsSummaryBinding
import org.json.JSONArray


data class Ticket(
    val status: String,
    val date: String,
    val problemType: String
)

class TicketsAdapter(private val ticketsList: List<Ticket>) : RecyclerView.Adapter<TicketsAdapter.TicketViewHolder>() {

    // Clase interna que define el ViewHolder (representa un ítem de la lista)
    class TicketViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {
        val ticketStatus: TextView = itemView.findViewById(R.id.txtEstado)
        val ticketDate: TextView = itemView.findViewById(R.id.txtFecha)
        val ticketProblemType: TextView = itemView.findViewById(R.id.txtDescripcion)

        // Método para vincular los datos del ticket con las vistas
        fun bind(ticket: Ticket) {
            ticketStatus.text = ticket.status
            ticketDate.text = ticket.date
            ticketProblemType.text = ticket.problemType
        }
    }

    // Infla el layout del ítem para cada fila del RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ticket, parent, false)  // Aquí inflamos el layout item_ticket.xml
        return TicketViewHolder(view)
    }

    // Vincula los datos del ticket con las vistas del ViewHolder
    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val ticket = ticketsList[position]
        holder.bind(ticket)
    }

    // Retorna el tamaño de la lista de tickets
    override fun getItemCount(): Int {
        return ticketsList.size
    }
}

class TechnicianTicketsSummary : AppCompatActivity() {
    private lateinit var binding: ActivityTechnicianTicketsSummaryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTechnicianTicketsSummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Variables para la petición al endpoint del servidor
        val urlAlTickets =  "http://10.0.2.2:3001/tickets"
        val queue = Volley.newRequestQueue(this)

        //Variables para contenedores de tickets
        val recyclerViewAllTickets: RecyclerView = findViewById(R.id.recyclerTodosTickets)
        val allTicketList = mutableListOf<Ticket>()


        val listener = Response.Listener<JSONArray> { result ->
            Log.e("Result", result.toString())

            //Recorremos cada objeto y obtenemos la información para construir cada resumen de ticket
            for(i in 0 until result.length()){
                val status = (result.getJSONObject(i).getString("status"))
                val date = (result.getJSONObject(i).getString("dateOpened"))
                val problemType = (result.getJSONObject(i).getString("description"))
                allTicketList.add(Ticket(status, date, problemType))
            }

        }

        val error = Response.ErrorListener { error ->
            Log.e("Error", error.message.toString())
        }

        val userValidation = JsonArrayRequest(
            Request.Method.GET, urlAlTickets,
            null, listener, error)

        queue.add(userValidation)

        //Adaptamos cada ticket
        val adapter = TicketsAdapter(allTicketList)
        recyclerViewAllTickets.layoutManager = LinearLayoutManager(this) // Establece el layout manager
        recyclerViewAllTickets.adapter = adapter  // Asigna el adaptador al RecyclerView

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