package com.coagere.gropix.ui.activities

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import androidx.lifecycle.lifecycleScope
import com.coagere.gropix.R
import com.coagere.gropix.databinding.ActivityMainBinding
import com.coagere.gropix.jetpack.entities.FileModel
import com.coagere.gropix.ui.frags.OrderListFrag
import com.coagere.gropix.ui.popups.Popups
import com.coagere.gropix.utils.HelperFileFormat
import com.coagere.gropix.utils.ShareData
import com.coagere.gropix.utils.UtilityClass
import com.tc.utils.elements.BaseActivity
import com.tc.utils.utils.helpers.HelperIntent
import com.tc.utils.utils.helpers.JamunAlertDialog
import com.tc.utils.utils.helpers.Utils
import com.tc.utils.utils.utility.UtilityCheckPermission
import com.tc.utils.variables.abstracts.OnEventOccurListener
import com.tc.utils.variables.enums.ActionType
import com.tc.utils.variables.interfaces.ApiKeys
import com.tc.utils.variables.interfaces.Constants
import com.tc.utils.variables.interfaces.IntentInterface
import tk.jamun.ui.share.models.ModelIntentPicker
import tk.jamun.ui.share.views.PickerIntent
import tk.jamun.ui.share.views.PickerShareFiles
import tk.jamun.ui.snacks.MySnackBar
import tk.jamunx.ui.camera.utils.InterfaceUtils
import java.io.File
import java.util.*

