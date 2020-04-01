package au.com.realestate.hometime

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import au.com.realestate.hometime.models.Tram

import java.util.ArrayList
import java.util.Date

class MainActivity : AppCompatActivity() {

    private var southTrams: List<Tram>? = null
    private var northTrams: List<Tram>? = null

    private var northListView: ListView? = null
    private var southListView: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        northListView = findViewById<View>(R.id.northListView) as ListView
        southListView = findViewById<View>(R.id.southListView) as ListView
    }

    fun refreshClick(view: View) {

    }

    fun clearClick(view: View) {
        northTrams = ArrayList()
        southTrams = ArrayList()
        showTrams()
    }

    private fun showTrams() {

        val northValues = ArrayList<String>()
        val southValues = ArrayList<String>()

        for (tram in northTrams!!) {
            val date = dateFromDotNetDate(tram.predictedArrival).toString()
            northValues.add(date)
        }

        for (tram in southTrams!!) {
            val date = dateFromDotNetDate(tram.predictedArrival).toString()
            southValues.add(date)
        }

        northListView!!.adapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                northValues)

        southListView!!.adapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                southValues)
    }

    /////////////
    // Convert .NET Date to Date
    ////////////
    private fun dateFromDotNetDate(dotNetDate: String): Date {

        val startIndex = dotNetDate.indexOf("(") + 1
        val endIndex = dotNetDate.indexOf("+")
        val date = dotNetDate.substring(startIndex, endIndex)

        val unixTime = java.lang.Long.parseLong(date)
        return Date(unixTime)
    }

}
