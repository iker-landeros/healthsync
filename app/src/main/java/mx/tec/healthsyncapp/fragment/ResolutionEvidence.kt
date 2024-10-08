package mx.tec.healthsyncapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import mx.tec.healthsyncapp.R
import mx.tec.healthsyncapp.databinding.FragmentActiveTimeBinding
import mx.tec.healthsyncapp.databinding.FragmentResolutionEvidenceBinding
import mx.tec.healthsyncapp.databinding.FragmentTicketDetailsBinding
import mx.tec.healthsyncapp.viewmodel.TicketViewModel

class ResolutionEvidence : Fragment() {
    private lateinit var binding: FragmentResolutionEvidenceBinding
    private lateinit var ticketViewModel: TicketViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResolutionEvidenceBinding.inflate(inflater, container, false)

        ticketViewModel = ViewModelProvider(requireActivity()).get(TicketViewModel::class.java)
        ticketViewModel.ticket.observe(viewLifecycleOwner) { ticket ->
            ticket?.let {
                binding.textProcesoRevisionCliente.text = ticket.revisionProcess
                binding.txtDiagnosticoCliente.text = ticket.diagnosis
                binding.txtProcesoResolucionCliente.text = ticket.solutionProcess
               // binding.imgEvidencia = ticketViewModel.loadImageFromBase64(ticket.ticketImage)
            }
        }
        return binding.root
    }
}