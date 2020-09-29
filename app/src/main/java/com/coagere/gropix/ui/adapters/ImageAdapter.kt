package com.coagere.gropix.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.coagere.gropix.R
import com.coagere.gropix.jetpack.entities.FileModel
import com.coagere.gropix.utils.DownloadImage.downloadFile
import com.coagere.gropix.utils.DownloadImage.downloadImages
import com.tc.utils.utils.customs.RoundedCornersTransformation
import com.tc.utils.utils.utility.isNullAndEmpty
import com.tc.utils.variables.abstracts.OnEventOccurListener
import com.tc.utils.variables.enums.ActionType
import com.tc.utils.variables.interfaces.Constants
import java.util.*

class ImageAdapter(private val modelList: ArrayList<FileModel>, private val listeners: OnEventOccurListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var size = 0

    fun notifyAdapterItemChanged(fileModel: FileModel) {
        if (!isNullAndEmpty(modelList)) {
            var index = 0
            for (fileModelListed in modelList) {
                if (fileModel.fileUrl == fileModelListed.fileUrl) {
                    modelList[index] = fileModel
                    notifyItemChanged(index)
                    break
                }
                index++
            }
        }
    }

    fun notifyAdapterItemRemoved(adapterPosition: Int) {
        size -= 1
        modelList.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)
        if (!enableAdding) {
            size += 1
            notifyItemInserted(size - 1)
        }
        enableAdding = true
    }

    fun notifyAdapterItemInserted() {
        if (size != 0) {
            if (size >= Constants.CONSTANTS_IMAGES_COUNT) {
                size -= 1
                notifyItemRemoved(size - 1)
                size = Constants.CONSTANTS_IMAGES_COUNT
                enableAdding = false
                notifyItemInserted(size - 1)
            } else {
                size += 1
                enableAdding = true
                notifyItemInserted(size - 2)
            }
        } else {
            size = 2
            notifyDataSetChanged()
        }
    }

    private var enableAdding = true
    override fun getItemViewType(position: Int): Int {
        return if (enableAdding) {
            if (position == size - 1) 1 else 0
        } else {
            0
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1 && enableAdding) {
            ViewHolderAdd(LayoutInflater.from(parent.context).inflate(R.layout.adapter_image_add, parent, false))
        } else {
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_image, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position != size - 1) {
            (holder as ViewHolder).bindTo(modelList[position])
        }
    }

    override fun getItemCount(): Int {
        return size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val fileDescriptionTextView: TextView
        private val fileTypeTextView: TextView
        private val fileActionImageView: AppCompatImageView
        private val progressBar: ProgressBar
        private val imageView: ImageView
        fun bindTo(fileModel: FileModel?) {
            itemView.findViewById<View>(R.id.id_image).visibility = View.VISIBLE
            bindImageThings(fileModel, itemView)
        }

        override fun onClick(v: View) {
            val adapterPosition = adapterPosition
            val fileModel = modelList[adapterPosition]
            when (v.id) {
                R.id.id_image_file_delete -> listeners.getEventData(fileModel, ActionType.ACTION_DELETE, adapterPosition)
                R.id.id_image_file_action -> handleFileActions(fileModel, adapterPosition)
                R.id.id_image -> listeners.getEventData(fileModel, ActionType.ACTION_EXPLORE_IMAGE, adapterPosition)
            }
        }

        private fun bindImageThings(fileModel: FileModel?, itemView: View) {
            if (fileModel!!.actionType == 0) {
                if (isNullAndEmpty(fileModel.fileUrl)) downloadImages(itemView.context, fileModel.downloadUrl,
                        imageView, R.drawable.placeholder_rounded, RoundedCornersTransformation.CornerType.ALL, Constants.VALUE_IMAGE_RADIUS)
                itemView.findViewById<View>(R.id.id_image_file_delete).visibility = View.VISIBLE
                fileDescriptionTextView.text = itemView.context.getString(R.string.string_button_name_remove)
                progressBar.visibility = View.GONE
                fileActionImageView.visibility = View.GONE
            } else {
                fileTypeTextView.text = fileModel.fileType!!.toUpperCase()
                downloadFile(itemView.context, fileModel.fileUrl,
                        imageView, R.drawable.placeholder_rounded, RoundedCornersTransformation.CornerType.ALL)
                handleActionType(fileModel)
            }
        }

        private fun handleActionType(fileModel: FileModel?) {
            if (fileModel!!.actionType == Constants.TYPE_ACTION_UPLOAD || fileModel.actionType == 0) {
                if (fileModel.actionType == Constants.TYPE_ACTION_UPLOAD) {
                    fileActionImageView.setImageResource(R.drawable.icon_vd_upload)
                    fileActionImageView.visibility = View.VISIBLE
                } else {
                    fileActionImageView.visibility = View.GONE
                }
                progressBar.visibility = View.GONE
                fileDescriptionTextView.text = fileModel.displaySize
                itemView.findViewById<View>(R.id.id_image_file_delete).visibility = View.VISIBLE
                fileDescriptionTextView.text = fileModel.displaySize
            } else if (fileModel.actionType == Constants.TYPE_ACTION_CANCEL) {
                itemView.findViewById<View>(R.id.id_image_file_delete).visibility = View.GONE
                fileActionImageView.visibility = View.VISIBLE
                fileActionImageView.setImageResource(R.drawable.icon_vd_clear)
                progressBar.visibility = View.VISIBLE
                progressBar.max = (fileModel.fileSize / 1024).toInt()
                progressBar.progress = fileModel.progress
                fileDescriptionTextView.text = fileModel.progressData
            }
        }

        private fun handleFileActions(fileModel: FileModel?, adapterPosition: Int) {
            when (fileModel!!.actionType) {
                Constants.TYPE_ACTION_CANCEL -> {
                    listeners.getEventData(fileModel, ActionType.ACTION_CANCEL, adapterPosition)
                    fileModel.progress = 0
                    fileModel.actionType = Constants.TYPE_ACTION_UPLOAD
                    handleActionType(fileModel)
                }
                Constants.TYPE_ACTION_UPLOAD -> {
                    fileModel.progress = 0
                    fileModel.actionType = Constants.TYPE_ACTION_CANCEL
                    handleActionType(fileModel)
                    listeners.getEventData(fileModel, ActionType.ACTION_UPLOAD, adapterPosition)
                }
            }
        }

        init {
            imageView = itemView.findViewById(R.id.id_image)
            fileActionImageView = itemView.findViewById(R.id.id_image_file_action)
            fileDescriptionTextView = itemView.findViewById(R.id.id_text_file_description)
            fileTypeTextView = itemView.findViewById(R.id.id_text_file_type)
            progressBar = itemView.findViewById(R.id.id_progress_bar)
            fileActionImageView.setOnClickListener(this)
            itemView.findViewById<View>(R.id.id_image_file_delete).setOnClickListener(this)
            imageView.setOnClickListener(this)
        }
    }

    inner class ViewHolderAdd(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(v: View) {
            if (v.id == R.id.id_text_hide) {
                listeners.getEventData(ActionType.ACTION_CANCEL)
            } else {
                listeners.getEventData(ActionType.ACTION_ADD)
            }
        }

        init {
            itemView.findViewById<View>(R.id.id_image_add).setOnClickListener(this)
            itemView.findViewById<View>(R.id.id_text_hide).setOnClickListener(this)
            itemView.setOnClickListener(this)
        }
    }

}