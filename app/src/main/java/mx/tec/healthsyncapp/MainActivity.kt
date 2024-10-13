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

        //Creamos y obtenemos los datos de la sesion de la aplicacion
        val sharedpref = getSharedPreferences("sesion", MODE_PRIVATE)
        val name = sharedpref.getString("name", "#")
        val userType = sharedpref.getString("userType", "#")

        //Verificamos si hay datos válidos en la sesión
        if (name != "#") {
            if (userType == "technician"){ //Verificamos el tipo de usuario y le damos acceso a su vista de aplicación
                val intent = Intent(this@MainActivity, TechnicianTicketsSummary::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            } else if (userType == "admin"){ //Verificamos el tipo de usuario y le damos acceso a su vista de aplicación
                val intent = Intent(this@MainActivity, AdminOptions::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Tipo de usuario no válido", Toast.LENGTH_SHORT).show()
            }
        }

        //Variables para la petición del inicio de sesión al API
        val subdomain = getString(R.string.subdomain)
        val urlLogin = "$subdomain/login"
        val queue = Volley.newRequestQueue(this)


        binding.btnIniciarSesion.setOnClickListener {
            //Obtenemos los datos ingresados por el usuario, tanto nombre de usuario como su contraseña
            val userInput = binding.edtUsuario.text.toString()
            val passwordInput = binding.edtContrasena.text.toString()

            //Agregamos los datos al cuerpo JSON
            val body = JSONObject() //Creamos y agregamos datos al cuerpo para la petición de validación
            body.put("username", userInput)
            body.put("password", passwordInput)

            //Escuchamos la respuesta 202 del API
            val listener = Response.Listener<JSONObject> { result ->
                Log.e("Result", result.toString())

                //Obtenemos los datos de la respuesta y los guardamos en la sesión
                val idUser = result.getString("idUser")
                val userType = result.getString("userType")
                val name = result.getString("name")
                with(sharedpref.edit()){
                    putString("name", name)
                    putString("userType", userType)
                    putString("idUser", idUser)
                    commit()
                }

                //Verificamos el tipo de usuario
                if (userType == "technician"){ //Navegación a primera pantalla para técnico
                    val intent = Intent(this@MainActivity, TechnicianTicketsSummary::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }  else if (userType == "admin"){ //Navegación a primera pantalla para administrador
                    val intent = Intent(this@MainActivity, AdminOptions::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                } else{ //En caso de que no cumpla con ningún tipo de usuario
                    Log.e("Tipo de usuario:", "No válido")
                    Toast.makeText(this, "Tipo de usuario no válido", Toast.LENGTH_SHORT).show()
                }
            }

            //Escuchamos la respuesta 404 del API, que sería usuario o contraseña inválidos
            val error = Response.ErrorListener { error ->
                Log.e("Error", error.message.toString())
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }

            //Hacemos petición al servidor para validar el usuario y contraseña ingresados
            val userValidation = JsonObjectRequest(
                Request.Method.POST,
                urlLogin,
                body,
                listener,
                error
            )

            //Agregamos la petición a la cola de peticiones
            queue.add(userValidation)
        }
    }
}