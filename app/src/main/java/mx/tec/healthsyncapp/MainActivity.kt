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

        val queue = Volley.newRequestQueue(this)
        val url = "http://10.0.2.2:3001/login"

        val sharedpref = getSharedPreferences("sesion", MODE_PRIVATE)
        val usuario = sharedpref.getString("usuario", "#")

        /* Comentamos esto por ahora para poder revisar el inicio de sesion varias veces
        if (usuario != "#") {
            val intent = Intent(this@MainActivity, Home::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        */
        binding.btnIniciarSesion.setOnClickListener {
            val usuario = binding.edtUsuario.text.toString()
            val contrasena = binding.edtContrasena.text.toString()

            val body = JSONObject() //Creamos y agregamos datos al cuerpo para la petición de validación
            body.put("username", usuario)
            body.put("password", contrasena)

            val listener = Response.Listener<JSONObject> { result ->
                Log.e("Resultado", result.toString())

                val mensaje = result.getString("message")
                if (mensaje == "ok"){ //Si existe el usuario guardamos sus datos
                    with(sharedpref.edit()){
                        putString("usuario", usuario)
                        commit()
                    }
                    val tipoUsuario = result.getString("userType")
                    if (tipoUsuario == "technician"){ //Verificamos el tipo de usuario y le damos acceso a su vista de aplicación
                        val intent = Intent(this@MainActivity, Home::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                }
            }
            val error = Response.ErrorListener { error ->
                Log.e("Error", error.message.toString())
            }

            val validacionUsuario = JsonObjectRequest(Request.Method.POST, url,
                body, listener, error)

            queue.add(validacionUsuario)
        }
    }
}