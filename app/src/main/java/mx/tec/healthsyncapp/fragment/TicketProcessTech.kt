package mx.tec.healthsyncapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mx.tec.healthsyncapp.R
import mx.tec.healthsyncapp.databinding.FragmentTicketProcessAdminBinding
import mx.tec.healthsyncapp.databinding.FragmentTicketProcessTechBinding

class TicketProcessTech : Fragment() {
    private lateinit var binding: FragmentTicketProcessTechBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTicketProcessTechBinding.inflate(inflater, container, false)
        return binding.root
    }
}