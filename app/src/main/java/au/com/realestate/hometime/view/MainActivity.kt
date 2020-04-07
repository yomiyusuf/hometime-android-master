package au.com.realestate.hometime.view

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import au.com.realestate.hometime.R
import au.com.realestate.hometime.util.InternetUtil
import au.com.realestate.hometime.view.adapter.TramsPagerAdapter
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

    private val stops = listOf(Pair("North", "4055"), Pair("South", "4155"))

    private var snackBar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        InternetUtil.init(application)//move this to custom application class in larger app
        bindProgressButton(btn_refresh)

        setupViewPager()
        registerObservers()
        btn_refresh.setOnClickListener { refresh(true) }
    }

    private fun setupViewPager() {
        val pagerAdapter = TramsPagerAdapter(supportFragmentManager)
        stops.forEach {
            pagerAdapter.addFragment(TramsFragment.newInstance(it.second), it.first)
        }
        viewpager.adapter = pagerAdapter
        view_pager_tab.setViewPager(viewpager)
    }

    /**
     * Register observers for global states: Loading, Error and network status
     */
    private fun registerObservers() {

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
        //Refresh list each time the activity resumes to avoid stale data
        refresh()
    }

    /**
     * Resolve the correct error message
     * @param type See ErrorType
     */
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

    private fun refresh(isHardRefresh: Boolean = false) {
        val stopIds = stops.map { it.second }
       if (isHardRefresh) viewModel.hardRefresh(stopIds) else viewModel.refresh(stopIds)
    }
}
