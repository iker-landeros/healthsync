package mx.tec.healthsyncapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import mx.tec.healthsyncapp.databinding.ActivityUnclaimedTicketBinding
import org.json.JSONArray
import org.json.JSONObject

class UnclaimedTicket : AppCompatActivity() {
    private lateinit var binding: ActivityUnclaimedTicketBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUnclaimedTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val ticketId = intent.getStringExtra("ticketId")

        val subdomain = getString(R.string.subdomain)
        val urlTicketDetails = "http://$subdomain/tickets/$ticketId"
        val queue = Volley.newRequestQueue(this)

        val listener = Response.Listener<JSONArray> { result ->
            Log.e("Result my tickets", result.toString())
            if (result.length() > 0) {
                val ticket = result.getJSONObject(0)  // Obtenemos el primer ticket del array
                Log.e("Result my tickets", ticket.toString())

                with(binding) {
                    txtEstado.text = ticket.getString("status")
                    textNombreCliente.text = ticket.getString("senderName")
                    textFechaCliente.text = ticket.getString("dateOpened")
                    // Aquí puedes continuar obteniendo los otros valores necesarios
                    textExtensionCliente.text = ticket.getString("idExtension")
                    textTipoCliente.text = ticket.getString("idDeviceType")
                    textCategoriaCliente.text = ticket.getString("idProblemType")
                    textMotivoCliente.text = ticket.getString("description")
                }
            }
        }
        val error = Response.ErrorListener { error ->
            Log.e("Error", error.message.toString())
        }

        val ticketsDetails = JsonArrayRequest(
            Request.Method.GET, urlTicketDetails,
            null, listener, error)

        queue.add(ticketsDetails)

        binding.btnHome.setOnClickListener{
            val intent = Intent(this@UnclaimedTicket, TechnicianTicketsSummary::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }


        binding.btnReclamarTicket.setOnClickListener{
            val urlReclaimeTicket =  "http://10.0.2.2:3001/tickets/$ticketId"

            val queue = Volley.newRequestQueue(this)
            val sharedpref = getSharedPreferences("sesion", MODE_PRIVATE)
            val idTechnician =  sharedpref.getString("idUser", "#")
            val body = JSONObject()
            body.put("idTechnician", idTechnician)
            body.put("status", "En progreso")

            val listener = Response.Listener<JSONObject> { result ->
                Log.e("Result my tickets", result.toString())

            }
            val error = Response.ErrorListener { error ->
                Log.e("Error", error.message.toString())
            }

            val ticketsDetails = JsonObjectRequest(
                Request.Method.PUT, urlReclaimeTicket,
                body, listener, error)

            queue.add(ticketsDetails)

            val intent = Intent(this@UnclaimedTicket, TechnicianTicketsSummary::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }


    }
}