package com.akb.journal.fragment

import android.os.Bundle
import android.text.Editable
import android.text.style.UnderlineSpan
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.akb.journal.Communicator
import com.akb.journal.R
import com.akb.journal.Util.Companion.hideKeyboard
import com.akb.journal.databinding.LayoutEntryBinding
import com.akb.journal.entity.CustomTextWatcher
import com.akb.journal.entity.EditHistory
import com.akb.journal.entity.EditHistoryItem
import com.akb.journal.entity.Entry
import com.akb.journal.viewmodel.EntryViewModel
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*


class EntryFragment : Fragment() {

    private lateinit var binding: LayoutEntryBinding
    private lateinit var entryViewModel: EntryViewModel
    private val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    private var skipTextWatch = false
    private var editHistory = EditHistory()
    private var newEntry = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onStop() {
        saveEntry()
        super.onStop()
    }

    private fun setTextWatcher() {
        binding.txtEntry.apply {
            removeTextChangedListener(textWatcher)
            addTextChangedListener(textWatcher)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = LayoutEntryBinding.inflate(layoutInflater)

        val communicator  = ViewModelProvider(requireActivity()).get(Communicator::class.java)
        entryViewModel = communicator.entryViewModel
        binding.lblDate.text = sdf.format(communicator.date)

        binding.txtEntry.textSize = communicator.settings.getFontSize()

        binding.btnCal.setOnClickListener {
            animateDropCalendar()
        }

        binding.lblDate.setOnClickListener {
            animateDropCalendar()
        }

        binding.calendarView.setOnDateChangeListener { _, i, i2, i3 ->
            saveEntry()
            val cal = Calendar.getInstance()
            cal.set(Calendar.YEAR, i)
            cal.set(Calendar.MONTH, i2)
            cal.set(Calendar.DAY_OF_MONTH, i3)
            communicator.date = cal.time
            binding.lblDate.text = sdf.format(communicator.date)
            animateDropCalendar()
            getEntry()
        }

        binding.btnNext.setOnClickListener {
            saveEntry()
            val milli = 1000 * 60 * 60 * 24
            communicator.date = Date(communicator.date.time + milli)
            binding.lblDate.text = sdf.format(communicator.date)
            binding.calendarView.date = communicator.date.time
            getEntry()
        }

        binding.btnPrev.setOnClickListener {
            saveEntry()
            val milli = 1000 * 60 * 60 * 24
            communicator.date = Date(communicator.date.time - milli)
            binding.lblDate.text = sdf.format(communicator.date)
            binding.calendarView.date = communicator.date.time
            getEntry()
        }

        binding.materialToolbar2.inflateMenu(R.menu.menu_items)
        binding.materialToolbar2.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.menu_save -> {
                    val snackbar = Snackbar.make(
                        binding.lytCoordinator, "Entry Saved.", Snackbar.LENGTH_SHORT)
                    snackbar.show()
                    saveEntry()
                    true
                }
                R.id.menu_delete -> {
                    deleteEntry()
                    true
                }
                R.id.menu_settings -> {
                    saveEntry()
                    hideKeyboard()
                    findNavController().navigate(R.id.action_entryFragment_to_settingsFragment)
                    true
                }
                R.id.menu_about -> {
                    hideKeyboard()
                    findNavController().navigate(R.id.action_entryFragment_to_aboutFragment)
                    true
                }
                R.id.menu_undo -> {
                    undo()
                    true
                }
                R.id.menu_redo -> {
                    redo()
                    true
                }
                else -> {
                    false
                }
            }
        }
        getEntry()
        setTextWatcher()
        return binding.root
    }

    private fun animateDropCalendar() {
        TransitionManager.beginDelayedTransition(binding.lytCal, AutoTransition())
        TransitionManager.beginDelayedTransition(binding.lytMain, AutoTransition())
        this.hideKeyboard()
        if (binding.lytCal.visibility == View.VISIBLE) {
            binding.lytCal.visibility = View.GONE
            binding.btnCal.setImageResource(R.drawable.ic_expand_black)
            binding.btnNext.visibility = View.VISIBLE
            binding.btnPrev.visibility = View.VISIBLE
            binding.txtEntry.requestFocus()
        } else {
            binding.lytCal.visibility = View.VISIBLE
            binding.btnCal.setImageResource(R.drawable.ic_collapse_black)
            binding.btnNext.visibility = View.INVISIBLE
            binding.btnPrev.visibility = View.INVISIBLE
        }
    }

    private fun saveEntry() {
        if (binding.txtEntry.text.isNotEmpty()) {
            val entry = Entry(
                binding.lblDate.text.toString(), binding.txtEntry.text.toString()
            )
            entryViewModel.saveEntry(entry)

        } else {
            return
        }
    }

    private fun removeTextWatcher() = binding.txtEntry.removeTextChangedListener(textWatcher)

    private fun addTextWatcher() = binding.txtEntry.addTextChangedListener(textWatcher)

    private fun getEntry() {

        entryViewModel.getEntry(binding.lblDate.text.toString())
        entryViewModel.entry.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.txtEntry.setText(it.text)
                binding.txtEntry.setSelection(binding.txtEntry.length())
                binding.txtEntry.requestFocus()
            } else {
                binding.txtEntry.setText("")
                binding.txtEntry.setSelection(0)
                binding.txtEntry.requestFocus()
            }
        }
        editHistory.reset()
        textWatcher.reset()
    }

    private fun deleteEntry() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(binding.root.context)
        builder.setTitle("Delete Entry")
        builder.setMessage(
            "This will delete the current entry on screen.\nThis cannot be undone.\nAre you sure?")
        builder.setPositiveButton("Yes") { dialog, _ ->
            val entry = Entry (
                binding.lblDate.text.toString(), binding.txtEntry.text.toString())
            entryViewModel.delete(entry)
            binding.txtEntry.setText("")
            dialog.cancel()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.cancel()
        }
        val alert = builder.create()
        alert.show()
    }

    private fun undo() {
        val edit = editHistory.getPrevious() ?: return
        val text = binding.txtEntry.editableText
        val start = edit.start
        val end = start + if (edit.after != null) edit.after.length else 0

        skipTextWatch = true
        text.replace(start,end,edit.before)
        skipTextWatch = false

        for (o in text.getSpans(0, text.length, UnderlineSpan::class.java)) {
            text.removeSpan(o)
        }

        binding.txtEntry.setSelection(if(edit.before == null) start else start + edit.before.length)
    }

    private fun redo() {
        val edit = editHistory.getNext() ?: return
        val text = binding.txtEntry.editableText
        val start = edit.start
        val end = start + if (edit.before != null) edit.before.length else 0

        skipTextWatch = true
        text.replace(start,end,edit.after)
        skipTextWatch = false

        for (o in text.getSpans(0, text.length, UnderlineSpan::class.java)) {
            text.removeSpan(o)
        }

        binding.txtEntry.setSelection(if(edit.after == null) start else start + edit.after.length)
    }

    private fun setUndoRedoButtons(showUndo: Boolean, showRedo: Boolean) {
        val redoBtn = binding.materialToolbar2.menu.findItem(R.id.menu_redo)
        val undoBtn = binding.materialToolbar2.menu.findItem(R.id.menu_undo)

        if(showUndo) {
            undoBtn.isEnabled = true
            undoBtn.icon.alpha = 255
        } else {
            undoBtn.isEnabled = false
            undoBtn.icon.alpha = 100
        }

        if(showRedo) {
            redoBtn.isEnabled = true
            redoBtn.icon.alpha = 255
        } else {
            redoBtn.isEnabled = false
            redoBtn.icon.alpha = 100
        }
    }

    fun setUndoButton(enabled: Boolean) {
        binding.materialToolbar2.menu.findItem(R.id.menu_undo).apply {
            isEnabled = enabled
            if(isEnabled) icon.alpha = 255 else icon.alpha = 100
        }
    }

    fun setRedoButton(enabled: Boolean) {
        binding.materialToolbar2.menu.findItem(R.id.menu_redo).apply {
            isEnabled = enabled
            if(isEnabled) icon.alpha = 255 else icon.alpha = 100
        }
    }

    private var textWatcher: CustomTextWatcher = object : CustomTextWatcher() {
        override fun beforeTextChanged(
            s: CharSequence,
            start: Int,
            count: Int,
            after: Int,
        ) {
            if(!skipTextWatch) {
                beforeChange = s.subSequence(start, start+count)
            }
        }

        override fun onTextChanged(
            s: CharSequence,
            start: Int,
            before: Int,
            count: Int,
        ) {
            if(!skipTextWatch) {
                afterChange = s.subSequence(start, start+count)
                editHistory.add(EditHistoryItem(start, beforeChange, afterChange))
            }
        }

        override fun afterTextChanged(p0: Editable?) {
            setUndoRedoButtons(
                editHistory.position > 1,
                editHistory.position < editHistory.history.size
            )
        }
    }

}
