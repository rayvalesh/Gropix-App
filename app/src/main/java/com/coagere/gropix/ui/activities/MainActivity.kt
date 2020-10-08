package com.coagere.gropix.ui.activities

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.coagere.gropix.R
import com.coagere.gropix.jetpack.entities.FileModel
import com.coagere.gropix.ui.frags.OrderListFrag
import com.coagere.gropix.ui.popups.Popups
import com.coagere.gropix.ui.sheets.ChooserSheet
import com.coagere.gropix.utils.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
import com.tc.utils.elements.BaseActivity
import com.tc.utils.utils.helpers.HelperActionBar
import com.tc.utils.utils.helpers.HelperIntent
import com.tc.utils.utils.helpers.JamunAlertDialog
import com.tc.utils.utils.helpers.Utils
import com.tc.utils.utils.utility.UtilityCheckPermission
import com.tc.utils.variables.abstracts.OnEventOccurListener
import com.tc.utils.variables.enums.ActionType
import com.tc.utils.variables.interfaces.ApiKeys
import com.tc.utils.variables.interfaces.Constants
import com.tc.utils.variables.interfaces.IntentInterface
import kotlinx.android.synthetic.main.activity_main.*
import tk.jamun.ui.share.views.PickerShareFiles
import tk.jamun.ui.snacks.MySnackBar
import tk.jamunx.ui.camera.utils.InterfaceUtils
import java.io.File

class MainActivity : BaseActivity(), View.OnClickListener {
    private var utilityClass: UtilityClass? = null
    private var popup: Popups? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenCreated {
            utilityClass = UtilityClass(this@MainActivity)
            setContentView(R.layout.activity_main)
            initializeViewModel()
            initializeListeners()
            initializeTabView()
            setToolbar()
            initializeSheet()
        }
    }


    override fun setToolbar() {
        super.setToolbar()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = ContextCompat.getColor(this@MainActivity, R.color.colorPrimary)
        }
        Utils.doStatusColorWhite(window)
        HelperActionBar.setAppBarLayout(
            this@MainActivity,
            0.8,
            object : HelperActionBar.ScrollingListener {
                override fun up() {
                }

                override fun down() {
                }
            })
    }

    override fun initializeTabView() {
//        id_view_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(id_tab_layout))
//        managerPagerAdapter = ManagerAdapter(supportFragmentManager)
//        id_view_pager.adapter = managerPagerAdapter
//        id_tab_layout.animatedIndicator = CustomTabIndicator(id_tab_layout)
//        id_tab_layout.setupWithViewPager(id_view_pager)
//        id_view_pager.offscreenPageLimit = 1
//        id_view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
//            override fun onPageScrolled(
//                position: Int,
//                positionOffset: Float,
//                positionOffsetPixels: Int
//            ) {
//
//            }
//
//            override fun onPageSelected(position: Int) {
//            }
//
//            override fun onPageScrollStateChanged(state: Int) {}
//        })
    }

    override fun initializeListeners() {
        super.initializeListeners()
        id_parent_button.setOnClickListener {

            val sheet = ChooserSheet()
            sheet.showDialog(object : OnEventOccurListener() {
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
            }, supportFragmentManager)

        }
        id_parent_overflow.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.id_parent_overflow -> {
                createPopup(id_parent_overflow)
                id_parent_overflow.startAnimation(
                    AnimationUtils.loadAnimation(
                        this@MainActivity,
                        R.anim.bounce
                    )
                )
            }
        }
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
                        uploadImage(arrayListOf(fileModel))
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
                        HelperFileFormat.getInstance().getFileFromUri(this,
                            uris.toTypedArray(),
                            object : OnEventOccurListener() {
                                override fun getEventData(`object`: Any?) {
                                    super.getEventData(`object`)
                                    uploadImage(`object` as ArrayList<FileModel>)
                                }

                                override fun onErrorResponse(
                                    `object`: Any?,
                                    errorMessage: String?
                                ) {
                                    super.onErrorResponse(`object`, errorMessage)
                                    MySnackBar.getInstance()
                                        .showSnackBarForMessage(
                                            this@MainActivity,
                                            errorMessage
                                        )
                                }
                            });
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
                    else -> {
                    }
                }
            }
        })
        popup?.callHomePopup(view)
    }

    private fun uploadImage(fileModels: ArrayList<FileModel>) {
        startActivity(
            Intent(this@MainActivity, OrderConfirmationActivity::class.java)
                .putParcelableArrayListExtra(IntentInterface.INTENT_FOR_MODEL, fileModels)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        HelperFileFormat.getInstance().cancelAsync()
    }

    override fun onBackPressed() {
        JamunAlertDialog(this).setAutoCancelable()
            .setMessage(R.string.string_message_sure_exit)
            .setAutoNegativeButton(R.string.string_button_name_no)
            .setPositiveButton(
                R.string.string_button_name_yes
            ) {
                it.dismiss()
                closeEverything()
                Handler().postDelayed({
                    finish()
                }, Constants.THREAD_TIME_DELAY)
            }
            .show()
    }

    inner class ManagerAdapter constructor(fm: FragmentManager) :
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getPageTitle(position: Int): CharSequence? {
            return "My Orders"

        }

        override fun getItem(position: Int): Fragment {
            return OrderListFrag.get(Constants.MODULE_PENDING)
        }

        override fun getCount(): Int {
            return 1
        }
    }
}
