package mx.tec.healthsyncapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import mx.tec.healthsyncapp.databinding.ActivityAdminAddTechnicianBinding
import org.json.JSONObject

class AdminAddTechnician : AppCompatActivity() {
    private lateinit var binding: ActivityAdminAddTechnicianBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminAddTechnicianBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnUpdate.setOnClickListener {
            val name = binding.edtName.text.toString().trim()
            val username = binding.edtUsername.text.toString().trim()
            val password = binding.edtNewPassword.text.toString().trim()

            if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Es Necesario Llenar Todos los Campos", Toast.LENGTH_SHORT).show()
            } else {
                addTechnician(name, username, password)
            }
        }

        binding.btnReturn.setOnClickListener {
            finish()
        }
    }

    private fun addTechnician(name: String, username: String, password: String) {
        val url = "http://10.0.2.2:3001/technicians"
        val queue = Volley.newRequestQueue(this)

        val jsonBody = JSONObject()
        jsonBody.put("name", name)
        jsonBody.put("username", username)
        jsonBody.put("password", password)

        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            { response ->
                Toast.makeText(this, "Técnico Agregado de Manera Exitosa", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@AdminAddTechnician, AdminManageAccounts::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            },
            { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        ) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return jsonBody.toString().toByteArray(Charsets.UTF_8)
            }
        }

        queue.add(stringRequest)
    }
}
