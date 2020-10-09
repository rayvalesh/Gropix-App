package com.coagere.gropix.ui.frags

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.coagere.gropix.R
import com.coagere.gropix.databinding.FragOrderListBinding
import com.coagere.gropix.jetpack.entities.OrderModel
import com.coagere.gropix.jetpack.viewmodels.OrderVM
import com.coagere.gropix.ui.activities.ExploreOrderActivity
import com.coagere.gropix.ui.adapters.OrderStatusAdapter
import com.coagere.gropix.utils.HelperLogout
import com.coagere.gropix.utils.UtilityClass
import com.tc.utils.elements.BaseFragment
import com.tc.utils.utils.helpers.Utils
import com.tc.utils.variables.abstracts.OnEventOccurListener
import com.tc.utils.variables.enums.ActionType
import com.tc.utils.variables.interfaces.IntentInterface
import tk.jamun.ui.snacks.MySnackBar

/**
 * @author Jatin Sahgal by 28-Sep-2020 12:25
 */
class OrderListFrag : BaseFragment() {
    private var binding: FragOrderListBinding? = null
    private var parentListener: OnEventOccurListener? = null
    private val utilityClass: UtilityClass by lazy {
        UtilityClass(
            requireActivity(),
            requireView()
        )
    }
    private val viewModel: OrderVM by lazy { ViewModelProvider(this).get(OrderVM::class.java) }
    private var modelList: ArrayList<OrderModel> = arrayListOf()
    private var adapter: OrderStatusAdapter = OrderStatusAdapter(
        modelList = modelList, object : OnEventOccurListener() {
            override fun getEventData(
                `object`: Any?,
                actionType: ActionType?,
                adapterPosition: Int
            ) {
                super.getEventData(`object`, actionType, adapterPosition)
                startActivity(
                    Intent(requireActivity(), ExploreOrderActivity::class.java)
                        .putExtra(IntentInterface.INTENT_FOR_MODEL, `object` as OrderModel)
                )
            }
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (binding == null) {
            binding = FragOrderListBinding.inflate(
                LayoutInflater.from(requireContext()),
                container,
                false
            )
            lifecycleScope.launchWhenCreated {
                initializeViewModel()
                initializeRecyclerView()
            }
        }
        return binding!!.root
    }

    override fun initializeViewModel() {
        super.initializeViewModel()
        viewModel.getApiOrderList(
            modelList = modelList,
            object : OnEventOccurListener() {
                override fun getEventData(`object`: Any?) {
                    super.getEventData(`object`)
                    adapter.notifyAdapterDataSetChanged()
                    initializeEmptyView(modelList.isNullOrEmpty())
                }

                override fun onErrorResponse(`object`: Any?, errorMessage: String?) {
                    super.onErrorResponse(`object`, errorMessage)
                    if (utilityClass.isUnAuthrized(`object`)) {
                        HelperLogout.logMeOut(
                            requireActivity(),
                            object : OnEventOccurListener() {})
                    } else {
                        MySnackBar.getInstance()
                            .showSnackBarForMessage(requireActivity(), errorMessage)
                    }
                }
            })
    }

    override fun initializeRecyclerView() {
        super.initializeRecyclerView()
        binding!!.idRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding!!.idRecyclerView.adapter = adapter
    }


    override fun initializeEmptyView(isEmpty: Boolean) {
        super.initializeEmptyView(isEmpty)
        Utils.setVisibility(
            binding!!.root.findViewById<LinearLayout>(R.id.id_linear_inbox_empty),
            isEmpty
        )
        parentListener?.getEventData(isEmpty)
    }

    fun bindListener(listener: OnEventOccurListener) {
        parentListener = listener
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