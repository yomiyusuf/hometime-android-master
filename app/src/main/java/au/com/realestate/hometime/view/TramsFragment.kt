package au.com.realestate.hometime.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager

import au.com.realestate.hometime.R
import au.com.realestate.hometime.view.adapter.TramListAdapter
import au.com.realestate.hometime.viewmodel.TrackerViewModel
import kotlinx.android.synthetic.main.fragment_trams.*

/**
 * A simple fragment that uses a [Recyclerview] to display Trams for the provided stopId
 */
class TramsFragment : Fragment() {

    private lateinit var viewModel: TrackerViewModel
    private val listAdapter = TramListAdapter(arrayListOf())

    private var stopId: String? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trams, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_trams.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
        }

        stopId = arguments?.getString(ARG_STOP_ID)
        viewModel = ViewModelProviders.of(activity!!).get(TrackerViewModel::class.java)
        registerObservers()
    }

    private fun registerObservers() {
        viewModel.trams.observe(this, Observer { list ->
            Log.e(stopId, list.size.toString())
            val trams = list.find { it.sopId == stopId }
            trams?.run{
                listAdapter.updateData(this.trams)
            }
        })
    }

    companion object {
        private const val ARG_STOP_ID = "stop"

        fun newInstance(stopId: String) = TramsFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_STOP_ID, stopId)
            }
        }
    }

}
