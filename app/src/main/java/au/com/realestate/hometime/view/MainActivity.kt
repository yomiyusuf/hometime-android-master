package au.com.realestate.hometime.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import au.com.realestate.hometime.R
import au.com.realestate.hometime.viewmodel.TrackerViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.util.Date

class MainActivity : AppCompatActivity() {

    private val viewModel:  TrackerViewModel by viewModels()
    private val listAdapter = TramListAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rv_trams.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
        }

        registerObservers()
    }

    private fun registerObservers() {
        viewModel.trams.observe(this, Observer { trams ->
            Log.e("MainActivity", trams.toString())
            listAdapter.updateData(trams)
        })

        viewModel.loading.observe(this, Observer {  })

        viewModel.loadingError.observe(this, Observer {  })
    }

    override fun onResume() {
        super.onResume()
        //Refresh list each time the activity resumes
        viewModel.refresh(listOf("4055", "4155"))
    }

    private fun showError() {

    }

    fun refreshClick(view: View) {

    }
}
