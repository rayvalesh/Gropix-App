package com.coagere.gropix.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.coagere.gropix.R
import com.coagere.gropix.jetpack.entities.ItemModel
import com.tc.utils.utils.utility.isNullAndEmpty
import kotlinx.android.synthetic.main.adapter_billing_details.view.*

class BillingDetailsAdapter(
    var modelList: MutableList<ItemModel>
) :
    RecyclerView.Adapter<BillingDetailsAdapter.ViewHolder>() {
    private var size: Int = 0

    init {
        size = this.modelList.size
    }

    fun notifyAdapterDataSetChanged(modelList: MutableList<ItemModel>) {
        if (!isNullAndEmpty(modelList)) {
            this.modelList = modelList
            size = this.modelList.size
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.adapter_billing_details,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(modelList[position], position)
    }

    inner class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal fun bindTo(model: ItemModel, position: Int) {
            itemView.id_text_title.text = model.name
            var items = ""
            var price =0
            for (priceModel in model.priceList) {
                if (priceModel.selectedCount > 0) {
                    items += priceModel.title + ","
                    price += priceModel.price
                }
            }
            itemView.id_text_items.text = items
            itemView.id_text_serial.text = "${position + 1}."
            itemView.id_text_times.text = model.times.toString()
            itemView.id_text_total_amount.text = price.toString()
        }

    }
}
