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
import com.coagere.gropix.jetpack.entities.ItemModel
import com.coagere.gropix.jetpack.entities.OrderModel
import com.coagere.gropix.jetpack.entities.PriceModel
import com.coagere.gropix.jetpack.viewmodels.OrderVM
import com.coagere.gropix.ui.adapters.BillingDetailsAdapter
import com.tc.utils.elements.BaseActivity
import com.tc.utils.utils.helpers.HelperActionBar
import com.tc.utils.utils.helpers.JamunAlertDialog
import com.tc.utils.utils.helpers.Utils
import com.tc.utils.utils.utility.isNull
import com.tc.utils.variables.abstracts.OnEventOccurListener
import com.tc.utils.variables.interfaces.Constants
import com.tc.utils.variables.interfaces.IntentInterface
import kotlinx.android.synthetic.main.activity_explore_order.*

/**
 * @author Jatin Sahgal by 28-Sep-2020 13:54
 */

class ExploreOrderActivity : BaseActivity(), View.OnClickListener {
    private var binding: ActivityExploreOrderBinding? = null
    private var orderModel: OrderModel? = null
    private var totalAmount: Double = 0.toDouble()
    private val viewModel by lazy {
        ViewModelProvider(this).get(OrderVM::class.java)
    }
    val modelList =  arrayListOf(
        ItemModel("Soap",33,4),
        ItemModel("Cream",33,4),
        ItemModel("Soft Drinks",33,4),
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExploreOrderBinding.inflate(LayoutInflater.from(this))
        orderModel = intent.getParcelableExtra(IntentInterface.INTENT_FOR_MODEL)
        binding!!.apply {
            this.model = orderModel
            this.clickListener = this@ExploreOrderActivity
            executePendingBindings()
        }
        Utils.log(orderModel!!.image)
        lifecycleScope.launchWhenCreated {
            setContentView(binding!!.root)
            initializeViewModel()
            initializeBillingData()
            setToolbar()
        }
    }

    override fun initializeRecyclerView() {
        super.initializeRecyclerView()
        binding!!.idRecyclerViewBills.layoutManager = LinearLayoutManager(this)
        binding!!.idRecyclerViewBills.adapter = BillingDetailsAdapter(modelList)
    }

    override fun setToolbar() {
        super.setToolbar()
        Utils.doStatusColorWhite(window)
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
        for (serviceModel in modelList) {
            for (model: ItemModel in modelList) {
                totalAmount += (model.itemPrice * model.times)
                selectedCount += model.times
            }
        }
        id_text_selected_items.text = selectedCount.toString()
        binding!!.idTextBillAmount.text = totalAmount.toString()
        binding!!.idTextTotalAmount.text = String.format("%.2f", totalAmount)
        initializeRecyclerView()
        binding!!.idParentBilling.visibility = View.VISIBLE
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
