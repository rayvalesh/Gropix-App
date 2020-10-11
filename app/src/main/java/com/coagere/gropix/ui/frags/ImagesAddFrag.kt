package com.coagere.gropix.ui.frags

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.coagere.gropix.databinding.FragImagesAddBinding
import com.coagere.gropix.jetpack.entities.FileModel
import com.coagere.gropix.services.post.UploadPhotoAsync
import com.coagere.gropix.ui.activities.CameraActivity
import com.coagere.gropix.ui.activities.ViewImageActivity
import com.coagere.gropix.ui.adapters.ImageAdapter
import com.coagere.gropix.ui.sheets.ChooserSheet
import com.coagere.gropix.utils.CheckOs
import com.coagere.gropix.utils.HelperFileFormat
import com.coagere.gropix.utils.UtilityClass
import com.tc.utils.elements.BaseFragment
import com.tc.utils.receivers.ServiceReceiver
import com.tc.utils.utils.helpers.Utils
import com.tc.utils.utils.utility.CheckConnection.checkConnection
import com.tc.utils.utils.utility.UtilityCheckPermission
import com.tc.utils.utils.utility.isNotNull
import com.tc.utils.utils.utility.isNullAndEmpty
import com.tc.utils.variables.abstracts.OnEventOccurListener
import com.tc.utils.variables.enums.ActionType
import com.tc.utils.variables.interfaces.Constants
import com.tc.utils.variables.interfaces.IntentInterface
import tk.jamun.ui.snacks.MySnackBar
import tk.jamunx.ui.camera.utils.InterfaceUtils
import java.io.File
import java.util.*

class ImagesAddFrag : BaseFragment(), ServiceReceiver.Receiver {
    private var binding: FragImagesAddBinding? = null
    var modelList: ArrayList<FileModel> = arrayListOf()
    private val utilityClass: UtilityClass by lazy {
        UtilityClass(
            requireActivity(),
            requireView()
        )
    }
    private var adapter: ImageAdapter? = null
    private var listeners: OnEventOccurListener? = null
    private var resultReceiver: ServiceReceiver? = null
    private var bundle: Intent? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (binding == null) {
            binding = FragImagesAddBinding.inflate(inflater, container, false)
            resultReceiver = ServiceReceiver(Handler(), this)
            lifecycleScope.launchWhenCreated {
                initializeRecyclerView()
            }
        }
        return binding!!.root
    }


    override fun initializeRecyclerView() {
        binding!!.idRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter = ImageAdapter(modelList, object : OnEventOccurListener() {
            override fun getEventData(`object`: Any?, actionType: ActionType?) {
                super.getEventData(`object`, actionType)
                if (actionType == ActionType.ACTION_EXPLORE) {
                    ViewImageActivity.launch(requireActivity(), arrayOf(`object` as String))
                }
            }

            override fun getEventData(`object`: Any) {
                super.getEventData(`object`)
                val sheet = ChooserSheet()
                sheet.showDialog(1,object : OnEventOccurListener() {
                    override fun getEventData(`object`: Any?) {
                        super.getEventData(`object`)
                        when (`object` as ActionType) {
                            ActionType.ACTION_CAMERA -> {
                                onClickCamera()
                            }
                            else -> {
                                onClickManager()
                            }
                        }
                    }
                }, childFragmentManager)
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
                        listeners?.getEventData(
                            `object`,
                            actionType,
                            adapterPosition
                        )
                    }
                    else -> {
                        ViewImageActivity.launch(
                            requireActivity(), arrayOf(fileModel.downloadUrl!!),
                            adapterPosition = adapterPosition
                        )
                    }
                }
            }
        })
        binding!!.idRecyclerView.adapter = adapter
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
            binding!!.idRecyclerView.scrollToPosition(modelList.size)
            UploadPhotoAsync.start(requireContext(), modelList, resultReceiver)
        }
    }

    override fun onReceiveResult(requestCode: Int, resultCode: Int, resultData: Bundle) {
        when (resultCode) {
            Constants.RESPONSE_SUCCESS -> listeners?.getEventData(
                modelList.toTypedArray(),
                ActionType.ACTION_UPDATE_STATUS,
                0
            )
            Constants.RESPONSE_PENDING -> {
                val fileModel: FileModel? =
                    resultData.getParcelable(IntentInterface.INTENT_FOR_MODEL)
                fileModel?.let {
                    adapter!!.notifyAdapterItemChanged(it)
                    if (isNotNull(listeners) && it.actionType == 0) listeners?.getEventData(
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

    private fun onClickCamera() {
        startActivityForResult(
            Intent(requireContext(), CameraActivity::class.java),
            IntentInterface.REQUEST_CAMERA
        )
    }

    private fun onClickManager() {
        if (UtilityCheckPermission.checkPermission(
                requireContext(),
                UtilityCheckPermission.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
            )
        ) {
            val chooserIntent = HelperFileFormat.getInstance().getFilePickerIntent(
                requireContext(),
                "image/*", true
            )
            try {
                startActivityForResult(chooserIntent, IntentInterface.REQUEST_MANAGER)
            } catch (ignored: ActivityNotFoundException) {
                Utils.toast(requireContext(), "No suitable File Manager was found.")
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null)
            when (requestCode) {
                IntentInterface.REQUEST_CAMERA -> {
                    val fileModel = utilityClass.getFileModelFromFile(
                        File(data.getStringExtra(InterfaceUtils.CAMERA_RESULT_IMAGE_PATH)!!)
                    )
                    fileModel?.let {
                        addImages(fileModel)
                    }
                }
                IntentInterface.REQUEST_MANAGER -> {
                    val uris = arrayListOf<Uri>()
                    if (resultCode == Activity.RESULT_OK) {
                        if (data.data != null) {
                            uris.add(data.data!!)
                        } else if (CheckOs.checkForJellyBean() && data.clipData != null) {
                            val mClipData = data.clipData
                            for (i in 0 until mClipData!!.itemCount) {
                                val item = mClipData.getItemAt(i)
                                uris.add(item.uri)
                            }
                        }
                        HelperFileFormat.getInstance().getFileFromUri(requireContext(),
                            uris.toTypedArray(),
                            object : OnEventOccurListener() {
                                override fun getEventData(`object`: Any?) {
                                    super.getEventData(`object`)
                                    val models = `object` as ArrayList<FileModel>
                                    for (fileModel in models) {
                                        addImages(fileModel)
                                    }
                                }

                                override fun onErrorResponse(
                                    `object`: Any?,
                                    errorMessage: String?
                                ) {
                                    super.onErrorResponse(`object`, errorMessage)
                                    MySnackBar.getInstance()
                                        .showSnackBarForMessage(
                                            requireActivity(),
                                            errorMessage
                                        )
                                }
                            });
                    }
                }
            }
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