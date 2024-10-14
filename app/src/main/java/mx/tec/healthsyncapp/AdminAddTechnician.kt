package mx.tec.healthsyncapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import mx.tec.healthsyncapp.databinding.ActivityAdminAddTechnicianBinding
import mx.tec.healthsyncapp.utils.SesionUtil
import org.json.JSONObject

class AdminAddTechnician : AppCompatActivity() {
    private lateinit var binding: ActivityAdminAddTechnicianBinding
    private lateinit var sesionUtil: SesionUtil

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
        val subdomain = getString(R.string.subdomain)
        val url = "$subdomain/technicians"
        val queue = Volley.newRequestQueue(this)
        val sharedPref = getSharedPreferences("sesion", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", null) ?: return

        sesionUtil = SesionUtil()


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
        ) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return jsonBody.toString().toByteArray(Charsets.UTF_8)
            }

            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $token" // Añadir el token en el header
                return headers
            }
        }

        queue.add(stringRequest)
    }
}
