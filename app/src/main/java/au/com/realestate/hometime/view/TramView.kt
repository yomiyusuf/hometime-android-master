package au.com.realestate.hometime.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import au.com.realestate.hometime.R
import au.com.realestate.hometime.models.Tram
import kotlinx.android.synthetic.main.item_tram.view.*

/**
 * Custom view to encapsulate Tram view
 */
class TramView: ConstraintLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet):    super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?,    defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        LayoutInflater.from(context)
                .inflate(R.layout.item_tram, this, true)
    }

    fun setView(tram: Tram) {
        txt_item_destination.text = tram.destination
        txt_item_route.text = tram.routeNo
        txt_item_scheduled.text = tram.readablePredictedArrival().toString()
    }
}