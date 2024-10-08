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
import com.android.volley.toolbox.Volley
import mx.tec.healthsyncapp.databinding.ActivityPrimaryStatsBinding
import mx.tec.healthsyncapp.model.TicketSummary
import org.json.JSONArray

class PrimaryStats : AppCompatActivity() {
    private lateinit var binding: ActivityPrimaryStatsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrimaryStatsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val urlPrimaryStats = "http://10.0.2.2:3001/tickets/statistics"
        val queue = Volley.newRequestQueue(this)

        val listener = Response.Listener<JSONArray> { result ->
            if (result.length() > 0) {
                val statistics = result.getJSONObject(0)  // Obtenemos el primer ticket del array
                Log.e("Result my tickets", statistics.toString())

                with(binding) {
                    txtNumTktsTotales.text = statistics.getString("totalTickets")
                    //txtNumTktsEmpezados.text = statistics.getString("progressingTickets")
                    txtNumTktsNoRes.text = statistics.getString("notSolvedTickets")
                    txtNumTktsRes.text = statistics.getString("solvedTickets")
                }
            }
        }

        val error = Response.ErrorListener { error ->
            Log.e("Error from My Tickets", error.message.toString())
        }

        val userValidationMyTickets = JsonArrayRequest(
            Request.Method.GET, urlPrimaryStats,
            null, listener, error)

        queue.add(userValidationMyTickets)

        binding.btnMasStats.setOnClickListener{
            val intent = Intent(this@PrimaryStats, SecondaryStats::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener{
            val intent = Intent(this@PrimaryStats, AdminOptions::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}