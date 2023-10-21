package com.wbrk.deltionroosters

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.wbrk.deltionroosters.api.SharedPref

class KlasSelector(group: String, groups: List<String>, activity: AppCompatActivity) : DialogFragment() {
    private var selectedGroup = group
    private var savedGroups = groups
    private val sharedPref = SharedPref(activity)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction.
            val builder = MaterialAlertDialogBuilder(it)
            var selectedGroupFromOptions = if (selectedGroup !== "none") { savedGroups.indexOf(selectedGroup) } else { 0 }

            if (savedGroups.isNotEmpty()) {
                val index: Int = if (selectedGroup !== "none") { savedGroups.indexOf(selectedGroup) } else { -1 }
                builder.setSingleChoiceItems(savedGroups.toTypedArray(), index) { dialog, which -> selectedGroupFromOptions = which }
                builder.setPositiveButton("Opslaan") { dialog, id -> sharedPref.write("selectedGroup", savedGroups[selectedGroupFromOptions]); onDismissFunction() }
            } else {
                builder.setMessage("Voeg klassen toe via de knop zoeken om er snel tussen te kiezen.")
            }

            builder.setTitle("Klas")
                .setNegativeButton("Annuleer") { dialog, id -> }
                .setNeutralButton("Zoeken") { dialog, id -> startActivity(
                    Intent(activity, KlasActivity::class.java)
                )}
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private var onDismissFunction: () -> Unit = {}

    fun setOnDismissFunction(block: () -> Unit){
        onDismissFunction = block
    }
}