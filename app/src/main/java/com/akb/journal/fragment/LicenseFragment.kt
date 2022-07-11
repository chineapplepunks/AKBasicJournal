package com.akb.journal.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.akb.journal.R
import com.akb.journal.databinding.LayoutLicenseBinding
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity

class LicenseFragment : Fragment() {

    lateinit var binding: LayoutLicenseBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutLicenseBinding.inflate(layoutInflater)

        binding.btnOk.setOnClickListener() {
            findNavController().navigate(R.id.action_licenseFragment_to_settingsFragment)
        }

        binding.btnThirdParty.setOnClickListener() {
            startActivity(Intent(context, OssLicensesMenuActivity::class.java))
        }

        return binding.root
    }
}