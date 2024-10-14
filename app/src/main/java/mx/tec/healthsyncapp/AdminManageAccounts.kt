package mx.tec.healthsyncapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import mx.tec.healthsyncapp.adapter.TechnicianInfoAdapter
import mx.tec.healthsyncapp.adapter.TechnicianStatsAdapter
import mx.tec.healthsyncapp.databinding.ActivityAdminManageAccountsBinding
import mx.tec.healthsyncapp.model.TechnicianInfo
import mx.tec.healthsyncapp.model.TechnicianStats
import mx.tec.healthsyncapp.utils.SesionUtil
import org.json.JSONArray

class AdminManageAccounts : AppCompatActivity() {
    private lateinit var binding: ActivityAdminManageAccountsBinding
    private lateinit var sesionUtil: SesionUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminManageAccountsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val subdomain = getString(R.string.subdomain)
        val urlManageAccounts  = "$subdomain/technicians"
        val queue = Volley.newRequestQueue(this)

        sesionUtil = SesionUtil()
        val sharedPref = getSharedPreferences("sesion", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", null) ?: return

        val recyclerViewTechnicians: RecyclerView = binding.rvTechnicians
        val technicians = mutableListOf<TechnicianInfo>()

        val adapterTechnician = TechnicianInfoAdapter(technicians)
        recyclerViewTechnicians.layoutManager = LinearLayoutManager(this)
        recyclerViewTechnicians.adapter = adapterTechnician

        val listenerTechnicians = Response.Listener<JSONArray> { result ->
            for(i in 0 until result.length()){
                val name = (result.getJSONObject(i).getString("name"))
                val username = (result.getJSONObject(i).getString("username"))
                val idUser = (result.getJSONObject(i).getString("idUser"))
                technicians.add(TechnicianInfo(username, name, idUser))
            }
            adapterTechnician.notifyDataSetChanged()
        }

        val errorMyTickets = Response.ErrorListener { error ->
            val networkResponse = error.networkResponse
            if (networkResponse != null) {
                when (networkResponse.statusCode) {
                    401 -> {
                        Toast.makeText(this, "Error al obtener datos", Toast.LENGTH_SHORT).show()
                        Log.e("Error 401", "Error al obtener datos")
                    }
                    403 -> {
                        Toast.makeText(this, "Token no válido", Toast.LENGTH_SHORT).show()
                        Log.e("Error 403", "Token no válido")
                        sesionUtil.logout(this)
                    }
                    else -> {
                        Toast.makeText(this, "Error desconocido", Toast.LENGTH_SHORT).show()
                        Log.e("Error general", error.message.toString())
                    }
                }
            } else {
                Toast.makeText(this, "Error de red desconocido", Toast.LENGTH_SHORT).show()
                Log.e("Error desconocido", error.message.toString())
            }
        }

        val userValidationMyTickets = object: JsonArrayRequest(
            Request.Method.GET, urlManageAccounts,
            null, listenerTechnicians, errorMyTickets
        ){
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $token" // Añadir el token en el header
                return headers
            }
        }

        queue.add(userValidationMyTickets)

        binding.btnHome.setOnClickListener{
            val intent = Intent(this@AdminManageAccounts, AdminOptions::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        binding.btnAddTechnician.setOnClickListener{
            val intent = Intent(this@AdminManageAccounts, AdminAddTechnician::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}