package mx.tec.healthsyncapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.android.volley.toolbox.Volley
import mx.tec.healthsyncapp.R
import mx.tec.healthsyncapp.databinding.FragmentNoResolutionAdminBinding
import mx.tec.healthsyncapp.databinding.FragmentNoResolutionEvidenceAdminBinding
import mx.tec.healthsyncapp.repository.TicketRepository
import mx.tec.healthsyncapp.viewmodel.TicketViewModel

/*
 Fragmento dedicado a visualizar la información de la evidencia cargada de la solución de los
 tickets sobre las fallas técnicas, esto desde la pantalla de administrador en los detalles de técnico
 */

class NoResolutionEvidence : Fragment() {
    private lateinit var binding: FragmentNoResolutionAdminBinding

    private lateinit var ticketViewModel: TicketViewModel
    private lateinit var ticketRepository: TicketRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoResolutionAdminBinding.inflate(inflater, container, false)

        ticketViewModel = ViewModelProvider(requireActivity()).get(TicketViewModel::class.java)
        val queue = Volley.newRequestQueue(requireContext())
        ticketRepository = TicketRepository(queue)

        ticketViewModel.ticket.observe(viewLifecycleOwner) { ticket ->
            ticket?.let {
                binding.txtMotivoNoResCliente.text = ticket.failedReason
                ticket.ticketImage?.let { base64Image ->
                    ticketViewModel.loadImage(base64Image, ticketRepository)
                }
            }
        }
        ticketViewModel.imageBitmap.observe(viewLifecycleOwner) { bitmap ->
            bitmap?.let {
                binding.imgEvidenciaNoRes.setImageBitmap(it)
            } ?: run {
                binding.imgEvidenciaNoRes.setImageResource(R.drawable.estado_en_curso_background)
            }
        }
        return binding.root
    }
}