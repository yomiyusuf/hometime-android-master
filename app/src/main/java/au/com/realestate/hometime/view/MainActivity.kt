package au.com.realestate.hometime.view

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import au.com.realestate.hometime.R
import au.com.realestate.hometime.util.InternetUtil
import au.com.realestate.hometime.view.model.ErrorType
import au.com.realestate.hometime.viewmodel.TrackerViewModel
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.app_content.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val viewModel:  TrackerViewModel by viewModels()
    private val listAdapter = TramListAdapter(arrayListOf())

    private val stops = listOf("4055", "4155")

    private var snackBar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        InternetUtil.init(application)//move this to custom application class in larger app
        bindProgressButton(btn_refresh)

        rv_trams.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
        }

        registerObservers()
        btn_refresh.setOnClickListener { refresh() }
    }

    private fun registerObservers() {
        viewModel.trams.observe(this, Observer { trams ->
            Log.e("MainActivity", trams.toString())
            listAdapter.updateData(trams)
        })

        viewModel.loading.observe(this, Observer { loading ->
            if (loading) {
                btn_refresh.showProgress {
                    progressColor = Color.WHITE
                    buttonText = getString(R.string.refresh)
                }
            } else {
                btn_refresh.hideProgress(R.string.refresh)
            }
        })

        viewModel.loadingError.observe(this, Observer { errorStatus->
            if (errorStatus.error) showError(errorStatus.type!!) else dismissError()
        })

        InternetUtil.observe(this, Observer { status ->
            if (status) {
                refresh()
            } else {
                showError(ErrorType.NetworkError)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        //Refresh list each time the activity resumes
        refresh()
    }

    private fun showError(type: ErrorType) {
        val msg = when (type){
            ErrorType.NetworkError -> getString(R.string.network_error)
            ErrorType.ServerError -> getString(R.string.server_error)
        }
        snackBar = Snackbar.make(coordinatorLayout,msg, Snackbar.LENGTH_INDEFINITE)
        snackBar?.show()
    }

    private fun dismissError() {
        snackBar?.dismiss()
    }

    private fun refresh() {
        viewModel.refresh(stops)
    }
}
