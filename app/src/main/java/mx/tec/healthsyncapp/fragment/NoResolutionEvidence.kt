package mx.tec.healthsyncapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mx.tec.healthsyncapp.R
import mx.tec.healthsyncapp.databinding.FragmentNoResolutionEvidenceBinding
import mx.tec.healthsyncapp.databinding.FragmentResolutionEvidenceBinding

class NoResolutionEvidence : Fragment() {
    private lateinit var binding: FragmentNoResolutionEvidenceBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoResolutionEvidenceBinding.inflate(inflater, container, false)
        return binding.root
    }
}