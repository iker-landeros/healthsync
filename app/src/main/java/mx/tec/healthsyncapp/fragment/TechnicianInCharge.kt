package mx.tec.healthsyncapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import mx.tec.healthsyncapp.databinding.FragmentTechnicianInChargeBinding
import mx.tec.healthsyncapp.viewmodel.TicketViewModel

class TechnicianInCharge : Fragment() {
    private lateinit var binding: FragmentTechnicianInChargeBinding
    private lateinit var ticketViewModel: TicketViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTechnicianInChargeBinding.inflate(inflater, container, false)

        ticketViewModel = ViewModelProvider(requireActivity()).get(TicketViewModel::class.java)

        ticketViewModel.ticket.observe(viewLifecycleOwner) { ticket ->
            ticket?.let {
                binding.txtTecnicoACargo.text = it.technicianName
            }
        }

        return binding.root
    }
}
