package com.coagere.gropix.ui.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tc.utils.variables.abstracts.OnEventOccurListener

class OrderStatusAdapter(modulePending: Int, listener: OnEventOccurListener) :
    RecyclerView.Adapter<OrderStatusAdapter.ViewHolder>() {

    var size = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderStatusAdapter.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: OrderStatusAdapter.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    inner class ViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

    }

}
