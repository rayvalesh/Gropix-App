package com.coagere.gropix.ui.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.coagere.gropix.databinding.ActivityExploreOrderBinding
import com.coagere.gropix.jetpack.entities.OrderModel
import com.tc.utils.elements.BaseActivity
import com.tc.utils.utils.helpers.HelperDialogCommon
import com.tc.utils.utils.helpers.JamunAlertDialog
import com.tc.utils.utils.utility.isNotNull
import com.tc.utils.utils.utility.isNull
import com.tc.utils.variables.abstracts.OnEventOccurListener
import com.tc.utils.variables.enums.ActionType
import com.tc.utils.variables.interfaces.ApiKeys
import com.tc.utils.variables.interfaces.Constants
import com.tc.utils.variables.interfaces.IntentInterface
import com.tc.utils.variables.interfaces.JamunDialogInterface

/**
 * @author Jatin Sahgal by 28-Sep-2020 13:54
 */
class ExploreOrderActivity : BaseActivity() {
    private var binding: ActivityExploreOrderBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (binding == null) {
            binding = ActivityExploreOrderBinding.inflate(LayoutInflater.from(this))
            binding!!.apply {
                this.model =
                    intent.getParcelableExtra<OrderModel>(IntentInterface.INTENT_FOR_MODEL)
                executePendingBindings()
            }
        }
        lifecycleScope.launchWhenCreated {
            setContentView(binding!!.root)
            initializeViewModel()
            initializeView()
        }
    }

    override fun initializeFragsView() {
        super.initializeFragsView()
        cartFrag = supportFragmentManager.findFragmentById(R.id.id_frag) as CartFrag
        cartFrag!!.bindListener(object : OnEventOccurListener() {
            override fun getEventData(`object`: Any?) {
                super.getEventData(`object`)
                when (`object` as ActionType) {
                    ActionType.ACTION_BOOK -> {
                        id_button_submit.isEnabled = true
                        initializeBillingData()
                    }
                    else -> {
                        initializeAmount()
                    }
                }
            }
        })
    }

    private fun callBookOrder() {
        if (cartFrag!!.modelAddress == null) {
            cartFrag!!.onClickAddress()
        } else {
            utilityClass.showProgressDialog(
                id_button_submit,
                findViewById(R.id.id_progress_bar_book), true
            )
            id_parent_bottom.visibility = View.GONE
            viewModel.postCartItems(
                cartFrag!!.modelAddress!!,
                cartFrag!!.modelList!!,
                object : OnEventOccurListener() {
                    override fun getEventData(`object`: Any?) {
                        super.getEventData(`object`)
                        utilityClass.closeProgressDialog(
                            id_button_submit,
                            progressView = findViewById(R.id.id_progress_bar_book), hideView = false
                        )
                        id_parent_bottom.visibility = View.VISIBLE
                        id_button_submit.isEnabled = false
                        id_text_warning_title.text =
                            "Their is no delivery service available in your Area."
                    }

                    override fun onErrorResponse(`object`: Any?, errorMessage: String?) {
                        super.onErrorResponse(`object`, errorMessage)
                        id_button_submit.isEnabled = true
                        utilityClass.closeProgressDialog(
                            id_button_submit,
                            progressView = findViewById(R.id.id_progress_bar_book), hideView = false
                        )
                        MySnackBar.getInstance()
                            .showSnackBarForMessage(this@CartActivity, errorMessage)
                    }
                })
        }
    }

    private fun initializeBillingData() {
        var selectedCount = 0
        for (serviceModel in cartFrag!!.modelList!!) {
            for (priceModel: PriceModel in serviceModel.priceList) {
                totalAmount += (priceModel.price * priceModel.selectedCount)
                selectedCount += priceModel.selectedCount
            }
        }
        id_text_selected_items.text = selectedCount.toString()
        if (selectedCount > 0) {
            id_text_bill_amount.text = totalAmount.toString()
            if (isNull(serviceAdapter)) {
                id_recycler_view_bills.layoutManager = LinearLayoutManager(this@CartActivity)
                serviceAdapter = BillingDetailsAdapter(cartFrag!!.modelList!!)
                id_recycler_view_bills.adapter = serviceAdapter
                id_recycler_view_bills.isNestedScrollingEnabled = false
            } else {
                serviceAdapter?.notifyAdapterDataSetChanged(cartFrag!!.modelList!!)
            }
            initializeAmount()
            id_parent_billing.visibility = View.VISIBLE
            id_parent_bottom_view.visibility = View.VISIBLE
        } else {
            id_parent_bottom_view.visibility = View.GONE
            id_parent_billing.visibility = View.GONE
        }
    }

    private fun initializeAmount() {
        val amount = totalAmount + if (cartFrag!!.modelAddress != null) {
            (totalAmount * 0.7)
        } else {
            (totalAmount * 0.18)
        }
        id_text_total_amount.text = String.format("%.2f", amount)
        id_text_payment_amount.text = String.format("%.2f", amount)
    }

    override fun initializeListeners() {
        super.initializeListeners()
        id_button_submit.setOnClickListener {
            callBookOrder()
        }
    }

    private fun onClickDelete() {
        UserStorage.instance.cartItems = emptyArray()
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_cart, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.id_menu_overflow -> {
                popupWindow(findViewById(R.id.id_menu_overflow))
            }
            else -> utilityClass.setNavigationBackButton(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun popupWindow(viewPoint: View) {
        val layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        if (isNotNull(layoutInflater)) {
            val view: View = layoutInflater.inflate(R.layout.popup_home, null)
            popupWindow = HelperDialogCommon().popupWindowOverflow(view, this, viewPoint)
            view.findViewById<View>(R.id.id_linear_action_clear)
                .setOnClickListener(this)
            view.findViewById<View>(R.id.id_linear_action_terms)
                .setOnClickListener(this)
            view.findViewById<View>(R.id.id_linear_action_contact)
                .setOnClickListener(this)
            view.findViewById<View>(R.id.id_switch_night_mode).visibility = View.GONE
        }
    }

    override fun onClick(v: View?) {
        if (isNotNull(popupWindow)) {
            popupWindow?.dismiss()
        }
        when (v?.id) {
            R.id.id_linear_action_terms ->
                HelperIntent(this).intentForBrowser(ApiKeys.URL_PRIVACY_POLICY)
            R.id.id_linear_action_contact -> {
                startActivity(Intent(this@CartActivity, ReachUsActivity::class.java))
            }
            R.id.id_linear_action_clear -> {
                onClickDelete()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        closeEverything()
    }

    override fun onBackPressed() {
        JamunAlertDialog(this).setAutoCancelable()
            .setAutoNegativeButton(R.string.string_button_name_no)
            .setMessage(R.string.string_message_sure_go_back)
            .setPositiveButton(
                R.string.string_button_name_yes_want,
                JamunDialogInterface.OnClickListener {
                    it.dismiss()
                    closeEverything()
                    Handler().postDelayed({
                        setResult(Activity.RESULT_OK)
                        finish()
                    }, Constants.THREAD_TIME_DELAY)
                })
            .show()
    }

    override fun closeEverything() {
        super.closeEverything()
        closeLocationUpdates()
        viewModel.closeEverything()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (CheckOs.permissionRequestResult(grantResults)) {
            checkAndGo()
        }
    }

    private fun closeLocationUpdates() {
        helperLocation?.stopUpdates()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            helperLocation?.startLocationUpdates()
        }
    }


}
