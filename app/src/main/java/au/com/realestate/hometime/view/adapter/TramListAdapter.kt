package au.com.realestate.hometime.view.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import au.com.realestate.hometime.models.Tram
import au.com.realestate.hometime.view.TramView


class TramListAdapter(private val tramList: ArrayList<Tram>):
        RecyclerView.Adapter<TramListAdapter.TramViewHolder>() {

    fun updateData(newTramList: List<Tram>) {
        tramList.clear()
        tramList.addAll(newTramList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TramViewHolder {
        val tramView = TramView(parent.context)
        tramView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return TramViewHolder(tramView)
    }

    override fun onBindViewHolder(holder: TramViewHolder, position: Int) {
        holder.bind(tramList[position])
    }

    override fun getItemCount() = tramList.size

    class TramViewHolder(var view: View): RecyclerView.ViewHolder(view) {
        private val tramView: TramView = view as TramView

        fun bind(item: Tram) {
            tramView.setView(item)
        }
    }
}