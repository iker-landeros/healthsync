package mx.tec.healthsyncapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mx.tec.healthsyncapp.R
import mx.tec.healthsyncapp.databinding.FragmentNoResolutionEvidenceAdminBinding


class NoResolutionEvidence : Fragment() {
    private lateinit var binding: FragmentNoResolutionEvidenceAdminBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoResolutionEvidenceAdminBinding.inflate(inflater, container, false)
        return binding.root
    }
}