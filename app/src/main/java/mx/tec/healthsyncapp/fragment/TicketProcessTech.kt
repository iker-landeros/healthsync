package mx.tec.healthsyncapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import mx.tec.healthsyncapp.R
import mx.tec.healthsyncapp.databinding.FragmentTicketProcessTechBinding
import mx.tec.healthsyncapp.viewmodel.TicketViewModel

class TicketProcessTech : Fragment() {
    private lateinit var binding: FragmentTicketProcessTechBinding
    private lateinit var ticketViewModel: TicketViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTicketProcessTechBinding.inflate(inflater, container, false)
        return binding.root
    }
}