package mx.tec.healthsyncapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import mx.tec.healthsyncapp.databinding.ActivityAdminOptionsBinding

class AdminOptions : AppCompatActivity() {
    private  lateinit var binding:ActivityAdminOptionsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Función del botón de cerrar sesión, limpiando todos los datos de Shared Preferences
        binding.btnLogout.setOnClickListener{
            val sharedpref = getSharedPreferences("sesion", MODE_PRIVATE)
            val editor = sharedpref.edit()
            editor.clear()
            editor.apply()
            //Después de limpiar los datos, enviamos al usuario a la pantalla de inicio de sesión
            val intent = Intent(this@AdminOptions, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        binding.btnEstadisticas.setOnClickListener{
            val intent = Intent(this@AdminOptions, PrimaryStats::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        binding.btnHistorialTickets.setOnClickListener{
            val intent = Intent(this@AdminOptions, AdminFeatures::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        binding.btnAdministrar.setOnClickListener{
            val intent = Intent(this@AdminOptions, AdminManageAccounts::class.java)
            startActivity(intent)
        }
    }
}