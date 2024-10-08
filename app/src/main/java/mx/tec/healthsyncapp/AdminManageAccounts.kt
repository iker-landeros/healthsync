package mx.tec.healthsyncapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import org.json.JSONArray

class AdminManageAccounts : AppCompatActivity() {
    private lateinit var binding: ActivityAdminManageAccountsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminManageAccountsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val urlMyTickets  = "http://10.0.2.2:3001/technicians"
        val queue = Volley.newRequestQueue(this)

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
            Log.e("Error", error.message.toString())
        }

        val userValidationMyTickets = JsonArrayRequest(
            Request.Method.GET, urlMyTickets,
            null, listenerTechnicians, errorMyTickets)

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