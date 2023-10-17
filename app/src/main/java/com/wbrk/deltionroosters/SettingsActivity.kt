package com.wbrk.deltionroosters

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.wbrk.deltionroosters.api.SharedPref

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val sharedPref = SharedPref(this)

        findViewById<EditText>(R.id.customapiedit).setText(
            sharedPref.read("customapi", "").toString()
        )

        findViewById<Button>(R.id.save).setOnClickListener {
            Log.d("test", findViewById<EditText>(R.id.customapiedit).text.toString())
            if (findViewById<EditText>(R.id.customapiedit).text.toString().isNotEmpty()) {
                DangerDialog(sharedPref, this).show(supportFragmentManager, "DANGER_SAVE_DIALOG")
            } else {
                sharedPref.write("customapi", findViewById<EditText>(R.id.customapiedit).text.toString())
                finish()
            }
        }
    }
}

class DangerDialog(sharedPref: SharedPref, app: AppCompatActivity) : DialogFragment() {
    private val sharedPref = sharedPref
    private val app = app
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction.
            val builder = MaterialAlertDialogBuilder(it)
            builder.setTitle("Danger")
            builder.setMessage("Het opslaan van custom api location kan voor problemen zorgen.")
                .setPositiveButton("Opslaan") { dialog, id ->
                    sharedPref.write("customapi", app.findViewById<EditText>(R.id.customapiedit).text.toString())
                    app.finish()
                }
                .setNegativeButton("Annuleer") { dialog, id ->
                    // User cancelled the dialog.
                }
            // Create the AlertDialog object and return it.
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
