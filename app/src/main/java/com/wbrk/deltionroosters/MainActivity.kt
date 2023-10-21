package com.wbrk.deltionroosters

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wbrk.deltionroosters.api.SharedPref
import com.wbrk.deltionroosters.api.api
import com.wbrk.deltionroosters.api.apiInterface
import com.wbrk.deltionroosters.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var day: Int = 0
    private var fetching: Boolean = false

    private var dayFetchDelay: Int = 0
    private var fetchDelayed: Boolean = false

    private var url: String = ""
    private var group: String = "none"

    private var timesList = mutableListOf<String>()
    private var titlesList = mutableListOf<String>()
    private var locationsList = mutableListOf<String>()

    private val recyclerAdapter = MainRecyclerAdapter(timesList, titlesList, locationsList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(binding.toolbar)

        val sharedPref = SharedPref(this)
        url = sharedPref.read("customapi", "").toString()
        group = sharedPref.read("selectedGroup", "none").toString()

        val recyclerView: RecyclerView = findViewById(R.id.list_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = recyclerAdapter

        val file = File(filesDir, "selectedGroups")
        file.createNewFile()
        file.writeBytes("['SD1A', 'SD1B', 'SD1C']".toByteArray())

        if (group !== "none") {
            renderDayRoster(group, day)
        } else {
            val gson = Gson()
            val type = object : TypeToken<List<String?>?>() {}.type
            val file = File(filesDir, "selectedGroups")
            file.createNewFile()
            val selectedGroupsString = file.readBytes().toString(Charset.defaultCharset())
            var selectedGroups: List<String> = mutableListOf()
            if (selectedGroupsString.isNotEmpty()) { selectedGroups = gson.fromJson(selectedGroupsString, type) }
            val klasSelector = KlasSelector("test3", selectedGroups, this)
            klasSelector.show(supportFragmentManager, "KLAS_SELECT_DIALOG")
            klasSelector.setOnDismissFunction { group = sharedPref.read("selectedGroup", "none").toString(); renderDayRoster(group, day) }
        }

        findViewById<Button>(R.id.next_button).setOnClickListener {
            if (group === "none") {
                dayFetchDelay = 0
                fetchDelayed = false
                fetching = false
            } else if (!fetching) {
                day += 1
                renderDayRoster(group, day)
            } else {
                dayFetchDelay += 1
                fetchDelayed = true
            }
        }
        findViewById<Button>(R.id.prev_button).setOnClickListener {
            if (group === "none") {
                dayFetchDelay = 0
                fetchDelayed = false
                fetching = false
            } else if (!fetching) {
                day -= 1
                renderDayRoster(group, day)
            } else {
                dayFetchDelay -= 1
                fetchDelayed = true
            }
        }
    }

    fun renderDayRoster(group: String, dayToFetch: Int) {
        fetching = true
        timesList.clear()
        titlesList.clear()
        locationsList.clear()
        notifyDataChange()
        runOnUiThread { findViewById<ProgressBar>(R.id.progress).visibility = VISIBLE }
        val api = api.getInstance(url).create(apiInterface::class.java)
        GlobalScope.launch {
            val result = api.getRosterDay(group, dayToFetch)
            if (result.isSuccessful) {
                for (day in result.body()!!.data) {
                    for (leshour in day.items) {
                        timesList.add(leshour.t)
                        titlesList.add(leshour.v)
                        locationsList.add(leshour.r)
                    }
                }
                runOnUiThread {
                    findViewById<TextView>(R.id.week).text = "Week ${result.body()!!.data[0].weeknum}"
                    findViewById<TextView>(R.id.date).text = result.body()!!.data[0].date_f
                    findViewById<TextView>(R.id.group).text = result.body()!!.group
                    findViewById<ProgressBar>(R.id.progress).visibility = GONE
                }
                notifyDataChange()
                if (fetchDelayed) {
                    day += dayFetchDelay
                    dayFetchDelay = 0
                    fetchDelayed = false
                    renderDayRoster(group, day)
                } else {
                    fetching = false
                }
            } else {
                fetching = false
                fetchDelayed = false
                dayFetchDelay = 0
            }
        }
    }

    private fun notifyDataChange() {
        runOnUiThread {
            recyclerAdapter.notifyDataSetChanged()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val sharedPref = SharedPref(this)
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_today -> {
                if (!fetching && day != 0 && group !== "none") {
                    day = 0
                    renderDayRoster(group, day)
                }
                return true
            }
            R.id.action_group -> {
                val gson = Gson()
                val type = object : TypeToken<List<String?>?>() {}.type
                val file = File(filesDir, "selectedGroups")
                file.createNewFile()
                val selectedGroupsString = file.readBytes().toString(Charset.defaultCharset())
                var selectedGroups: List<String> = mutableListOf()
                if (selectedGroupsString.isNotEmpty()) { selectedGroups = gson.fromJson(selectedGroupsString, type) }
                val klasSelector = KlasSelector(group, selectedGroups, this)
                klasSelector.show(supportFragmentManager, "KLAS_SELECT_DIALOG")
                klasSelector.setOnDismissFunction { group = sharedPref.read("selectedGroup", "none").toString(); renderDayRoster(group, day) }
                true
            }
            R.id.action_settings -> {
                startActivity(
                    Intent(this, SettingsActivity::class.java))
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}