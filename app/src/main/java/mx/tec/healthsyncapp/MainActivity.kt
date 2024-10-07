package mx.tec.healthsyncapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import mx.tec.healthsyncapp.databinding.ActivityMainBinding
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val url = "http://10.0.2.2:3001/login"
        val queue = Volley.newRequestQueue(this)


        val sharedpref = getSharedPreferences("sesion", MODE_PRIVATE)
        val user = sharedpref.getString("user", "#")
        val userType = sharedpref.getString("userType", "#")


        if (userType != "#") {
            if (userType == "technician"){ //Verificamos el tipo de usuario y le damos acceso a su vista de aplicación
                val intent = Intent(this@MainActivity, TechnicianTicketsSummary::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            if (userType == "admin"){ //Verificamos el tipo de usuario y le damos acceso a su vista de aplicación
                val intent = Intent(this@MainActivity, AdminOptions::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }

        binding.btnIniciarSesion.setOnClickListener {
            val userInput = binding.edtUsuario.text.toString()
            val passwordInput = binding.edtContrasena.text.toString()

            val body = JSONObject() //Creamos y agregamos datos al cuerpo para la petición de validación
            body.put("username", userInput)
            body.put("password", passwordInput)

            val listener = Response.Listener<JSONObject> { result ->
                Log.e("Result", result.toString())

                val message = result.getString("message")
                if (message == "ok"){ //Si existe el usuario guardamos sus datos
                    val idUser = result.getString("idUser")
                    val userType = result.getString("userType")
                    val name = result.getString("name")
                    with(sharedpref.edit()){
                        putString("user", user)
                        putString("name", name)
                        putString("userType", userType)
                        putString("idUser", idUser)
                        commit()
                    }
                    if (userType == "technician"){ //Verificamos el tipo de usuario y le damos acceso a su vista de aplicación
                        val intent = Intent(this@MainActivity, TechnicianTicketsSummary::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                    if (userType == "admin"){ //Verificamos el tipo de usuario y le damos acceso a su vista de aplicación
                        val intent = Intent(this@MainActivity, AdminOptions::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                } else{
                    Log.e("Result", "incorrecto")
                    Toast.makeText(this, "Usuario o contraseña incorrecta", Toast.LENGTH_SHORT).show()
                }
            }
            val error = Response.ErrorListener { error ->
                Log.e("Error", error.message.toString())
            }

            val userValidation = JsonObjectRequest(Request.Method.POST, url,
                body, listener, error)

            queue.add(userValidation)
        }
    }
}