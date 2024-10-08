package mx.tec.healthsyncapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import mx.tec.healthsyncapp.R
import mx.tec.healthsyncapp.databinding.FragmentActiveTimeBinding
import mx.tec.healthsyncapp.viewmodel.TicketViewModel

import java.math.RoundingMode
import java.text.DecimalFormat

class ActiveTime : Fragment() {
    private lateinit var binding: FragmentActiveTimeBinding
    private lateinit var ticketViewModel: TicketViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentActiveTimeBinding.inflate(inflater, container, false)

        ticketViewModel = ViewModelProvider(requireActivity()).get(TicketViewModel::class.java)

        ticketViewModel.ticket.observe(viewLifecycleOwner) { ticket ->
            ticket?.let {
                val formattedTime = formatToTwoDecimals(it.resolutionTimeHours)
                binding.txtPeriodo.text = "$formattedTime h"
            }
        }

        return binding.root
    }

    private fun formatToTwoDecimals(value: String): String {
        return try {
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.HALF_UP
            df.format(value.toDouble())
        } catch (e: NumberFormatException) {
            value // Si no es un número válido, retorna el valor original
        }
    }
}
