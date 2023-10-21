package com.wbrk.deltionroosters

import com.wbrk.deltionroosters.R
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.wbrk.deltionroosters.api.Groups
import com.wbrk.deltionroosters.api.SharedPref
import com.wbrk.deltionroosters.api.api
import com.wbrk.deltionroosters.api.apiInterface
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.nio.charset.Charset


class KlasActivity : AppCompatActivity() {

    private var klasList = mutableListOf<String>()
    private var klasSelected = mutableListOf<Boolean>()

    private val recyclerAdapter = KlasRecyclerAdapter(klasList, klasSelected)

    private var url: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.group_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val recyclerView: RecyclerView = findViewById(R.id.groupRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = recyclerAdapter

        val sharedPref = SharedPref(this)
        url = sharedPref.read("customapi", "").toString()

        renderGroups()
    }

    private fun renderGroups() {
        val gson = Gson()
        var groupsFromJson = Groups(arrayListOf())
        var gottenObject = false

        var file = File(filesDir, "groups.json")
        file.createNewFile()

        var groups = file.readBytes().toString(Charset.defaultCharset())

        runOnUiThread { findViewById<ProgressBar>(R.id.progress).visibility = View.VISIBLE }

        GlobalScope.launch {
            if (groups.isNullOrEmpty()) {
                val api = api.getInstance(url).create(apiInterface::class.java)
                val result = api.getGroups()
                if (result.isSuccessful) {
                    file.writeBytes(gson.toJson(result.body()).toByteArray())
                    groupsFromJson = result.body()!!
                    gottenObject = true
                }
            } else {
                groupsFromJson = gson.fromJson(groups, Groups::class.java)
                gottenObject = true
            }
            if (gottenObject) {
                Log.d("test", groupsFromJson.toString())
                for (group in groupsFromJson.data) {
                    klasList.add(group)
                    klasSelected.add(false)
                }
            }

            runOnUiThread { findViewById<ProgressBar>(R.id.progress).visibility = View.GONE
            recyclerAdapter.notifyDataSetChanged() }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_groups, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val file = File(filesDir, "groups.json")
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_today -> {
                file.writeBytes("".toByteArray())
                klasList.clear()
                klasSelected.clear()
                runOnUiThread { recyclerAdapter.notifyDataSetChanged() }
                renderGroups()
                return super.onOptionsItemSelected(item)
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}