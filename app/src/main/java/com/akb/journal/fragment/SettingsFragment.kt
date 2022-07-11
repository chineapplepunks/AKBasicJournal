package com.akb.journal.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.akb.journal.Communicator
import com.akb.journal.EntryDatabase
import com.akb.journal.R
import com.akb.journal.dialog.FontDialog
import com.akb.journal.dialog.PasswordDialog
import com.akb.journal.entity.Entry
import com.akb.journal.viewmodel.PasswordViewModel
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import java.io.PrintWriter

class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var communicator: Communicator
    private lateinit var passwordViewModel: PasswordViewModel
    private lateinit var fontDialog: FontDialog

    private fun export(entries: List<Entry>,
                       uri: Uri?) {
        val outputStream = context?.contentResolver?.openOutputStream(uri!!)
        var export = ""
        val it = entries.iterator()
        while (it.hasNext()) {
            val entry = it.next()
            export += entry.date + "\n"
            export += entry.text + "\n\n"
        }
        val writer = PrintWriter(outputStream!!)
        writer.print(export.dropLast(2))
        writer.close()
    }

    private var backupLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            communicator.entryViewModel.getEntries()
            communicator.entryViewModel.entries.observe(viewLifecycleOwner) {
                export(it, data?.data)
            }
        }
    }

    private var exportLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == Activity.RESULT_OK) {
            EntryDatabase.export(requireActivity().applicationContext,result.data?.data)
        }
    }

    private var importLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == Activity.RESULT_OK) {
            EntryDatabase.import(requireContext(),result.data?.data)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        communicator = ViewModelProvider(requireActivity()).get(Communicator::class.java)
        passwordViewModel = communicator.passwordViewModel
        fontDialog = FontDialog(requireContext(), communicator.settingsViewModel)
        fontDialog.create()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onDisplayPreferenceDialog(preference: Preference) = when {
        preference.key.equals("reset") -> {
            val dialog = PasswordDialog(
                requireContext(),
                communicator ,
                PasswordDialog.Type.RESET
            )
            dialog.create()
            dialog.show()
        }

        preference.key.equals("font") -> {
            fontDialog.show()
        }
        preference.key.equals("export") -> {
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "text/plain"
                putExtra(Intent.EXTRA_TITLE, "export.txt")
            }
            backupLauncher.launch(intent)
        }
        preference.key.equals("backup") -> {
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/octet-stream"
                putExtra(Intent.EXTRA_TITLE, "export.db")
            }
            exportLauncher.launch(intent)
        }
        preference.key.equals("import") -> {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/octet-stream"
                putExtra(Intent.EXTRA_TITLE,"*.db")
            }
            importLauncher.launch(intent)
        }
        preference.key.equals("about") -> {
            findNavController().navigate(R.id.action_settingsFragment_to_aboutFragment)
        }

        preference.key.equals("licenses") -> {
            findNavController().navigate(R.id.action_settingsFragment_to_licenseFragment)
        }
        else -> {
            super.onDisplayPreferenceDialog(preference)
        }
    }
}