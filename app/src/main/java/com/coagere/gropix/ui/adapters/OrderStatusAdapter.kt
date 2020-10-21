package com.coagere.gropix.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.coagere.gropix.R
import com.coagere.gropix.databinding.AdapterOrderPlacedBinding
import com.coagere.gropix.databinding.AdapterOrdersCancelledBinding
import com.coagere.gropix.databinding.AdapterOrdersPendingBinding
import com.coagere.gropix.jetpack.entities.OrderModel
import com.tc.utils.utils.helpers.Utils
import com.tc.utils.variables.abstracts.OnEventOccurListener
import com.tc.utils.variables.enums.ActionType
import com.tc.utils.variables.interfaces.Constants
import tk.jamun.ui.snacks.L

class OrderStatusAdapter(
    private val modelList: ArrayList<OrderModel>,
    private val listener: OnEventOccurListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var size = modelList.size

    fun notifyAdapterDataSetChanged() {
        size = modelList.size
        notifyDataSetChanged()
    }

    fun notifyAdapterItemChange(position: Int) {
        notifyItemChanged(position)
    }

    override fun getItemViewType(position: Int): Int {
        return modelList[position].status
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        when (viewType) {
            Constants.ORDER_CANCELLED -> {
                return CancelViewHolder(
                    AdapterOrdersCancelledBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            Constants.ORDER_COMPLETE -> {
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

    override fun getItemCount(): Int {
        return size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) holder.bindTo(modelList[position], position)
        if (holder is PlacedViewHolder) holder.bindTo(modelList[position], position)
        else if (holder is CancelViewHolder) {
            holder.bindTo(modelList[position], position)
        }
    }


    inner class ViewHolder(private var binding: AdapterOrdersPendingBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        fun bindTo(orderModel: OrderModel, position: Int) {
            binding.apply {
                clickListener = this@ViewHolder
                model = orderModel
                executePendingBindings()
            }
            when (orderModel.status) {
                Constants.ORDER_CART -> {
                    Utils.setVisibility(binding.idParentAmount, false)
                    binding.idTextStatus.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.colorStyleSixDark
                        )
                    )
                    binding.idTextStatus.text =
                        itemView.context.getString(R.string.string_label_status_cart)
                }
                Constants.ORDER_PENDING -> {
                    Utils.setVisibility(binding.idParentAmount, false)
                    binding.idTextStatus.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.colorStyleFourDark
                        )
                    )
                    binding.idTextStatus.text =
                        itemView.context.getString(R.string.string_label_status_placed)
                }
                Constants.ORDER_CONFIRMED -> {
                    Utils.setVisibility(binding.idParentAmount, true)
                    binding.idTextStatus.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.colorStyleThreeDark
                        )
                    )
                    binding.idTextStatus.text =
                        itemView.context.getString(R.string.string_label_status_confirmed)
                }
                Constants.ORDER_OUT_FOR_DELIVERY -> {
                    Utils.setVisibility(binding.idParentAmount, true)
                    binding.idTextStatus.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.colorStyleThreeDark
                        )
                    )
                    binding.idTextStatus.text =
                        itemView.context.getString(R.string.string_label_status_out_delivery)
                }
            }
            Utils.setVisibility(binding.idViewBottom, position == size - 1)
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

        fun bindTo(orderModel: OrderModel, position: Int) {
            binding.apply {
                this.clickListener = this@PlacedViewHolder
                model = orderModel
                executePendingBindings()
            }
            Utils.setVisibility(binding.idViewBottom, position == size - 1)

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

        fun bindTo(orderModel: OrderModel, position: Int) {
            binding.apply {
                this.clickListener = this@CancelViewHolder
                model = orderModel
                executePendingBindings()
            }
            Utils.setVisibility(binding.idViewBottom, position == size - 1)

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
