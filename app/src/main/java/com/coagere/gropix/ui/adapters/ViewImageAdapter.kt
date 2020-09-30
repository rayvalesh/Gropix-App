package com.coagere.gropix.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.coagere.gropix.R
import com.coagere.gropix.databinding.AdapterViewImageBinding
import com.coagere.gropix.jetpack.entities.ViewImageModel
import com.coagere.gropix.utils.DownloadImage
import com.tc.utils.utils.customs.RoundedCornersTransformation
import com.tc.utils.variables.abstracts.OnEventOccurListener
import com.tc.utils.variables.interfaces.Constants

/**
 * @author Jatin Sahgal by 14-Sep-2020 11:28
 */
class ViewImageAdapter(
    private val modelList: Array<ViewImageModel>,
    private val listener: OnEventOccurListener
) : RecyclerView.Adapter<ViewImageAdapter.ViewHolder>() {
    var size = modelList.size

    fun notifyAdapterItemChanged(position: Int) {
        notifyItemChanged(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            AdapterViewImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(modelList[position])
    }

    override fun getItemCount(): Int {
        return size
    }

    inner class ViewHolder constructor(private val binding: AdapterViewImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.idImage.setOnClickListener {
                listener.getEventData(adapterPosition)
            }
        }

        /**
         * Loading Image using [HelperDownload] and performing selected View selection
         */
        fun bindTo(model: ViewImageModel) {
            DownloadImage.downloadImages(
                itemView.context,
                model.image,
                binding.idImage,
                R.drawable.placeholder_rounded,
                RoundedCornersTransformation.CornerType.ALL,
                Constants.VALUE_IMAGE_RADIUS
            )
            binding.idImage.isSelected = model.selected
        }
    }
}
