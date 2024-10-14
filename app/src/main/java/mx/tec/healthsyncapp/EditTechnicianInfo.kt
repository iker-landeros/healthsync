package mx.tec.healthsyncapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import mx.tec.healthsyncapp.databinding.ActivityEditTechnicianInfoBinding
import mx.tec.healthsyncapp.utils.SesionUtil
import org.json.JSONObject

class EditTechnicianInfo : AppCompatActivity() {
    private lateinit var binding: ActivityEditTechnicianInfoBinding
    private lateinit var requestQueue: RequestQueue
    private lateinit var originalName: String
    private lateinit var originalUsername: String
    private lateinit var technicianId: String
    private lateinit var sesionUtil: SesionUtil


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditTechnicianInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestQueue = Volley.newRequestQueue(this)

        originalName = intent.getStringExtra("TECHNICIAN_NAME") ?: ""
        originalUsername = intent.getStringExtra("TECHNICIAN_USERNAME") ?: ""
        technicianId = intent.getStringExtra("TECHNICIAN_ID") ?: ""

        binding.txtName.text = originalName
        binding.edtName.setText(originalName)
        binding.edtUsername.setText(originalUsername)

        binding.btnReturn.setOnClickListener {
            finish()
        }

        binding.btnUpdate.setOnClickListener {
            val newName = binding.edtName.text.toString().trim()
            val newUsername = binding.edtUsername.text.toString().trim()
            val newPassword = binding.edtNewPassword.text.toString().trim()

            if (newName != originalName || newUsername != originalUsername || newPassword.isNotEmpty()) {
                updateTechnician(newName, newUsername, newPassword)
            } else {
                Toast.makeText(this, "No se detectaron cambios", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateTechnician(newName: String, newUsername: String, newPassword: String) {
        val subdomain = getString(R.string.subdomain)
        val url = "$subdomain/technicians/$technicianId"

        sesionUtil = SesionUtil()
        val sharedPref = getSharedPreferences("sesion", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", null) ?: return

        val jsonBody = JSONObject()
        jsonBody.put("name", newName)
        jsonBody.put("username", newUsername)
        if (newPassword.isNotEmpty()) {
            jsonBody.put("password", newPassword)
        }

        val putRequest = object: JsonObjectRequest(
            Request.Method.PUT, url, jsonBody,
            { response ->
                Toast.makeText(this, "Datos Actualizados de Manera Exitosa", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@EditTechnicianInfo, AdminManageAccounts::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(
                    this,
                    "Error: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        ){
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $token" // Añadir el token en el header
                return headers
            }
        }

        requestQueue.add(putRequest)
    }
}
