package com.coagere.gropix.ui.activities

import android.os.Bundle
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
import com.coagere.gropix.jetpack.viewmodels.OrderVM
import com.coagere.gropix.ui.adapters.BillingDetailsAdapter
import com.coagere.gropix.utils.HelperLogout
import com.coagere.gropix.utils.UtilityClass
import com.tc.utils.elements.BaseActivity
import com.tc.utils.utils.helpers.HelperActionBar
import com.tc.utils.utils.helpers.Utils
import com.tc.utils.variables.abstracts.OnEventOccurListener
import com.tc.utils.variables.interfaces.IntentInterface
import kotlinx.android.synthetic.main.activity_explore_order.*
import tk.jamun.ui.snacks.MySnackBar

/**
 * @author Jatin Sahgal by 28-Sep-2020 13:54
 */
class ExploreOrderActivity : BaseActivity(), View.OnClickListener {
    private var binding: ActivityExploreOrderBinding? = null
    private lateinit var orderModel: OrderModel
    private val viewModel by lazy {
        ViewModelProvider(this).get(OrderVM::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExploreOrderBinding.inflate(LayoutInflater.from(this))
        orderModel = intent.getParcelableExtra(IntentInterface.INTENT_FOR_MODEL)!!
        binding!!.apply {
            this.model = orderModel
            this.clickListener = this@ExploreOrderActivity
            executePendingBindings()
        }
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
        binding!!.idRecyclerViewBills.adapter = BillingDetailsAdapter(orderModel.itemList.toMutableList())
    }

    override fun setToolbar() {
        super.setToolbar()
        Utils.doStatusColorWhite(window)
        HelperActionBar.setSupportActionBar(
            this@ExploreOrderActivity,
            binding!!.idAppBar
        )
    }

    override fun initializeViewModel() {
        super.initializeViewModel()
        viewModel.getApiOrderDetails(orderModel, object : OnEventOccurListener() {
            override fun getEventData(`object`: Any?) {
                super.getEventData(`object`)
                orderModel = `object` as OrderModel
                initializeBillingData()
            }

            override fun onErrorResponse(`object`: Any?, errorMessage: String?) {
                super.onErrorResponse(`object`, errorMessage)
                if (UtilityClass(this@ExploreOrderActivity).isUnAuthrized(`object`)) {
                    HelperLogout.logMeOut(
                        this@ExploreOrderActivity,
                        object : OnEventOccurListener() {})
                } else {
                    MySnackBar.getInstance()
                        .showSnackBarForMessage(this@ExploreOrderActivity, errorMessage)
                }
            }
        })
    }

    private fun initializeBillingData() {
        id_text_delivery_fee.text = orderModel.deliveryFee
//        binding!!.idTextBillAmount.text = orderModel!!.totalAmount.toString()
        binding!!.idTextTotalAmount.text = orderModel.totalAmount
        initializeRecyclerView()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.id_image -> {

            }
            R.id.id_text_button_cancel -> {

            }
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
        super.onBackPressed()
    }


}
