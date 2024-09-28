package mx.tec.healthsyncapp.utils

import android.content.Context
import androidx.core.content.ContextCompat
import mx.tec.healthsyncapp.R
import mx.tec.healthsyncapp.databinding.ItemTicketBinding

class StatusColorUtil {
    companion object{
        fun getColorForStatus(context: Context, status: String): Int{
            return when (status){
                "Sin empezar" -> ContextCompat.getColor(context, R.color.yellow)
                "En progreso"-> ContextCompat.getColor(context, R.color.blue)
                "Resuelto"-> ContextCompat.getColor(context, R.color.green)
                "Eliminado"-> ContextCompat.getColor(context, R.color.orange)
                "No resuelto"-> ContextCompat.getColor(context, R.color.purple)
                else -> ContextCompat.getColor(context, R.color.gray)
            }
        }
    }
}