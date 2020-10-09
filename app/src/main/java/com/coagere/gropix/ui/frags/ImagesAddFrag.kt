package com.coagere.gropix.ui.frags

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coagere.gropix.databinding.FragImagesAddBinding
import com.coagere.gropix.jetpack.entities.FileModel
import com.coagere.gropix.services.post.UploadPhotoAsync
import com.coagere.gropix.ui.activities.ViewImageActivity
import com.coagere.gropix.ui.adapters.ImageAdapter
import com.coagere.gropix.utils.UtilityClass
import com.tc.utils.elements.BaseFragment
import com.tc.utils.receivers.ServiceReceiver
import com.tc.utils.utils.utility.CheckConnection.checkConnection
import com.tc.utils.utils.utility.isNotNull
import com.tc.utils.utils.utility.isNullAndEmpty
import com.tc.utils.variables.abstracts.OnEventOccurListener
import com.tc.utils.variables.enums.ActionType
import com.tc.utils.variables.interfaces.Constants
import com.tc.utils.variables.interfaces.IntentInterface
import tk.jamun.ui.snacks.L
import java.util.*

class ImagesAddFrag : BaseFragment(), ServiceReceiver.Receiver {
    private var binding: FragImagesAddBinding? = null
    private val utilityClass: UtilityClass by lazy { UtilityClass(requireActivity(), binding!!.root) }
    var modelList: ArrayList<FileModel> = arrayListOf()
    private var adapter: ImageAdapter? = null
    private var listeners: OnEventOccurListener? = null
    private var resultReceiver: ServiceReceiver? = null
    private var recyclerView: RecyclerView? = null
    private var bundle: Intent? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (binding == null) {
            binding = FragImagesAddBinding.inflate(inflater, container, false)
            lifecycleScope.launchWhenCreated {
                initializeRecyclerView()
            }
        }
        return binding!!.root
    }


    override fun initializeRecyclerView() {
        recyclerView = utilityClass.setRecyclerView(
            binding!!.idRecyclerView.id,
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        )
        adapter = ImageAdapter(modelList, object : OnEventOccurListener() {
            override fun getEventData(`object`: Any) {
                super.getEventData(`object`)
                if (isNotNull(listeners)) listeners!!.getEventData(`object`)
            }

            override fun getEventData(`object`: Any, actionType: ActionType, adapterPosition: Int) {
                super.getEventData(`object`, actionType, adapterPosition)
                val fileModel = `object` as FileModel
                bundle?.putExtra(IntentInterface.INTENT_FOR_POSITION, adapterPosition)
                when (actionType) {
                    ActionType.ACTION_CANCEL -> if (UploadPhotoAsync.isRunning) {
                        bundle!!.putExtra(
                            IntentInterface.INTENT_COME_FROM,
                            Constants.TYPE_ACTION_CANCEL
                        )
                        LocalBroadcastManager.getInstance(context!!).sendBroadcast(bundle!!)
                    }
                    ActionType.ACTION_UPLOAD -> if (checkConnection(activity)) {
                        if (UploadPhotoAsync.isRunning) {
                            bundle!!.putExtra(
                                IntentInterface.INTENT_COME_FROM,
                                Constants.TYPE_ACTION_UPLOAD
                            )
                            bundle!!.putExtra(IntentInterface.INTENT_FOR_MODEL, fileModel)
                            LocalBroadcastManager.getInstance(context!!).sendBroadcast(bundle!!)
                        } else {
                            UploadPhotoAsync.start(requireContext(), modelList, resultReceiver)
                        }
                    }
                    ActionType.ACTION_DELETE -> if (checkConnection(activity)) {
                        adapter!!.notifyAdapterItemRemoved(adapterPosition)
                        if (isNotNull(listeners)) listeners!!.getEventData(
                            `object`,
                            actionType,
                            adapterPosition
                        )
                    }
                    else -> {
                        ViewImageActivity.launch(requireActivity(), arrayOf(fileModel.downloadUrl!!),
                            adapterPosition = adapterPosition)
                    }
                }
            }
        })
        recyclerView?.adapter = adapter
    }

    fun addImages(fileModel: FileModel) {
        if (modelList.size + 1 <= Constants.CONSTANTS_IMAGES_COUNT) {
            Collections.addAll(modelList, fileModel)
            if (isNullAndEmpty(fileModel.downloadUrl)) {
                fileModel.actionType = Constants.TYPE_ACTION_CANCEL
            } else {
                fileModel.actionType = 0
            }
            adapter?.notifyAdapterItemInserted()
            recyclerView?.scrollToPosition(modelList.size)
            UploadPhotoAsync.start(requireContext(), modelList, resultReceiver)
        }
    }

    override fun onReceiveResult(requestCode: Int, resultCode: Int, resultData: Bundle) {
        when (resultCode) {
            Constants.RESPONSE_SUCCESS -> if (isNotNull(listeners)) listeners!!.getEventData(
                modelList.toTypedArray(),
                ActionType.ACTION_UPDATE_STATUS,
                0
            )
            Constants.RESPONSE_PENDING -> {
                val fileModel: FileModel? =
                    resultData.getParcelable(IntentInterface.INTENT_FOR_MODEL)
                fileModel?.let {
                    adapter!!.notifyAdapterItemChanged(it)
                    if (isNotNull(listeners) && it.actionType == 0) listeners!!.getEventData(
                        it,
                        ActionType.ACTION_REMOVE,
                        0
                    )
                }
            }
        }
    }

    val isAvailableToSubmit: Boolean
        get() {
            if (isNullAndEmpty(modelList)) return true
            for (fileModel in modelList) {
                if (isNullAndEmpty(fileModel.downloadUrl)) {
                    return false
                }
            }
            return true
        }

    fun bindListeners(listeners: OnEventOccurListener?) {
        this.listeners = listeners
    }

    companion object {
        operator fun get(moduleType: Int): ImagesAddFrag {
            val imagesAddFrag = ImagesAddFrag()
            val bundle = Bundle()
            bundle.putInt(IntentInterface.INTENT_COME_FROM, moduleType)
            imagesAddFrag.arguments = bundle
            return imagesAddFrag
        }
    }
}