class MainActivity : BaseActivity(), View.OnClickListener {
    private var binding: ActivityMainBinding? = null
    private var utilityClass: UtilityClass? = null
    private var intentPicker: PickerIntent? = null
    private var popup: Popups? = null
    private var pendingFrag: OrderListFrag = OrderListFrag.get(Constants.MODULE_PENDING)
    private var cancelledFrag: OrderListFrag = OrderListFrag.get(Constants.MODULE_PLACED)
    private var placedFrag: OrderListFrag = OrderListFrag.get(Constants.MODULE_CANCELLED)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (binding == null) {
            binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
            binding!!.apply {
                clickListener = this@MainActivity
                executePendingBindings()
            }
        }
        lifecycleScope.launchWhenCreated {
            utilityClass = UtilityClass(this@MainActivity)
            setContentView(binding!!.root)
            initializeViewModel()
            initializeListeners()
            initializeFragsView()
        }
    }

    override fun initializeFragsView() {
        super.initializeFragsView()
        val transactionManager = supportFragmentManager.beginTransaction()
        if (!pendingFrag.isAdded) {
            transactionManager.add(
                R.id.id_frag_pending,
                pendingFrag,
                "Pending Frag"
            )
                .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)

        }
        if (!cancelledFrag.isAdded) {
            transactionManager
                .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                .add(
                    R.id.id_frag_cancelled,
                    cancelledFrag,
                    "Cancelled Frag"
                )
        }
        if (!placedFrag.isAdded) {
            transactionManager
                .add(
                    R.id.id_frag_complete,
                    placedFrag,
                    "Placed Frag"
                )
                .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
        }

    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.id_parent_overflow -> {
                createPopup(binding!!.idParentOverflow)
                binding!!.idParentOverflow.startAnimation(
                    AnimationUtils.loadAnimation(
                        this@MainActivity,
                        R.anim.bounce
                    )
                )
            }
            R.id.id_image_pick -> {
                binding!!.idParent.visibility = View.VISIBLE
                binding!!.idImagePick.startAnimation(
                    AnimationUtils.loadAnimation(
                        this@MainActivity,
                        R.anim.bounce
                    )
                )
            }
            R.id.id_parent_camera -> {
                onClickCamera()
            }
            R.id.id_parent_manager -> {
                onClickManager()
            }
        }
    }

    private fun onClickImage() {
        if (intentPicker == null) {
            val arrayList =
                ArrayList<ModelIntentPicker>()
            arrayList.add(
                ModelIntentPicker(
                    PickerIntent.PICKER_CAMERA,
                    getString(R.string.string_button_name_camera),
                    R.drawable.library_icon_vd_camera,
                    R.drawable.background_intent,
                    PickerIntent.PICKER_CAMERA
                )
            )
            arrayList.add(
                ModelIntentPicker(
                    PickerIntent.PICKER_MANAGER,
                    getString(R.string.string_button_name_manager),
                    R.drawable.icon_vd_manager,
                    R.drawable.background_intent,
                    "image/*",
                    PickerIntent.PICKER_MANAGER
                )
            )

            intentPicker = PickerIntent().setThings(this).setPicker(
                getString(R.string.string_label_choose_image), arrayList
            ) { modelIntentPicker: ModelIntentPicker ->
                when (modelIntentPicker.id) {
                    PickerIntent.PICKER_CAMERA -> {
                        onClickCamera()
                    }
                    PickerIntent.PICKER_MANAGER -> {
                        onClickManager()
                    }

                }
                intentPicker?.dismiss()
            }
        }
        intentPicker?.showPicker(supportFragmentManager)
    }

    private fun onClickCamera() {
        startActivityForResult(
            Intent(this, CameraActivity::class.java),
            IntentInterface.REQUEST_CAMERA
        )
    }

    private fun onClickManager() {
        if (UtilityCheckPermission.checkPermission(
                this,
                UtilityCheckPermission.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
            )
        ) {
            val chooserIntent = HelperFileFormat.getInstance().getFilePickerIntent(
                this,
                "image/*", true
            )
            try {
                startActivityForResult(chooserIntent, IntentInterface.REQUEST_MANAGER)
            } catch (ignored: ActivityNotFoundException) {
                Utils.toast(this, "No suitable File Manager was found.")
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null)
            when (requestCode) {
                IntentInterface.REQUEST_CAMERA -> {
                    val fileModel = utilityClass!!.getFileModelFromFile(
                        File(data.getStringExtra(InterfaceUtils.CAMERA_RESULT_IMAGE_PATH)!!)
                    )
                    fileModel?.let {
                        uploadImage(fileModel)
                    }
                }
                IntentInterface.REQUEST_MANAGER -> {
                    if (resultCode == Activity.RESULT_OK) {
                        if (data != null && data.data != null) {
                            HelperFileFormat.getInstance().getFileFromUri(this,
                                data.data!!,
                                object : OnEventOccurListener() {
                                    override fun getEventData(`object`: Any?) {
                                        super.getEventData(`object`)
                                        val fileModel: FileModel = (`object` as Array<FileModel>)[0]
                                        uploadImage(fileModel)
                                    }

                                    override fun onErrorResponse(
                                        `object`: Any?,
                                        errorMessage: String?
                                    ) {
                                        super.onErrorResponse(`object`, errorMessage)
                                        MySnackBar.getInstance()
                                            .showSnackBarForMessage(this@MainActivity, errorMessage)
                                    }
                                });
                        }
                    }
                }
            }
    }


    private fun createPopup(view: View) {
        popup = Popups(this@MainActivity, object : OnEventOccurListener() {
            override fun getEventData(`object`: Any?) {
                super.getEventData(`object`)
                when (`object` as ActionType) {
                    ActionType.ACTION_RATE -> {
                        HelperIntent.rateUs(
                            this@MainActivity
                        )
                    }
                    ActionType.ACTION_SHARE -> {
                        val shareData = ShareData(this@MainActivity)
                        PickerShareFiles().setThings(this@MainActivity)
                            .shareThings(
                                supportFragmentManager,
                                shareData.referralShareDesc,
                                shareData.referralShareSubject
                            )
                    }
                    ActionType.ACTION_PRIVACY_POLICY -> {
                        HelperIntent.intentForBrowser(this@MainActivity, ApiKeys.URL_PRIVACY_POLICY)
                    }
                    ActionType.ACTION_LOGOUT -> {
                        JamunAlertDialog(this@MainActivity)
                            .setMessage(R.string.string_dialog_desc_logout)
                            .setPositiveButton(
                                R.string.string_label_logout
                            ) {
                                it.dismiss()
                            }.setAutoNegativeButton(R.string.string_button_name_no)
                            .setAutoCancelable()
                            .show()
                    }
                }
            }
        })
        popup?.callHomePopup(view)
    }

    private fun uploadImage(fileModel: FileModel) {
        startActivity(
            Intent(this@MainActivity, OrderConfirmationActivity::class.java)
                .putExtra(IntentInterface.INTENT_FOR_MODEL, fileModel)
        )
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
