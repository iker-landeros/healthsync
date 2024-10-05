package mx.tec.healthsyncapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mx.tec.healthsyncapp.R
import mx.tec.healthsyncapp.databinding.FragmentActiveTimeBinding
import mx.tec.healthsyncapp.databinding.FragmentResolutionEvidenceBinding

class ResolutionEvidence : Fragment() {
    private lateinit var binding: FragmentResolutionEvidenceBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResolutionEvidenceBinding.inflate(inflater, container, false)
        return binding.root
    }
}