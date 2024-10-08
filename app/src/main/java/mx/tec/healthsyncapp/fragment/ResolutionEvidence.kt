package mx.tec.healthsyncapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.android.volley.toolbox.Volley
import mx.tec.healthsyncapp.R
import mx.tec.healthsyncapp.databinding.FragmentActiveTimeBinding
import mx.tec.healthsyncapp.databinding.FragmentResolutionEvidenceBinding
import mx.tec.healthsyncapp.databinding.FragmentTicketDetailsBinding
import mx.tec.healthsyncapp.repository.TicketRepository
import mx.tec.healthsyncapp.viewmodel.TicketViewModel

class ResolutionEvidence : Fragment() {
    private lateinit var binding: FragmentResolutionEvidenceBinding
    private lateinit var ticketViewModel: TicketViewModel
    private lateinit var ticketRepository: TicketRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResolutionEvidenceBinding.inflate(inflater, container, false)

        ticketViewModel = ViewModelProvider(requireActivity()).get(TicketViewModel::class.java)
        val queue = Volley.newRequestQueue(requireContext())
        ticketRepository = TicketRepository(queue)

        ticketViewModel.ticket.observe(viewLifecycleOwner) { ticket ->
            ticket?.let {
                binding.textProcesoRevisionCliente.text = ticket.revisionProcess
                binding.txtDiagnosticoCliente.text = ticket.diagnosis
                binding.txtProcesoResolucionCliente.text = ticket.solutionProcess
                ticket.ticketImage?.let { base64Image ->
                    ticketViewModel.loadImage(base64Image.data, ticketRepository)
                }
            }
        }
        ticketViewModel.imageBitmap.observe(viewLifecycleOwner) { bitmap ->
            bitmap?.let {
                binding.imgEvidencia.setImageBitmap(it)
            } ?: run {
                binding.imgEvidencia.setImageResource(R.drawable.estado_en_curso_background)
            }
        }

        return binding.root
    }
}