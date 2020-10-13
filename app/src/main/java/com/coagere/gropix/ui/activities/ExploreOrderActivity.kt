package com.coagere.gropix.ui.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.coagere.gropix.R
import com.coagere.gropix.databinding.ActivityExploreOrderBinding
import com.coagere.gropix.jetpack.entities.ItemModel
import com.coagere.gropix.jetpack.entities.OrderModel
import com.coagere.gropix.jetpack.repos.OrderRepo
import com.coagere.gropix.jetpack.viewmodels.OrderVM
import com.coagere.gropix.ui.adapters.BillingDetailsAdapter
import com.coagere.gropix.utils.DownloadImage
import com.coagere.gropix.utils.HelperLogout
import com.coagere.gropix.utils.UtilityClass
import com.tc.utils.elements.BaseActivity
import com.tc.utils.utils.customs.RoundedCornersTransformation
import com.tc.utils.utils.helpers.HelperActionBar
import com.tc.utils.utils.helpers.JamunAlertDialog
import com.tc.utils.utils.helpers.Utils
import com.tc.utils.variables.abstracts.OnEventOccurListener
import com.tc.utils.variables.interfaces.Constants
import com.tc.utils.variables.interfaces.IntentInterface
import kotlinx.android.synthetic.main.activity_explore_order.*
import tk.jamun.ui.snacks.MySnackBar

/**
 * @author Jatin Sahgal by 28-Sep-2020 13:54
 */
class ExploreOrderActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityExploreOrderBinding
    private var orderModel = OrderModel()
    private val viewModel by lazy {
        ViewModelProvider(this).get(OrderVM::class.java)
    }
    private val utilityClass by lazy { UtilityClass(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExploreOrderBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        setToolbar()
        if (intent.getParcelableExtra<OrderModel>(IntentInterface.INTENT_FOR_MODEL) != null) {
            orderModel = intent.getParcelableExtra(IntentInterface.INTENT_FOR_MODEL)!!
            initializeView()
            binding.apply {
                this.model = orderModel
                this.clickListener = this@ExploreOrderActivity
                executePendingBindings()
            }
        } else {
            orderModel.orderId = intent.getStringExtra(IntentInterface.INTENT_FOR_ID)
        }
        initializeViewModel()
    }


    override fun initializeRecyclerView() {
        super.initializeRecyclerView()
        binding.idRecyclerViewBills.layoutManager = LinearLayoutManager(this)
        binding.idRecyclerViewBills.adapter =
            BillingDetailsAdapter(orderModel.itemList.toMutableList())
    }

    override fun setToolbar() {
        super.setToolbar()
        setSupportActionBar(findViewById(R.id.id_app_bar))
        supportActionBar?.title = getString(R.string.string_activity_name_order_details)
        Utils.doStatusColorWhite(window)
        HelperActionBar.setSupportActionBar(
            this@ExploreOrderActivity,
            binding.idAppBar
        )
    }

    override fun initializeViewModel() {
        super.initializeViewModel()
        val id = orderModel.orderId
        viewModel.getApiOrderDetails(
            id!!,
            object : OnEventOccurListener() {
                override fun getEventData(`object`: Any?) {
                    super.getEventData(`object`)
                    orderModel = `object` as OrderModel
                    orderModel.orderId = id
                    binding.model= orderModel
                    binding.clickListener= this@ExploreOrderActivity
                    binding.invalidateAll()
                    binding.executePendingBindings()
                    initializeView()
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

    override fun initializeView() {
        super.initializeView()
        Utils.setVisibility(binding.idTextButtonCancel, true)
        Utils.setVisibility(binding.idParentBilling, true)
        Utils.setVisibility(binding.idParentConfirmation, false)
        when (orderModel.status) {
            Constants.ORDER_CART -> {
                Utils.setVisibility(binding.idParentConfirmation, true)
                binding.idTextStatus.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorStyleSixDark
                    )
                )
                binding.idTextStatus.text =
                    getString(R.string.string_label_status_cart)
            }
            Constants.ORDER_PENDING -> {
                Utils.setVisibility(binding.idParentBilling, false)
                binding.idTextStatus.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorStyleFourDark
                    )
                )
                binding.idTextStatus.text =
                    getString(R.string.string_label_status_placed)
            }
            Constants.ORDER_CONFIRMED -> {
                binding.idTextStatus.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorStyleThreeDark
                    )
                )
                binding.idTextStatus.text =
                    getString(R.string.string_label_status_confirmed)
            }
            Constants.ORDER_OUT_FOR_DELIVERY -> {
                binding.idTextStatus.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorStyleThreeDark
                    )
                )
                binding.idTextStatus.text =
                    getString(R.string.string_label_status_out_delivery)
            }
            Constants.ORDER_COMPLETE -> {
                binding.idTextStatus.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorStyleOneDark
                    )
                )
                Utils.setVisibility(binding.idTextButtonCancel, false)
                binding.idTextStatus.text =
                    getString(R.string.string_label_status_delivered)
            }
            Constants.ORDER_CANCELLED -> {
                binding.idTextStatus.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorStyleSixDark
                    )
                )
                binding.idTextStatus.text = getString(R.string.string_label_status_cancelled)
                Utils.setVisibility(binding.idParentBilling, false)
                Utils.setVisibility(binding.idTextButtonCancel, false)
            }
        }
        DownloadImage.downloadImages(
            this,
            orderModel.images[0],
            binding.idImage,
            R.drawable.placeholder_one,
            RoundedCornersTransformation.CornerType.ALL,
            12
        )
    }

    private fun initializeBillingData() {
        id_text_delivery_fee.text = orderModel.deliveryFee
//        binding.idTextBillAmount.text = orderModel!!.totalAmount.toString()
        binding.idTextTotalAmount.text = orderModel.totalAmount
        initializeRecyclerView()
    }

    /**
     * Api call to Confirm order : [OrderRepo.apiConfirmOrder]
     * Api call to cancel order : [OrderRepo.apiCancelOrder]
     */
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.id_button_submit -> {
                JamunAlertDialog(this).setAutoCancelable()
                    .setMessage(R.string.string_message_sure_confirm_order)
                    .setAutoNegativeButton(R.string.string_button_name_wait)
                    .setPositiveButton(
                        R.string.string_button_name_confirm
                    ) {
                        it.dismiss()
                        utilityClass.startProgressBar(
                            binding.idButtonSubmit,
                            binding.root.findViewById(R.id.id_progress_bar_submit)
                        )
                        binding.idTextButtonCancel.isEnabled = false
                        viewModel.confirmOrder(orderModel, object : OnEventOccurListener() {
                            override fun getEventData(`object`: Any?) {
                                super.getEventData(`object`)
                                binding.idTextButtonCancel.isEnabled = true
                                utilityClass.closeProgressBar()
                                setResult(
                                    RESULT_OK,
                                    Intent().putExtra(
                                        IntentInterface.INTENT_COME_FROM,
                                        Constants.ORDER_CONFIRMED
                                    ).putExtra(
                                        IntentInterface.INTENT_FOR_POSITION, intent.getIntExtra(
                                            IntentInterface.INTENT_FOR_POSITION, 0
                                        )
                                    )
                                )
                                finish()
                            }

                            override fun onErrorResponse(`object`: Any?, errorMessage: String?) {
                                super.onErrorResponse(`object`, errorMessage)
                                binding.idTextButtonCancel.isEnabled = true
                                utilityClass.closeProgressBar()
                                MySnackBar.getInstance()
                                    .showSnackBarForMessage(this@ExploreOrderActivity, errorMessage)
                            }
                        })
                    }
                    .show()

            }
            R.id.id_image -> {
                ViewImageActivity.launch(this, orderModel.images.toTypedArray())
            }
            R.id.id_text_button_cancel -> {
                JamunAlertDialog(this).setAutoCancelable()
                    .setMessage(R.string.string_message_sure_cancel_order)
                    .setAutoNegativeButton(R.string.string_button_name_no)
                    .setPositiveButton(
                        R.string.string_button_name_cancel_order
                    ) {
                        it.dismiss()
                        utilityClass.startProgressBar(
                            binding.idTextButtonCancel,
                            binding.root.findViewById(R.id.id_progress_bar_cancel)
                        )
                        binding.idButtonSubmit.isEnabled = false
                        viewModel.cancelOrder(orderModel, object : OnEventOccurListener() {
                            override fun getEventData(`object`: Any?) {
                                super.getEventData(`object`)
                                binding.idButtonSubmit.isEnabled = true
                                utilityClass.closeProgressBar()
                                setResult(
                                    RESULT_OK,
                                    Intent().putExtra(
                                        IntentInterface.INTENT_COME_FROM,
                                        Constants.ORDER_CANCELLED
                                    ).putExtra(
                                        IntentInterface.INTENT_FOR_POSITION, intent.getIntExtra(
                                            IntentInterface.INTENT_FOR_POSITION, 0
                                        )
                                    )
                                )
                                finish()
                            }

                            override fun onErrorResponse(`object`: Any?, errorMessage: String?) {
                                super.onErrorResponse(`object`, errorMessage)
                                binding.idButtonSubmit.isEnabled = true
                                utilityClass.closeProgressBar()
                                MySnackBar.getInstance()
                                    .showSnackBarForMessage(this@ExploreOrderActivity, errorMessage)
                            }
                        })
                    }
                    .show()

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
