package com.coagere.gropix.ui.frags

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.coagere.gropix.R
import com.coagere.gropix.databinding.FragOrderListBinding
import com.coagere.gropix.jetpack.entities.OrderModel
import com.coagere.gropix.jetpack.viewmodels.OrderVM
import com.coagere.gropix.ui.activities.ExploreOrderActivity
import com.coagere.gropix.ui.adapters.OrderStatusAdapter
import com.coagere.gropix.utils.UtilityClass
import com.tc.utils.elements.BaseFragment
import com.tc.utils.variables.abstracts.OnEventOccurListener
import com.tc.utils.variables.enums.ActionType
import com.tc.utils.variables.interfaces.Constants
import com.tc.utils.variables.interfaces.IntentInterface
import tk.jamun.ui.snacks.MySnackBar

/**
 * @author Jatin Sahgal by 28-Sep-2020 12:25
 */
class OrderListFrag : BaseFragment() {
    private var binding: FragOrderListBinding? = null
    private var utilityClass: UtilityClass? = null
    private val viewModel: OrderVM by lazy { ViewModelProvider(this).get(OrderVM::class.java) }
    private var adapter: OrderStatusAdapter? = null
    private var moduleType: Int = 0
    private var modelList: ArrayList<OrderModel> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (binding == null) {
            binding = FragOrderListBinding.inflate(
                LayoutInflater.from(container!!.context),
                container,
                false
            )
            lifecycleScope.launchWhenCreated {
                utilityClass = UtilityClass(requireActivity())
                moduleType = requireArguments().getInt(IntentInterface.INTENT_FOR_MODULE_TYPE)
                initializeView()
                initializeViewModel()
                initializeRecyclerView()

            }
        }
        return binding!!.root
    }

    override fun initializeView() {
        super.initializeView()
        when (moduleType) {
            Constants.MODULE_PLACED -> {
                binding!!.idTextTitle.text = getString(R.string.string_label_placed)
            }
            Constants.MODULE_CANCELLED -> {
                binding!!.idTextTitle.text = getString(R.string.string_label_cancelled)
            }
            else -> {
                binding!!.idTextTitle.text = getString(R.string.string_label_pending)
            }
        }
    }

    override fun initializeViewModel() {
        super.initializeViewModel()
        utilityClass!!.startProgressBar()
        viewModel.getApiOrderStatusList(
            moduleType = moduleType,
            modelList = modelList,
            object : OnEventOccurListener() {
                override fun getEventData(`object`: Any?) {
                    super.getEventData(`object`)
                    adapter?.notifyAdapterDataSetChanged()
                    initializeEmptyView(modelList.isNullOrEmpty())
                }

                override fun onErrorResponse(`object`: Any?, errorMessage: String?) {
                    super.onErrorResponse(`object`, errorMessage)
                    MySnackBar.getInstance().showSnackBarForMessage(requireActivity(), errorMessage)
                }
            })
    }

    override fun initializeRecyclerView() {
        super.initializeRecyclerView()
        binding!!.idRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = OrderStatusAdapter(
            moduleType = moduleType,
            modelList = modelList,
            object : OnEventOccurListener() {
                override fun getEventData(
                    `object`: Any?,
                    actionType: ActionType?,
                    adapterPosition: Int
                ) {
                    super.getEventData(`object`, actionType, adapterPosition)
                    startActivity(
                        Intent(context, ExploreOrderActivity::class.java)
                            .putExtra(IntentInterface.INTENT_FOR_MODEL, `object` as OrderModel)
                    )
                }
            })
        binding!!.idRecyclerView.adapter = adapter
    }

    override fun initializeEmptyView(isEmpty: Boolean) {
        super.initializeEmptyView(isEmpty)
        binding!!.root.findViewById<LinearLayout>(R.id.id_linear_inbox_empty).visibility =
            if (isEmpty) View.VISIBLE else
                View.GONE
        binding!!.root.findViewById<TextView>(R.id.id_text_inbox_description).text =
            "What is stopping you? Click on button Capture Your Shopping List"
    }

    companion object {
        fun get(moduleType: Int): OrderListFrag {
            val frag = OrderListFrag()
            val bundle = Bundle()
            bundle.putInt(IntentInterface.INTENT_FOR_MODULE_TYPE, moduleType)
            frag.arguments = bundle
            return frag
        }
    }

}