package mx.tec.healthsyncapp.fragment

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import mx.tec.healthsyncapp.R
import mx.tec.healthsyncapp.databinding.FragmentTicketDetailsBinding
import mx.tec.healthsyncapp.utils.StatusColorUtil
import mx.tec.healthsyncapp.viewmodel.TicketViewModel

class TicketDetails : Fragment() {
    private lateinit var binding: FragmentTicketDetailsBinding
    private lateinit var ticketViewModel: TicketViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTicketDetailsBinding.inflate(inflater, container, false)
        ticketViewModel = ViewModelProvider(requireActivity()).get(TicketViewModel::class.java)
        ticketViewModel.ticket.observe(viewLifecycleOwner) { ticket ->
            ticket?.let {
                binding.txtEstado.text = ticket.status
                binding.textNombreCliente.text = ticket.senderName
                binding.textFechaCliente.text = ticket.dateOpened
                binding.textMotivoCliente.text = ticket.description
                binding.textAreaCliente.text = ticket.areaName
                binding.textExtensionCliente.text = ticket.extensionNumber
                binding.textTipoCliente.text = ticket.deviceName
                binding.textCategoriaCliente.text = ticket.problemName

                val color = StatusColorUtil.getColorForStatus(requireContext(), ticket.status)
                val drawable = binding.txtEstado.background as GradientDrawable
                drawable.setColor(color)
            }
        }
        return binding.root
    }
}