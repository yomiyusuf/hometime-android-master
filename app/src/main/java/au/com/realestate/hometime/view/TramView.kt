package au.com.realestate.hometime.view

import android.content.Context
import android.text.format.DateUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import au.com.realestate.hometime.R
import au.com.realestate.hometime.models.Tram
import kotlinx.android.synthetic.main.item_tram.view.*
import java.time.LocalDateTime

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

    /**
     * Method to initialize the views of the custom view
     * The countdown textview displays the countdown to the scheduled time in minutes if the if it's less than an hour
     * else it displays the scheduled time
     * @param  Tram object
     */
    fun setView(tram: Tram) {
        txt_item_destination.text = tram.destination
        txt_item_route.text = tram.routeNo

        val scheduledTime = DateUtils.formatDateTime(context, tram.timeSpan(), DateUtils.FORMAT_SHOW_TIME).toString()
        txt_item_scheduled.text = scheduledTime
        if (tram.timeFromNow() < 60 ) {
            txt_item_countdown.text = tram.timeFromNow().toString()
        } else {
            txt_item_countdown.text = scheduledTime
            txt_item_min.visibility = View.GONE
        }
    }
}