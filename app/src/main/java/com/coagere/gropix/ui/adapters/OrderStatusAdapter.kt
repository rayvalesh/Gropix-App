package com.coagere.gropix.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.coagere.gropix.databinding.AdapterOrderPlacedBinding
import com.coagere.gropix.databinding.AdapterOrdersCancelledBinding
import com.coagere.gropix.databinding.AdapterOrdersPendingBinding
import com.coagere.gropix.jetpack.entities.OrderModel
import com.tc.utils.variables.abstracts.OnEventOccurListener
import com.tc.utils.variables.enums.ActionType
import com.tc.utils.variables.interfaces.Constants

class OrderStatusAdapter(
    private val modelList: ArrayList<OrderModel>,
    private val listener: OnEventOccurListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var size = modelList.size

    fun notifyAdapterDataSetChanged() {
        size = this.modelList.size
        notifyDataSetChanged()
    }

    fun notifyAdapterItemRangeChanged() {
        val lastSize = size
        size = modelList.size
        notifyItemRangeChanged(lastSize, size - lastSize)
    }

    override fun getItemViewType(position: Int): Int {
        return modelList[position].status
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        when (viewType) {
            Constants.MODULE_CANCELLED -> {
                return CancelViewHolder(
                    AdapterOrdersCancelledBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            Constants.MODULE_PLACED -> {
                return PlacedViewHolder(
                    AdapterOrderPlacedBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                return ViewHolder(
                    AdapterOrdersPendingBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) holder.bindTo(modelList[position])
        if (holder is PlacedViewHolder) holder.bindTo(modelList[position])
        else if (holder is CancelViewHolder) {
            holder.bindTo(modelList[position])
        }
    }

    override fun getItemCount(): Int {
        return size
    }

    inner class ViewHolder(private var binding: AdapterOrdersPendingBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        fun bindTo(orderModel: OrderModel) {
            binding.apply {
                clickListener = this@ViewHolder
                model = orderModel
                executePendingBindings()
            }
        }

        override fun onClick(v: View) {
            listener.getEventData(
                modelList[adapterPosition],
                ActionType.ACTION_EXPLORE,
                adapterPosition
            )
        }
    }

    inner class PlacedViewHolder(private var binding: AdapterOrderPlacedBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        fun bindTo(orderModel: OrderModel) {
            binding.apply {
                this.clickListener = this@PlacedViewHolder
                model = orderModel
                executePendingBindings()
            }

        }

        override fun onClick(v: View) {
            listener.getEventData(
                modelList[adapterPosition],
                ActionType.ACTION_EXPLORE,
                adapterPosition
            )
        }

    }

    inner class CancelViewHolder(private var binding: AdapterOrdersCancelledBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        fun bindTo(orderModel: OrderModel) {
            binding.apply {
                this.clickListener = this@CancelViewHolder
                model = orderModel
                executePendingBindings()
            }

        }

        override fun onClick(v: View) {
            listener.getEventData(
                modelList[adapterPosition],
                ActionType.ACTION_EXPLORE,
                adapterPosition
            )
        }

    }

}
