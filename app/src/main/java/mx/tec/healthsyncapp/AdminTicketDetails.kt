package mx.tec.healthsyncapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.android.volley.toolbox.Volley
import mx.tec.healthsyncapp.databinding.ActivityAdminTicketDetailsBinding
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


        //Variables para petición al servidor
        val ticketId = intent.getStringExtra("ticketId") ?: return
        val subdomain = getString(R.string.subdomain)
        val urlTicket =  "$subdomain/tickets/$ticketId"
        val queue = Volley.newRequestQueue(this)

        ticketRepository = TicketRepository(queue)
        ticketViewModel = TicketViewModel()

        //Observamos los cambios del modelo y si son nulos hacemos petición al servidor
        ticketViewModel = ViewModelProvider(this).get(TicketViewModel::class.java)
        if (ticketViewModel.ticket.value == null) {
            ticketViewModel.fetchTicket(ticketId, urlTicket, queue, this)
        }

        ticketViewModel.ticket.observe(this) { ticket ->
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            manageFragments(
                status = ticket.status,
                fragmentTransaction = fragmentTransaction,
                technicianFragment = supportFragmentManager.findFragmentById(binding.fragContainerTC.id),
                resolutionFragment = supportFragmentManager.findFragmentById(binding.fragContainerSolution.id),
                noResolutionFragment = supportFragmentManager.findFragmentById(binding.fragContainerNoSolution.id)
            )

            fragmentTransaction.commit()
        }



        val btnReturn = findViewById<ImageButton>(R.id.btnHome)
        btnReturn.setOnClickListener{
            val intent = Intent(this@AdminTicketDetails, AdminFeatures::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }



    }

    private fun manageFragments(
        status: String,
        fragmentTransaction: FragmentTransaction,
        technicianFragment: Fragment?,
        resolutionFragment: Fragment?,
        noResolutionFragment: Fragment?
    ) {
        when (status) {
            "Sin empezar" -> {
                technicianFragment?.let { fragmentTransaction.hide(it) }
                resolutionFragment?.let { fragmentTransaction.hide(it) }
                noResolutionFragment?.let { fragmentTransaction.hide(it) }
            }
            "En progreso" -> {
                resolutionFragment?.let { fragmentTransaction.hide(it) }
                noResolutionFragment?.let { fragmentTransaction.hide(it) }
            }
            "Resuelto" -> {

                noResolutionFragment?.let { fragmentTransaction.hide(it) }
            }
            "Eliminado" -> {
                technicianFragment?.let { fragmentTransaction.hide(it) }
                resolutionFragment?.let { fragmentTransaction.hide(it) }
                noResolutionFragment?.let { fragmentTransaction.hide(it) }
            }
            "No resuelto" -> {
                resolutionFragment?.let { fragmentTransaction.hide(it) }
            }
            else -> {
                Log.e("Resultado", "No cumple con las condiciones")
            }
        }
    }


}