package com.akb.journal.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.akb.journal.Communicator
import com.akb.journal.dialog.PasswordDialog
import com.akb.journal.R
import com.akb.journal.Util.Companion.generateHash
import com.akb.journal.Util.Companion.hideKeyboard
import com.akb.journal.databinding.LayoutLoginBinding

/**
 * The fragment used for the login.
 *
 * @author Adam Brooke
 */
class LoginFragment : Fragment() {

    private lateinit var binding: LayoutLoginBinding

    override fun onStart() {
        super.onStart()
        binding.txtLogin.setText("")
        binding.txtLogin.clearFocus()
        hideKeyboard()
    }
    /**
     * Creates the view.
     *
     * Also sets up the click listeners for the login button, as well as setting the
     * login hash in the communicator. If the login hash is blank or null, the
     * PasswordDialog will pop up, allowing you to set your password.
     *
     * @param inflater The layout inflater.
     * @param container The view group
     * @param savedInstanceState The bundle that holds a saved instance state.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutLoginBinding.inflate(inflater,container,false)

        val communicator = ViewModelProvider(requireActivity()).get(Communicator::class.java)
        val passwordViewModel = communicator.passwordViewModel
        binding.lytLogin.visibility = View.GONE
        binding.pBar.visibility = View.VISIBLE
        binding.txtLogin.clearFocus()
        hideKeyboard()

        communicator.loginHash = passwordViewModel.getHash()
        if (communicator.loginHash.isNullOrBlank() ) {
            val dialog = PasswordDialog(
                binding.root.context, findNavController(), communicator,PasswordDialog.Type.SET)
            dialog.create()
            dialog.show()
        }

        binding.lytLogin.visibility = View.VISIBLE
        binding.pBar.visibility = View.GONE

        binding.btnLogin.setOnClickListener {
            val hash = generateHash(binding.txtLogin.text.toString())
            if(hash == communicator.loginHash) {
                findNavController().navigate(R.id.action_loginFragment_to_entryFragment)
            } else {
                binding.lblLoginErr.setText(R.string.str_login_incorrect_password)
                binding.txtLogin.setText("")
                binding.lblLoginErr.visibility = View.VISIBLE
                hideKeyboard()
            }
        }
        return binding.root
    }

}