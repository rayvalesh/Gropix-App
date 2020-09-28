package com.coagere.gropix.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.coagere.gropix.databinding.AdapterOrdersCancelledBinding
import com.coagere.gropix.databinding.AdapterOrdersPendingBinding
import com.coagere.gropix.jetpack.entities.OrderModel
import com.tc.utils.variables.abstracts.OnEventOccurListener
import com.tc.utils.variables.interfaces.Constants

class OrderStatusAdapter(
    private val moduleType: Int,
    private val modelList: ArrayList<OrderModel>,
    private val listener: OnEventOccurListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var size = modelList.size


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        if (moduleType == Constants.MODULE_PENDING) {
            return ViewHolder(
                AdapterOrdersPendingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            return CancelViewHolder(
                AdapterOrdersCancelledBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    }

    override fun getItemCount(): Int {
        return size
    }

    inner class ViewHolder(binding: AdapterOrdersPendingBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    inner class CancelViewHolder(binding: AdapterOrdersCancelledBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

}
