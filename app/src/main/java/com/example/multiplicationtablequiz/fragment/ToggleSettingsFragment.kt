package com.example.multiplicationtablequiz.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.multiplicationtablequiz.R


class ToggleSettingsFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            builder.
                    setView(inflater.inflate(R.layout.fragment_toggle_dialog, null)).
                    setNegativeButton(R.string.cancel) { dialog, id -> dialog.cancel() }.create()
        } ?: throw IllegalStateException("Activity cannot be null")

    }
}
