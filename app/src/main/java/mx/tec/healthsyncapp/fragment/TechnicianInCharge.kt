package mx.tec.healthsyncapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mx.tec.healthsyncapp.databinding.FragmentTechnicianInChargeBinding

class TechnicianInCharge : Fragment() {
    private lateinit var binding: FragmentTechnicianInChargeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTechnicianInChargeBinding.inflate(inflater, container, false)
        return binding.root
    }
}