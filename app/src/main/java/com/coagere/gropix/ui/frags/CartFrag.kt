//package com.coagere.gropix.ui.frags
//
//import android.app.Activity
//import android.content.Intent
//import android.os.Bundle
//import android.os.Handler
//import android.os.Parcelable
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.coagere.gropix.jetpack.entities.AddressModel
//import com.coagere.gropix.prefs.UserStorage
//import com.coagere.gropix.utils.elements.BaseFragment
//import com.coagere.gropix.jetpack.entities.ItemModel
//import com.coagere.gropix.jetpack.entities.PriceModel
//import com.tc.utils.receivers.ServiceReceiver
//import com.tc.utils.utils.utility.isNull
//import com.tc.utils.variables.abstracts.OnEventOccurListener
//import com.tc.utils.variables.enums.ActionType
//import com.tc.utils.variables.interfaces.Constants
//import com.tc.utils.variables.interfaces.IntentInterface
//import kotlinx.android.synthetic.main.frag_cart.*
//
//class CartFrag : BaseFragment(), ServiceReceiver.Receiver {
//    private lateinit var adapter: ItemAdapter
//    private var itemsSheet: ItemsSheet? = null
//    var modelList: MutableList<ItemModel>? = null
//    private var serviceModel: ItemModel? = null
//    private var parentPosition: Int = 0
//    private var parentListener: OnEventOccurListener? = null
//    var modelAddress: AddressModel? = null
//    private var intentCurrentAddress: Intent? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        this.modelList = UserStorage.instance.cartItems.toMutableList()
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.frag_cart, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        initializeView()
//        initializeRecyclerView()
//        initializeListeners()
//        if (UserStorage.instance.latitude != 0.0) {
//            currentAddress()
//        }
//    }
//
//    fun currentAddress() {
//        val modelAddress = AddressModel()
//        modelAddress.latitude = UserStorage.instance.latitude
//        modelAddress.longitude = UserStorage.instance.longitude
//        if (intentCurrentAddress != null) {
//            requireContext().stopService(intentCurrentAddress)
//        }
//        intentCurrentAddress =
//            Intent(Intent.ACTION_SYNC, null, requireContext(), IntentGetAddress::class.java)
//                .putExtra(IntentInterface.INTENT_FOR_MODEL, modelAddress)
//                .putExtra(
//                    ServiceReceiver.TAG_RECEIVER,
//                    ServiceReceiver(Handler(), this, 1)
//                )
//        requireContext().startService(intentCurrentAddress)
//    }
//
//    override fun onReceiveResult(requestCode: Int, resultCode: Int, resultData: Bundle) {
//        when (resultCode) {
//            ServiceReceiver.STATUS_FINISHED -> if (resultData.getParcelable<Parcelable?>(
//                    IntentInterface.INTENT_FOR_MODEL
//                ) != null
//            ) {
//                modelAddress = resultData.getParcelable(IntentInterface.INTENT_FOR_MODEL)
//                modelAddress?.let {
//                    if (it.street != null && it.street != "") {
//                        id_text_address.text =
//                            "${it.street}, ${it.city}, ${it.state} (${it.pinCode})"
//                    } else if (it.other != null && it.other != "") {
//                        id_text_address.text = it.other
//                    }
//                }
//            }
//        }
//    }
//
//    override fun initializeListeners() {
//        super.initializeListeners()
//        id_button_address.setOnClickListener {
//            onClickAddress()
//        }
//        id_button_add_more.setOnClickListener {
//            requireActivity().setResult(Activity.RESULT_CANCELED)
//            requireActivity().finish()
//        }
//
//    }
//
//    override fun initializeView() {
//        super.initializeView()
//        id_text_name.text = UserStorage.instance.name
//    }
//
//    override fun initializeRecyclerView() {
//        super.initializeRecyclerView()
//        id_recycler_view.layoutManager = LinearLayoutManager(requireContext())
//        adapter = ItemAdapter(Constants.MODULE_CART, object : OnEventOccurListener() {
//            override fun getEventData(
//                `object`: Any?,
//                actionType: ActionType?,
//                adapterPosition: Int
//            ) {
//                super.getEventData(`object`, actionType, adapterPosition)
//                when (actionType) {
//                    ActionType.ACTION_CANCEL -> {
//                        modelList?.remove(`object` as ItemModel)
//                        adapter.notifyAdapterItemRemoved(adapterPosition)
//                        UserStorage.instance.cartItems = modelList!!.toTypedArray()
//                        parentListener?.getEventData(ActionType.ACTION_BOOK)
//                    }
//                    else -> {
//                        serviceModel = `object` as ItemModel
//                        parentPosition = adapterPosition
//                        if (serviceModel!!.priceList.size == 1)
//                            listener.getEventData(`object`, actionType)
//                        else {
//                            showBottomSheet()
//                        }
//                    }
//                }
//            }
//        })
//        id_recycler_view.adapter = adapter
//        id_recycler_view.isNestedScrollingEnabled = false
//        adapter.notifyAdapterDataSetChanged(modelList!!)
//    }
//
//    private fun showBottomSheet() {
//        serviceModel?.let {
//            if (isNull(itemsSheet))
//                itemsSheet = ItemsSheet()
//            itemsSheet?.bindListener(
//                it.categoryId!!,
//                it.categoryName,
//                serviceModel!!, listener
//            )
//            itemsSheet?.showDialog(childFragmentManager)
//        }
//    }
//
//    private val listener = object : OnEventOccurListener() {
//        override fun getEventData(
//            `object`: Any?,
//            actionType: ActionType?
//        ) {
//            super.getEventData(`object`)
//            var selectedCount = 0
//            for (priceModel: PriceModel in serviceModel!!.priceList) {
//                selectedCount += priceModel.selectedCount
//            }
//            serviceModel?.times = selectedCount
//            modelList!!.set(parentPosition, serviceModel!!)
//            adapter.notifyItemChanged(parentPosition)
//            if (serviceModel!!.priceList.size > 1)
//                itemsSheet?.initializeData(serviceModel!!)
//            parentListener?.getEventData(ActionType.ACTION_BOOK)
//            UserStorage.instance.cartItems = modelList!!.toTypedArray()
//        }
//    }
//
//    fun bindListener(parentListener: OnEventOccurListener) {
//        this.parentListener = parentListener
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        if (intentCurrentAddress != null) {
//            requireContext().stopService(intentCurrentAddress)
//        }
//    }
//
//
//}