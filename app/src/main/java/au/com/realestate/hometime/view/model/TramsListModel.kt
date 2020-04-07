package au.com.realestate.hometime.view.model

import au.com.realestate.hometime.models.Tram
import au.com.realestate.hometime.util.Constants

data class TramsListModel (val sopId: String, var trams: List<Tram>) {

    fun belowCountdownThreshold(): Boolean {
        return trams.any { it.timeFromNow() < Constants.COUNTDOWN_THRESHOLD }
    }
}