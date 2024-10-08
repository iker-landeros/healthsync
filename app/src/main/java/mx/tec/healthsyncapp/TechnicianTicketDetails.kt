package mx.tec.healthsyncapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import mx.tec.healthsyncapp.databinding.ActivityTicketDetailsTechnicianBinding
import mx.tec.healthsyncapp.fragment.TechnicianInCharge
import mx.tec.healthsyncapp.fragment.TicketDetails
import mx.tec.healthsyncapp.model.Ticket
import mx.tec.healthsyncapp.viewmodel.TicketViewModel
import org.json.JSONArray
import android.widget.Button
import android.widget.LinearLayout
import com.android.volley.toolbox.JsonObjectRequest
import mx.tec.healthsyncapp.repository.TicketRepository
import org.json.JSONObject


class TechnicianTicketDetails : AppCompatActivity() {
    private lateinit var binding: ActivityTicketDetailsTechnicianBinding
    private lateinit var ticketViewModel: TicketViewModel
    private lateinit var ticketRepository: TicketRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTicketDetailsTechnicianBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Obtenemos los datos de la sesion
        val sharedpref = getSharedPreferences("sesion", MODE_PRIVATE)
        val name = sharedpref.getString("name", "#")
        val idTechnician = sharedpref.getString("idUser", "#") ?: return
        findViewById<TextView>(R.id.txtNombre).text = name

        //Variables para petición al servidor
        val ticketId = intent.getStringExtra("ticketId") ?: return
        val subdomain = getString(R.string.subdomain)
        val subdomain2 = getString(R.string.subdomain2)
        val urlTicket =  "http://$subdomain/tickets/$ticketId"
        val queue = Volley.newRequestQueue(this)

        ticketRepository = TicketRepository(queue)
        ticketViewModel = TicketViewModel()

        //Observamos los cambios del modelo y si son nulos hacemos petición al servidor
        ticketViewModel = ViewModelProvider(this).get(TicketViewModel::class.java)
        if (ticketViewModel.ticket.value == null) {
            ticketViewModel.fetchTicket(ticketId, urlTicket, queue)
        }

        //Instanciamos el manejador de fragmentos
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        //Observamos los cambios y modificamos los elementos de acuerdo al estado del ticket
        ticketViewModel.ticket.observe(this) { ticket ->
            if (ticket.status == "Sin empezar") {
                fragmentTransaction.replace(binding.fragContainerDetails.id, TicketDetails())
                val technicianFragment = supportFragmentManager.findFragmentById(binding.fragContainerTC.id)
                if (technicianFragment != null) {
                    fragmentTransaction.hide(technicianFragment)
                }
                val btnFinish = findViewById<View>(R.id.btnFinish)
                btnFinish.visibility = View.GONE

                val btnRemove = findViewById<View>(R.id.btnRemove)
                btnRemove.setOnClickListener{
                    ticketViewModel.updateTicket(ticketId, idTechnician, "Eliminado", subdomain2, ticketRepository)
                }

                val btnClaim = findViewById<View>(R.id.btnClaim)
                btnClaim.setOnClickListener{
                    ticketViewModel.updateTicket(ticketId, idTechnician, "En progreso", subdomain2, ticketRepository)
                }

            } else if (ticket.status == "En progreso" && ticket.technicianName == name) {
                fragmentTransaction.replace(binding.fragContainerTC.id, TechnicianInCharge())
                fragmentTransaction.replace(binding.fragContainerDetails.id, TicketDetails())
                val btnClaim = findViewById<View>(R.id.btnClaim)
                btnClaim.visibility = View.GONE

                val btnRemove = findViewById<View>(R.id.btnRemove)
                btnRemove.setOnClickListener{
                    ticketViewModel.updateTicket(ticketId, idTechnician, "Eliminado", subdomain2, ticketRepository)
                }

            } else if (ticket.status == "En progreso" && ticket.technicianName != name) {
                fragmentTransaction.replace(binding.fragContainerTC.id, TechnicianInCharge())
                fragmentTransaction.replace(binding.fragContainerDetails.id, TicketDetails())
                val btnFinish = findViewById<View>(R.id.btnFinish)
                btnFinish.visibility = View.GONE
                val btnRemove = findViewById<View>(R.id.btnRemove)
                btnRemove.visibility = View.GONE
                val btnClaim = findViewById<View>(R.id.btnClaim)
                btnClaim.setOnClickListener{
                    ticketViewModel.updateTicket(ticketId, idTechnician, "En progreso", subdomain2, ticketRepository)
                }

            } else {
                Log.e("Resultado", "No cumple con las condiciones")
            }

            fragmentTransaction.commit()
        }


        val btnGoBack = findViewById<ImageButton>(R.id.btnLogout)
        btnGoBack.setOnClickListener{
            val intent = Intent(this@TechnicianTicketDetails, TechnicianTicketsSummary::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

    }

}