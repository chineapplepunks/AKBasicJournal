package com.akb.journal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.akb.journal.databinding.LayoutMainBinding
import com.akb.journal.preference.Settings
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
     * @param savedInstanceState The bundle that holds a saved instance state. Not used.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = Repository(applicationContext)

        val entryViewModel = EntryViewModel(application, repository)
        val passwordViewModel = PasswordViewModel(application, repository)
        val settings = Settings(applicationContext)
        val communicator = ViewModelProvider(this).get(Communicator::class.java)

        communicator.entryViewModel = entryViewModel
        communicator.passwordViewModel = passwordViewModel
        communicator.settings = settings
        communicator.date = Calendar.getInstance().time

        binding = LayoutMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}

