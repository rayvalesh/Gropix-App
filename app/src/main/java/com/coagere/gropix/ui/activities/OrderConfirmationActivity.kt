package com.coagere.gropix.ui.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.coagere.gropix.R
import com.coagere.gropix.databinding.ActivityConfirmationBinding
import com.coagere.gropix.prefs.UserStorage
import com.coagere.gropix.ui.frags.ImagesAddFrag
import com.coagere.gropix.utils.GetData
import com.coagere.gropix.utils.UtilityClass
import com.tc.utils.elements.BaseActivity
import com.tc.utils.utils.helpers.HelperActionBar

class OrderConfirmationActivity : BaseActivity(), View.OnClickListener {

    private var binding: ActivityConfirmationBinding? = null
    private var utilityClass: UtilityClass? = null
    private var imageFrag: ImagesAddFrag? = null

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

    override fun onClick(v: View?) {
    }
}
