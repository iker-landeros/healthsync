package mx.tec.healthsyncapp.utils

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import mx.tec.healthsyncapp.MainActivity

class SesionUtil{
        fun logout(context: Context) {
            val sharedpref = context.getSharedPreferences("sesion", MODE_PRIVATE)
            val editor = sharedpref.edit()
            editor.clear()
            editor.apply()
            //Después de limpiar los datos, enviamos al usuario a la pantalla de inicio de sesión
            val intent = Intent(context, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
}