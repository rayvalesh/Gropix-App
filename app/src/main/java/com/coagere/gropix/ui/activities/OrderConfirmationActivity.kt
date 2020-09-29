package com.coagere.gropix.ui.activities

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.coagere.gropix.R
import com.coagere.gropix.databinding.ActivityConfirmationBinding
import com.coagere.gropix.jetpack.entities.AddressModel
import com.coagere.gropix.jetpack.entities.FileModel
import com.coagere.gropix.prefs.UserStorage
import com.coagere.gropix.ui.frags.ImagesAddFrag
import com.coagere.gropix.utils.GetData
import com.coagere.gropix.utils.UtilityClass
import com.tc.utils.elements.BaseActivity
import com.tc.utils.utils.helpers.HelperActionBar
import com.tc.utils.utils.helpers.JamunAlertDialog
import com.tc.utils.variables.interfaces.Constants
import com.tc.utils.variables.interfaces.IntentInterface

class OrderConfirmationActivity : BaseActivity(), View.OnClickListener {

    private var binding: ActivityConfirmationBinding? = null
    private var utilityClass: UtilityClass? = null
    private var imageFrag: ImagesAddFrag? = ImagesAddFrag[Constants.MODULE_CART]
    private var fileModel: FileModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (binding == null) {
            binding = ActivityConfirmationBinding.inflate(LayoutInflater.from(this))
            binding!!.apply {
                clickListener = this@OrderConfirmationActivity
                model = UserStorage.instance.addressModel
                executePendingBindings()
            }
        }
        lifecycleScope.launchWhenCreated {
            utilityClass = UtilityClass(this@OrderConfirmationActivity)
            fileModel = intent.getParcelableExtra(IntentInterface.INTENT_FOR_MODEL)
            setToolbar()
            setContentView(binding!!.root)
            initializeView()
            initializeFragsView()
            initializeViewModel()
        }
    }

    override fun setToolbar() {
        super.setToolbar()
        HelperActionBar.setSupportActionBar(
            this@OrderConfirmationActivity,
            binding!!.idAppBar,
            getString(R.string.string_activity_name_order_confirmation),
        )
    }

    override fun initializeView() {
        super.initializeView()
        binding!!.idEditCity.setAdapter(utilityClass!!.setAdapter(GetData.getCitiesName()))
        binding!!.idEditState.setAdapter(utilityClass!!.setAdapter(GetData.getStateName()))
    }

    override fun initializeFragsView() {
        super.initializeFragsView()
        supportFragmentManager.beginTransaction().replace(
            R.id.id_frag_image,
            imageFrag!!,
            "Image Frag"
        ).setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).commit()
        imageFrag!!.addImages(fileModel!!)
    }

    override fun onClick(v: View?) {
        onClickSubmit()
    }

    private fun onClickSubmit() {
        if (validate()) {

        }
    }

    private fun validate(): Boolean {
        if (utilityClass!!.checkEditTextEmpty(
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
                errorTextView = binding!!.root.findViewById(R.id.id_text_error_state)
            )
        ) {
            return false
        }
        UserStorage.instance.addressModel = AddressModel(
            street = binding!!.idEditStreet.toString(),
            city = binding!!.idEditCity.toString(),
            state = binding!!.idEditState.toString(),
            pinCode = binding!!.idEditPinCode.toString()
        )
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
