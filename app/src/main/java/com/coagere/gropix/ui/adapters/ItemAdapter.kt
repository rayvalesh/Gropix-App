package com.coagere.gropix.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.coagere.gropix.R
import com.coagere.gropix.jetpack.entities.ItemModel
import com.coagere.gropix.utils.DownloadImage
import com.tc.utils.utils.utility.isNull
import com.tc.utils.variables.abstracts.OnEventOccurListener
import com.tc.utils.variables.enums.ActionType
import com.tc.utils.variables.interfaces.Constants
import com.tc.utils.variables.interfaces.Constants.Companion.MODULE_CART

class ItemAdapter(
    private val moduleType: Int = MODULE_CART,
    private val listener: OnEventOccurListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var modelList: MutableList<ItemModel> = mutableListOf()
    private var size: Int = 0
    private var parentPosition: Int = -1

    fun setParentPosition(parentPosition: Int) {
        this.parentPosition = parentPosition
    }

    fun notifyAdapterItemRemoved(index: Int) {
        size -= 1
        modelList.removeAt(index)
        notifyItemRemoved(index)
    }

    fun notifyAdapterItemChanged(model: ItemModel) {
        if (model.adapter != null) {
            model.adapter?.bindTo(model, 0)
        } else {
            val index = modelList.indexOf(model)
            if (index != -1) {
                notifyItemChanged(index)
            }
        }
    }

    fun notifyAdapterDataSetChanged(modelList: MutableList<ItemModel>) {
        size = if (isNull(modelList)) {
            this.modelList.clear()
            0
        } else {
            this.modelList.clear()
            this.modelList.addAll(modelList)
            this.modelList.size
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (moduleType == Constants.MODULE_CART) {
            CartViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.adapter_item, parent, false
                )
            )
        } else
            ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.adapter_child_item, parent, false)
            )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            val model = modelList[position]
            model.adapter = holder
            holder.bindTo(model, position)
        } else if (holder is CartViewHolder)
            holder.bindTo(modelList[position], position)
    }

    override fun getItemCount(): Int {
        return size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private val titleTextView: TextView = itemView.findViewById(R.id.id_text_title)
        private val priceTextView: TextView = itemView.findViewById(R.id.id_text_price)
        private val selectedItemTextView: TextView =
            itemView.findViewById(R.id.id_text_selected_items)
        private val quantityTextView: TextView = itemView.findViewById(R.id.id_text_quantity)
        private val imageView = itemView.findViewById<ImageView>(R.id.id_image)

        init {
            imageView.setOnClickListener(this)
            setUpCommonThings(itemView, this)
        }

        internal fun bindTo(model: ItemModel, position: Int) {
            titleTextView.text = model.name
            DownloadImage.downloadImageByUrl(
                itemView.context,
                model.image,
                imageView,
                R.drawable.placeholder_a
            )
            imageView.contentDescription = model.name
            priceTextView.text = model.priceList[0].price.toString()
            quantityTextView.text = "/${model.priceList[0].title}"
            if (model.times > 0) {
                selectedItemTextView.text = "${model.times}"
                showVisibility(itemView)
            } else {
                hideVisibility(itemView)
            }
            if (position == size - 1) {
                itemView.findViewById<View>(R.id.id_view).visibility = View.GONE
            } else {
                itemView.findViewById<View>(R.id.id_view).visibility = View.VISIBLE
            }
        }

        override fun onClick(v: View) {
            commonClickListener(v.id, adapterPosition, itemView)
        }
    }

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private val titleTextView: TextView = itemView.findViewById(R.id.id_text_title)
        private val priceTextView: TextView = itemView.findViewById(R.id.id_text_amount)
        private val selectedItemTextView: TextView =
            itemView.findViewById(R.id.id_text_selected_items)

        init {
            itemView.findViewById<AppCompatImageView>(R.id.id_button_remove).visibility =
                View.VISIBLE
            itemView.findViewById<AppCompatImageView>(R.id.id_button_remove)
                .setOnClickListener(this)
            setUpCommonThings(itemView, this)
        }

        internal fun bindTo(model: ItemModel, position: Int) {
            titleTextView.text = model.name
            priceTextView.visibility = View.VISIBLE
            var totalAmount = 0
            for (priceModel in model.priceList) {
                if (priceModel.selectedCount > 0)
                    totalAmount += (priceModel.price * priceModel.selectedCount)
            }
            priceTextView.text = totalAmount.toString()
            itemView.findViewById<ImageView>(R.id.id_image_dot).visibility = View.VISIBLE
            itemView.findViewById<TextView>(R.id.id_text_category).text = model.categoryName
            itemView.findViewById<TextView>(R.id.id_text_category_title).visibility = View.VISIBLE
            itemView.findViewById<TextView>(R.id.id_text_category).visibility = View.VISIBLE
            if (model.times > 0) {
                selectedItemTextView.text = "${model.times}"
                showVisibility(itemView)
            } else {
                hideVisibility(itemView)
            }
            if (position == size - 1) {
                itemView.findViewById<View>(R.id.id_view).visibility = View.GONE
            } else {
                itemView.findViewById<View>(R.id.id_view).visibility = View.VISIBLE
            }
        }


        override fun onClick(v: View) {
            commonClickListener(v.id, adapterPosition, itemView)
        }
    }

    private fun commonClickListener(id: Int, adapterPosition: Int, itemView: View) {
        val model = modelList[adapterPosition]
        when (id) {
            R.id.id_image -> listener.getEventData(
                model,
                ActionType.ACTION_EXPLORE_IMAGE,
                adapterPosition
            )
            R.id.id_button_remove ->
                listener.getEventData(model, ActionType.ACTION_CANCEL, adapterPosition)
            R.id.id_button_add, R.id.id_button_add_more -> {
                if (model.priceList.size == 1) {
                    model.priceList[0].selectedCount = model.priceList[0].selectedCount.plus(1)
                    itemView.findViewById<TextView>(R.id.id_text_selected_items).text =
                        "${model.priceList[0].selectedCount}"
                    showVisibility(itemView)
                }
                if (parentPosition == -1) {
                    listener.getEventData(model, ActionType.ACTION_ADD, adapterPosition)
                } else {
                    listener.getEventData(model, ActionType.ACTION_ADD, parentPosition)
                }
            }
            else -> {
                if (model.priceList.size == 1) {
                    model.priceList[0].selectedCount = model.priceList[0].selectedCount.minus(1)
                    if (model.priceList[0].selectedCount > 0) {
                        itemView.findViewById<TextView>(R.id.id_text_selected_items).text =
                            "${model.priceList[0].selectedCount}"
                    } else {
                        hideVisibility(itemView)
                    }
                }
                if (parentPosition == -1) {
                    listener.getEventData(model, ActionType.ACTION_SUBTRACT, adapterPosition)
                } else {
                    listener.getEventData(model, ActionType.ACTION_SUBTRACT, parentPosition)
                }
            }
        }

    }

    private fun setUpCommonThings(itemView: View, onViewClick: View.OnClickListener) {
        itemView.findViewById<LinearLayout>(R.id.id_parent_selected).setOnClickListener(onViewClick)
        itemView.findViewById<LinearLayout>(R.id.id_button_add).setOnClickListener(onViewClick)
        itemView.findViewById<ImageButton>(R.id.id_button_add_more).setOnClickListener(onViewClick)
        itemView.findViewById<ImageButton>(R.id.id_button_sub).setOnClickListener(onViewClick)

    }

    private fun hideVisibility(itemView: View) {
        itemView.findViewById<LinearLayout>(R.id.id_parent_selected).visibility = View.GONE
        itemView.findViewById<LinearLayout>(R.id.id_button_add).visibility = View.VISIBLE
    }

    private fun showVisibility(itemView: View) {
        itemView.findViewById<LinearLayout>(R.id.id_parent_selected).visibility = View.VISIBLE
        itemView.findViewById<LinearLayout>(R.id.id_button_add).visibility = View.GONE
    }
}
