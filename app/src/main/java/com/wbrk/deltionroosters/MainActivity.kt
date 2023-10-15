package com.wbrk.deltionroosters

import android.os.Bundle
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
import com.wbrk.deltionroosters.api.api
import com.wbrk.deltionroosters.api.apiInterface
import com.wbrk.deltionroosters.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var day: Int = 0
    private var fetching: Boolean = false

    private var timesList = mutableListOf<String>()
    private var titlesList = mutableListOf<String>()
    private var locationsList = mutableListOf<String>()

    private val recyclerAdapter = RecyclerAdapter(timesList, titlesList, locationsList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(findViewById(R.id.toolbar))

        val recyclerView: RecyclerView = findViewById(R.id.list_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = recyclerAdapter

        renderDayRoster(day)

        findViewById<Button>(R.id.next_button).setOnClickListener {
            if (!fetching) {
                day += 1
                renderDayRoster(day)
            }
        }
        findViewById<Button>(R.id.prev_button).setOnClickListener {
            if (!fetching) {
                day -= 1
                renderDayRoster(day)
            }
        }
    }

    fun renderDayRoster(day: Int) {
        fetching = true
        timesList.clear()
        titlesList.clear()
        locationsList.clear()
        notifyDataChange()
        runOnUiThread { findViewById<ProgressBar>(R.id.progress).visibility = VISIBLE }
        val api = api.getInstance().create(apiInterface::class.java)
        GlobalScope.launch {
            val result = api.getRosterDay("SD1A", day)
            if (result.isSuccessful) {
                for (day in result.body()!!.data) {
                    for (leshour in day.items) {
                        timesList.add(leshour.t)
                        titlesList.add(leshour.v)
                        locationsList.add(leshour.r)
                    }
                }
            }
            runOnUiThread {
                findViewById<TextView>(R.id.week).text = "Week ${result.body()!!.data[0].weeknum}"
                findViewById<TextView>(R.id.date).text = result.body()!!.data[0].date_f
                findViewById<TextView>(R.id.group).text = result.body()!!.group
                findViewById<ProgressBar>(R.id.progress).visibility = GONE
            }
            notifyDataChange()
            fetching = false
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_today -> {
                if (!fetching && day != 0) {
                    day = 0
                    renderDayRoster(day)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}