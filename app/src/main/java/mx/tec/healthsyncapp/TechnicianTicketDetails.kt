package mx.tec.healthsyncapp

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
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
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
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
        val urlTicket =  "$subdomain/tickets/$ticketId"
        val queue = Volley.newRequestQueue(this)

        ticketRepository = TicketRepository(queue)
        ticketViewModel = TicketViewModel()

        //Observamos los cambios del modelo y si son nulos hacemos petición al servidor
        ticketViewModel = ViewModelProvider(this).get(TicketViewModel::class.java)
        if (ticketViewModel.ticket.value == null) {
            ticketViewModel.fetchTicket(ticketId, urlTicket, queue, this)
        }

        //Instanciamos el manejador de fragmentos
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        //Observamos los cambios y modificamos los elementos de acuerdo al estado del ticket
        ticketViewModel.ticket.observe(this) { ticket ->
            val technicianFragment = supportFragmentManager.findFragmentById(binding.fragContainerTC.id)
            manageFragments(ticket.technicianName, name, ticket.status, fragmentTransaction, technicianFragment)

            val btnClaim = findViewById<View>(R.id.btnReclamarTicket)
            btnClaim.setOnClickListener{
                showConfirmClaimTkt(ticketId,
                    idTechnician,
                    subdomain,
                    ticketRepository,
                    this)
            }
            val btnRemove = findViewById<View>(R.id.btnEliminarTicket)
            btnRemove.setOnClickListener{
                showConfirmRevomeTkt(ticketId,
                    idTechnician,
                    subdomain,
                    ticketRepository,
                    this)
            }
            val btnNoSolution = findViewById<View>(R.id.btnNoResuelto)
            btnNoSolution.setOnClickListener{
                val intent = Intent(this@TechnicianTicketDetails, TechnicianTicketNoSolution::class.java)
                intent.putExtra("ticketId", ticketId)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            val btnFinish = findViewById<Button>(R.id.btnFinalizarTicket)
            btnFinish.setOnClickListener{ val intent = Intent(this@TechnicianTicketDetails, TechnicianTicketSolution::class.java)
                intent.putExtra("ticketId", ticketId)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            fragmentTransaction.commit()
        }

        val btnGoBack = findViewById<ImageButton>(R.id.btnHome)
        btnGoBack.setOnClickListener{
            val intent = Intent(this@TechnicianTicketDetails, TechnicianTicketsSummary::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

    }

    private fun manageFragments(
        technicianName: String?,
        TCName: String?,
        status: String,
        fragmentTransaction: FragmentTransaction,
        technicianFragment: Fragment?,

    ) {
        when (status) {
            "Sin empezar" -> {
                technicianFragment?.let { fragmentTransaction.hide(it) }
                val btnFinish = findViewById<View>(R.id.btnFinish)
                btnFinish.visibility = View.GONE
                val btnNoSolution = findViewById<View>(R.id.btnNoResuelto)
                btnNoSolution.visibility = View.GONE
            }
            "En progreso" -> {
                if(technicianName == TCName){
                    val btnClaim = findViewById<View>(R.id.btnClaim)
                    btnClaim.visibility = View.GONE
                } else {
                    val btnFinish = findViewById<View>(R.id.btnFinish)
                    btnFinish.visibility = View.GONE
                    val btnRemove = findViewById<View>(R.id.btnRemove)
                    btnRemove.visibility = View.GONE
                    val btnNoSolution = findViewById<View>(R.id.btnNoResuelto)
                    btnNoSolution.visibility = View.GONE
                }
            }
            else -> {
                Log.e("Resultado", "No cumple con las condiciones")
            }
        }

    }

    private fun showConfirmRevomeTkt(
        ticketId: String,
        idTechnician: String,
        subdomain: String,
        ticketRepository: TicketRepository,
        context: Context
    ){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // Eliminar el título del diálogo

        val dialogView = LayoutInflater.from(this).inflate(R.layout.confirm_remove_tkt, null)
        dialog.setContentView(dialogView) // Establecer el contenido del diálogo

        val btnNo = dialog.findViewById<Button>(R.id.btnNo)
        btnNo.setOnClickListener{
            dialog.dismiss()
        }

        val btnSi = dialog.findViewById<Button>(R.id.btnSi)
        btnSi.setOnClickListener{
            ticketViewModel.updateTicket(ticketId, idTechnician, "Eliminado", subdomain, ticketRepository, context)
            val intent = Intent(this@TechnicianTicketDetails, TechnicianTicketsSummary::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showConfirmClaimTkt(
        ticketId: String,
        idTechnician: String,
        subdomain: String,
        ticketRepository: TicketRepository,
        context: Context
    ){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // Eliminar el título del diálogo

        val dialogView = LayoutInflater.from(this).inflate(R.layout.confirm_claim_tkt, null)
        dialog.setContentView(dialogView) // Establecer el contenido del diálogo

        val btnNo = dialog.findViewById<Button>(R.id.btnNo)
        btnNo.setOnClickListener{
            dialog.dismiss()
        }

        val btnSi = dialog.findViewById<Button>(R.id.btnSi)
        btnSi.setOnClickListener{
            ticketViewModel.updateTicket(ticketId, idTechnician, "En progreso", subdomain, ticketRepository, context)
            val intent = Intent(this@TechnicianTicketDetails, TechnicianTicketsSummary::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            dialog.dismiss()
        }

        dialog.show()
    }
}