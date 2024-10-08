package mx.tec.healthsyncapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.android.volley.toolbox.Volley
import mx.tec.healthsyncapp.databinding.ActivityAdminTicketDetailsBinding
import mx.tec.healthsyncapp.fragment.TechnicianInCharge
import mx.tec.healthsyncapp.fragment.TicketDetails
import mx.tec.healthsyncapp.fragment.TicketProcessTech
import mx.tec.healthsyncapp.repository.TicketRepository
import mx.tec.healthsyncapp.viewmodel.TicketViewModel

class AdminTicketDetails : AppCompatActivity() {
    private lateinit var binding: ActivityAdminTicketDetailsBinding
    private lateinit var ticketViewModel: TicketViewModel
    private lateinit var ticketRepository: TicketRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminTicketDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Obtenemos los datos de la sesion
        val sharedpref = getSharedPreferences("sesion", MODE_PRIVATE)
        val name = sharedpref.getString("name", "#")

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
            if (ticket.status == "Resuelto") {
                fragmentTransaction.replace(binding.fragContainerTC.id, TechnicianInCharge())
                fragmentTransaction.replace(binding.fragContainerDetails.id, TicketDetails())
                fragmentTransaction.replace(binding.fragContainerSolution.id, TicketProcessTech())


            } else {
                Log.e("Resultado", "No cumple con las condiciones")
            }

            //fragmentTransaction.commit()
        }




    }


}