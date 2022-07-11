package com.akb.journal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.akb.journal.databinding.LayoutMainBinding
import com.akb.journal.fragment.SettingsFragment
import com.akb.journal.viewmodel.SettingsViewModel
import com.akb.journal.viewmodel.EntryViewModel
import com.akb.journal.viewmodel.PasswordViewModel
import java.util.*

/**
 * The Main Activity class. Doesn't really do much.
 * Creates the repository, view models and communicator.
 *
 * Also inflates the Main layout, that holds the nav host for fragments.
 *
 * @author Adam Brooke
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: LayoutMainBinding

    /**
     * The starting point for our application.
     *
     * All it does is create the Repo (whice creates the DB), creates the ViewModels,
     * creates the Communicator and the Settings preferences.
     *
     * @param savedInstanceState The bundle that holds a saved instance state. Not used.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = Repository(applicationContext)

        val entryViewModel = EntryViewModel(application, repository)
        val passwordViewModel = PasswordViewModel(application, repository)
        val settingsViewModel = SettingsViewModel(application)
        val communicator = ViewModelProvider(this).get(Communicator::class.java)

        communicator.entryViewModel = entryViewModel
        communicator.passwordViewModel = passwordViewModel
        communicator.settingsViewModel = settingsViewModel
        communicator.date = Calendar.getInstance().time

        binding = LayoutMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    /**
     * When we press the back button on the Settings page, we want to go back to the Entry
     * Fragment.
     */
    override fun onBackPressed() {
        val currentFragment =
            supportFragmentManager.fragments.last()?.childFragmentManager?.fragments?.get(0)
        if(currentFragment is SettingsFragment) {
            findNavController(R.id.main_fragment_view).navigate(
                R.id.action_settingsFragment_to_entryFragment)
        }
    }
}

