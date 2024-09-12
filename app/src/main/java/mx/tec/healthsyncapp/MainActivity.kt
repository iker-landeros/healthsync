package mx.tec.healthsyncapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import mx.tec.healthsyncapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedpref = getSharedPreferences("sesion", MODE_PRIVATE)
        val usuario = sharedpref.getString("usuario", "#")
        if (usuario != "#") {
            val intent = Intent(this@MainActivity, Home::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        binding.btnIniciarSesion.setOnClickListener {
            val usuario = binding.edtUsuario.text.toString()
            val contrasena = binding.edtContrasena.text.toString()

            // Por el momento es un usuario cualquiera, falta implementarlo con el API
            if (usuario == "juan" && contrasena == "hola") {
                with(sharedpref.edit()){
                    putString("usuario", usuario)
                    commit()
                }
                val intent = Intent(this@MainActivity, Home::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
    }
}