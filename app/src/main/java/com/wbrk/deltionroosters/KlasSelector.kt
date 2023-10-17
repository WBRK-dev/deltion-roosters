package com.wbrk.deltionroosters

import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.wbrk.deltionroosters.api.SharedPref

class KlasSelector(group: String) : DialogFragment() {
    private var selectedGroup = group
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction.
            val builder = MaterialAlertDialogBuilder(it)

            if (selectedGroup !== "none") {
                builder.setSingleChoiceItems(
                    arrayOf("SD1A", "SD1B", "SD1C"), 0
                ) { dialog, which ->
                    builder.setPositiveButton("Opslaan") { dialog, id ->

                    }
                }
            } else {
                builder.setMessage("Voeg klassen toe via instellingen om er snel tussen te kiezen.")
            }

            builder.setTitle("Klas")
                .setNegativeButton("Annuleer") { dialog, id ->

                }
                .setNeutralButton("Zoeken") { dialog, id ->

                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}