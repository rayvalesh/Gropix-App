package com.coagere.gropix.ui.activities

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.coagere.gropix.R
import com.coagere.gropix.databinding.ActivityExploreOrderBinding
import com.coagere.gropix.jetpack.entities.OrderModel
import com.coagere.gropix.jetpack.entities.PriceModel
import com.coagere.gropix.jetpack.viewmodels.OrderVM
import com.coagere.gropix.ui.adapters.BillingDetailsAdapter
import com.tc.utils.elements.BaseActivity
import com.tc.utils.utils.helpers.HelperActionBar
import com.tc.utils.utils.helpers.JamunAlertDialog
import com.tc.utils.utils.utility.isNull
import com.tc.utils.variables.abstracts.OnEventOccurListener
import com.tc.utils.variables.interfaces.Constants
import com.tc.utils.variables.interfaces.IntentInterface
import kotlinx.android.synthetic.main.activity_explore_order.*
import tk.jamun.ui.snacks.L

/**
 * @author Jatin Sahgal by 28-Sep-2020 13:54
 */

class ExploreOrderActivity : BaseActivity(), View.OnClickListener {
    private var binding: ActivityExploreOrderBinding? = null
    private var serviceAdapter: BillingDetailsAdapter? = null
    private var orderModel: OrderModel? = null
    private var totalAmount: Int = 0
    private val viewModel by lazy {
        ViewModelProvider(this).get(OrderVM::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (binding == null) {
            binding = ActivityExploreOrderBinding.inflate(LayoutInflater.from(this))
            binding!!.apply {
                this.model = intent.getParcelableExtra(IntentInterface.INTENT_FOR_MODEL)
                this.clickListener = this@ExploreOrderActivity
                executePendingBindings()
            }
        }
        lifecycleScope.launchWhenCreated {
            orderModel = intent.getParcelableExtra(IntentInterface.INTENT_FOR_MODEL)
            L.logE(
                orderModel!!.image
            )
            setContentView(binding!!.root)
            initializeViewModel()
            initializeView()
            setToolbar()
        }
    }

    override fun setToolbar() {
        super.setToolbar()
        HelperActionBar.setSupportActionBar(
            this@ExploreOrderActivity,
            binding!!.idAppBar,
            orderModel?.title
        )
    }

    override fun initializeViewModel() {
        super.initializeViewModel()
        viewModel.getApiOrderItemList(orderModel!!, object : OnEventOccurListener() {
            override fun getEventData(`object`: Any?) {
                super.getEventData(`object`)
                initializeBillingData()
            }

            override fun onErrorResponse(`object`: Any?, errorMessage: String?) {
                super.onErrorResponse(`object`, errorMessage)
            }
        })
    }

    private fun initializeBillingData() {
        var selectedCount = 0
        for (serviceModel in orderModel!!.itemModels) {
            for (priceModel: PriceModel in serviceModel.priceList) {
                totalAmount += (priceModel.price * priceModel.selectedCount)
                selectedCount += priceModel.selectedCount
            }
        }
        id_text_selected_items.text = selectedCount.toString()
        if (selectedCount > 0) {
            binding!!.idTextBillAmount.text = totalAmount.toString()
            if (isNull(serviceAdapter)) {
                binding!!.idRecyclerViewBills.layoutManager =
                    LinearLayoutManager(this@ExploreOrderActivity)
                serviceAdapter = BillingDetailsAdapter(orderModel!!.itemModels)
                binding!!.idRecyclerViewBills.adapter = serviceAdapter
                binding!!.idRecyclerViewBills.isNestedScrollingEnabled = false
            } else {
                serviceAdapter?.notifyAdapterDataSetChanged(orderModel!!.itemModels)
            }
            initializeAmount()
            binding!!.idParentBilling.visibility = View.VISIBLE
            binding!!.idParentBottom.visibility = View.VISIBLE
        } else {
            binding!!.idParentBottom.visibility = View.GONE
            binding!!.idParentBilling.visibility = View.GONE
        }
    }

    private fun initializeAmount() {
        val amount = totalAmount + if (orderModel!!.address != null) {
            (totalAmount * 0.7)
        } else {
            (totalAmount * 0.18)
        }
        binding!!.idTextTotalAmount.text = String.format("%.2f", amount)
        binding!!.idTextPaymentAmount.text = String.format("%.2f", amount)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
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
