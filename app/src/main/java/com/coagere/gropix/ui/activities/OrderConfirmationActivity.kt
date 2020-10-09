package com.coagere.gropix.ui.activities

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.coagere.gropix.R
import com.coagere.gropix.databinding.ActivityConfirmationBinding
import com.coagere.gropix.jetpack.entities.AddressModel
import com.coagere.gropix.jetpack.entities.FileModel
import com.coagere.gropix.jetpack.entities.OrderModel
import com.coagere.gropix.jetpack.viewmodels.OrderVM
import com.coagere.gropix.prefs.UserStorage
import com.coagere.gropix.ui.frags.ImagesAddFrag
import com.coagere.gropix.utils.GetData
import com.coagere.gropix.utils.HelperLogout
import com.coagere.gropix.utils.UtilityClass
import com.tc.utils.elements.BaseActivity
import com.tc.utils.utils.helpers.HelperActionBar
import com.tc.utils.utils.helpers.JamunAlertDialog
import com.tc.utils.utils.helpers.Utils
import com.tc.utils.utils.utility.CheckConnection
import com.tc.utils.variables.abstracts.OnEventOccurListener
import com.tc.utils.variables.enums.ActionType
import com.tc.utils.variables.interfaces.Constants
import com.tc.utils.variables.interfaces.IntentInterface
import tk.jamun.ui.snacks.MySnackBar

class OrderConfirmationActivity : BaseActivity(), View.OnClickListener {

    private var binding: ActivityConfirmationBinding? = null
    private val utilityClass: UtilityClass by lazy { UtilityClass(this) }
    private lateinit var imageFrag: ImagesAddFrag
    private lateinit var fileModels: ArrayList<FileModel>
    private var model: OrderModel = OrderModel()
    private val viewModel: OrderVM by lazy {
        ViewModelProvider(this).get(OrderVM::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmationBinding.inflate(LayoutInflater.from(this))
        binding!!.apply {
            clickListener = this@OrderConfirmationActivity
            model = UserStorage.instance.addressModel
            executePendingBindings()
        }
        lifecycleScope.launchWhenCreated {
            fileModels = intent.getParcelableArrayListExtra(IntentInterface.INTENT_FOR_MODEL)!!
            setToolbar()
            setContentView(binding!!.root)
            initializeView()
            initializeFragsView()
            initializeViewModel()
        }
    }

    override fun setToolbar() {
        super.setToolbar()
        Utils.doStatusColorWhite(window)
        HelperActionBar.setSupportActionBar(
            this@OrderConfirmationActivity,
            binding!!.idAppBar,
            getString(R.string.string_activity_name_order_confirmation),
        )
    }

    override fun initializeView() {
        super.initializeView()
        binding!!.idEditCity.setAdapter(utilityClass.setAdapter(GetData.getCitiesName()))
        binding!!.idEditState.setAdapter(utilityClass.setAdapter(GetData.getStateName()))
    }

    override fun initializeFragsView() {
        super.initializeFragsView()
        imageFrag = supportFragmentManager.findFragmentById(R.id.id_frag_image) as ImagesAddFrag
        for (model in fileModels) {
            Handler().postDelayed({
                imageFrag.addImages(model)
            }, Constants.THREAD_TIME_DELAY)
        }
        imageFrag.bindListeners(object : OnEventOccurListener() {
            override fun getEventData(
                `object`: Any?,
                actionType: ActionType?,
                adapterPosition: Int
            ) {
                super.getEventData(`object`, actionType, adapterPosition)
                if (actionType == ActionType.ACTION_UPDATE_STATUS) {
                    for (fileModel in (`object` as Array<FileModel>)) {
                        model.images!!.add(fileModel.downloadUrl!!)
                    }
                }
            }
        })
    }

    override fun onClick(v: View?) {
        onClickSubmit()
    }

    private fun onClickSubmit() {
        utilityClass.hideSoftKeyboard()
        if (validate() && CheckConnection.checkConnection(this)) {
//            utilityClass.startProgressBar()
            viewModel.performCreateOrder(model, object : OnEventOccurListener() {
                override fun getEventData(`object`: Any?) {
                    super.getEventData(`object`)
//                    utilityClass.closeProgressBar()
                    Toast.makeText(
                        this@OrderConfirmationActivity,
                        "Order Placed Successfully!!",
                        Toast.LENGTH_LONG
                    ).show()
                    Handler().postDelayed({
                        setResult(Activity.RESULT_OK)
                        finish()
                    }, Constants.THREAD_TIME_DELAY)
                }

                override fun onErrorResponse(`object`: Any?, errorMessage: String?) {
                    super.onErrorResponse(`object`, errorMessage)
                    if (utilityClass.isUnAuthrized(`object`)) {
                        HelperLogout.logMeOut(
                            this@OrderConfirmationActivity,
                            object : OnEventOccurListener() {})
                    } else {
//                        utilityClass.closeProgressBar()
                        MySnackBar.getInstance()
                            .showSnackBarForMessage(
                                this@OrderConfirmationActivity,
                                errorMessage
                            )
                    }
                }
            })
        }
    }

    private fun validate(): Boolean {
//        if (model.images.isNullOrEmpty()) {
//            return false
//        }
        if (utilityClass!!.checkEditTextEmpty(
                editText = binding!!.idEditName,
                minLength = resources.getInteger(R.integer.validation_min_name),
                errorTextView = binding!!.root.findViewById(R.id.id_text_error_name)
            )
        ) {
            return false
        }
        if (utilityClass!!.checkEditTextEmpty(
                editText = binding!!.idEditEmail,
                minLength = resources.getInteger(R.integer.validation_min_email),
                errorTextView = binding!!.root.findViewById(R.id.id_text_error_email)
            )
        ) {
            return false
        }
        if (utilityClass.checkEditTextEmpty(
                editText = binding!!.idEditStreet,
                minLength = resources.getInteger(R.integer.validation_min_name),
                errorTextView = binding!!.root.findViewById(R.id.id_text_error_street)
            )
        ) {
            return false
        }
        if (utilityClass!!.checkEditTextEmpty(
                editText = binding!!.idEditCity,
                minLength = resources.getInteger(R.integer.validation_min_name),
                errorTextView = binding!!.root.findViewById(R.id.id_text_error_city)
            )
        ) {
            return false
        }
        if (utilityClass!!.checkEditTextEmpty(
                editText = binding!!.idEditState,
                minLength = resources.getInteger(R.integer.validation_min_name),
                errorTextView = binding!!.root.findViewById(R.id.id_text_error_city)
            )
        ) {
            return false
        }
        UserStorage.instance.addressModel = AddressModel(
            street = binding!!.idEditStreet.text.toString(),
            city = binding!!.idEditCity.text.toString(),
            state = binding!!.idEditState.text.toString(),
            pinCode = binding!!.idEditPinCode.text.toString()
        )
        model.address = UserStorage.instance.addressModel!!
        model.mobileNumber = UserStorage.instance.mobileNumber
        model.userName = binding!!.idEditName.text.toString()
        model.email = binding!!.idEditEmail.text.toString()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        JamunAlertDialog(this).setAutoCancelable()
            .setAutoNegativeButton(R.string.string_button_name_no)
            .setMessage(R.string.string_message_sure_go_back)
            .setPositiveButton(
                R.string.string_button_name_yes_want
            ) {
                it.dismiss()
                closeEverything()
                Handler().postDelayed({
                    setResult(Activity.RESULT_OK)
                    finish()
                }, Constants.THREAD_TIME_DELAY)
            }
            .show()
    }
}
