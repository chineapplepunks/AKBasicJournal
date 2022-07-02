package com.akb.journal.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.akb.journal.databinding.LayoutAboutBinding

class AboutFragment : Fragment() {

    lateinit var binding: LayoutAboutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutAboutBinding.inflate(layoutInflater)

        binding.button.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }
}