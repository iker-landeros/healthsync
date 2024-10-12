package mx.tec.healthsyncapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.android.volley.toolbox.Volley
import mx.tec.healthsyncapp.R
import mx.tec.healthsyncapp.databinding.FragmentResolutionEvidenceAdminBinding
import mx.tec.healthsyncapp.repository.TicketRepository
import mx.tec.healthsyncapp.viewmodel.TicketViewModel

/*
 Fragmento dedicado a visualizar la información de la evidencia cargada de la SOLUCIÓN de los
 tickets sobre las fallas técnicas, esto desde la pantalla de administrador en los detalles de técnico
 */
class ResolutionEvidence : Fragment() {
    private lateinit var binding: FragmentResolutionEvidenceAdminBinding

    private lateinit var ticketViewModel: TicketViewModel
    private lateinit var ticketRepository: TicketRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResolutionEvidenceAdminBinding.inflate(inflater, container, false)

        ticketViewModel = ViewModelProvider(requireActivity()).get(TicketViewModel::class.java)
        val queue = Volley.newRequestQueue(requireContext())
        ticketRepository = TicketRepository(queue)

        ticketViewModel.ticket.observe(viewLifecycleOwner) { ticket ->
            ticket?.let {
                binding.textProcesoRevisionCliente.text = ticket.revisionProcess
                binding.txtDiagnosticoCliente.text = ticket.diagnosis
                binding.txtProcesoResolucionCliente.text = ticket.solutionProcess
                binding.txtComponentesCliente.text = ticket.components?.get(0) ?: "Sin componentes"
                ticket.ticketImage?.let { base64Image ->
                    ticketViewModel.loadImage(base64Image, ticketRepository)
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