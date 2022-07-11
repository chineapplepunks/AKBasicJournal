package com.akb.journal.dialog


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import com.akb.journal.Communicator
import com.akb.journal.R
import com.akb.journal.Util.Companion.generateHash
import com.akb.journal.Util.Companion.hideKeyboard
import com.akb.journal.databinding.LayoutResetPasswordBinding

class PasswordDialog(private val context: Context,
                     private val nav: NavController? = null,
                     private val communicator: Communicator,
                     private val type: Type) {

    constructor(context: Context,
                communicator: Communicator,
                type: Type) : this(context,null,communicator,type)

    enum class Type {SET, RESET}

    private var binding: LayoutResetPasswordBinding =
        LayoutResetPasswordBinding.inflate(LayoutInflater.from(context))
    private var alert: AlertDialog = AlertDialog.Builder(context).create()
    private var passwordViewModel = communicator.passwordViewModel

    fun create()  {
        if(type == Type.SET) {
            binding.btnCancel.visibility = View.GONE
            binding.lblCurrent.visibility = View.GONE
            binding.txtCurrent.visibility = View.GONE
            binding.lblTitle.setText(R.string.str_set_password_set_password)
            binding.btnConfirm.setText(R.string.str_set_password_set)
        } else {
            binding.btnCancel.visibility = View.VISIBLE
            binding.lblCurrent.visibility = View.VISIBLE
            binding.txtCurrent.visibility = View.VISIBLE
            binding.lblTitle.setText(R.string.str_reset_password_reset_password)
            binding.btnConfirm.setText(R.string.str_reset_password_confirm_button)
        }

        alert.setView(binding.root)

        binding.btnCancel.setOnClickListener {
            dismiss()
        }
        binding.btnConfirm.setOnClickListener {
            validatePasswords()
        }
    }
    fun show()  {
        alert.show()
    }

    private fun hide() {
        alert.hide()
    }

    private fun dismiss() {
        alert.dismiss()
    }

    private fun validateResetPassword() {
        if (communicator.loginHash.equals(generateHash(binding.txtCurrent.text.toString())))
        {
            binding.lblError.visibility = View.GONE
            validateSetPassword()
        } else {
            binding.lblError.text = "Current password is incorrect."
            clearTextBoxes()
            context.hideKeyboard(binding.root)
            binding.lblError.visibility = View.VISIBLE
        }
    }

    private fun clearTextBoxes() {
        binding.txtPassword.setText("")
        binding.txtConfirm.setText("")
        binding.txtPassword.requestFocus()
    }

    private fun validateSetPassword() {
        if(binding.txtPassword.text.toString() != binding.txtConfirm.text.toString()) {
            binding.lblError.setText(R.string.str_set_password_err_match)
            clearTextBoxes()
            context.hideKeyboard(binding.root)
            binding.lblError.visibility = View.VISIBLE
        } else if (binding.txtPassword.length() < 6) {
            binding.lblError.setText(R.string.str_set_password_characters)
            clearTextBoxes()
            context.hideKeyboard(binding.root)
            binding.lblError.visibility = View.VISIBLE
        } else {
            val hash = generateHash(binding.txtPassword.text.toString())
            passwordViewModel.saveHash(hash)
            communicator.loginHash = hash
            Toast.makeText(context,"Password successfully set.",Toast.LENGTH_LONG).show()
            if(nav != null) {
                nav.navigate(R.id.action_loginFragment_to_entryFragment)
            }
            dismiss()
        }
    }

    private fun validatePasswords() {
        if(type == Type.RESET) {
            validateResetPassword()
        } else {
            validateSetPassword()
        }
    }
}