package com.tc.utils.elements

import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import com.tc.utils.variables.interfaces.BluePrints


 open class BaseSheetDialogFragment : BottomSheetDialogFragment(), BluePrints.ofView, BluePrints.ofFrag,
    BluePrints.ofRecycler {
    private var mIsVisibleToUser: Boolean =
        false // you can see this variable may absolutely <=> getUserVisibleHint() but it not. Currently, after many test I find that

    /**
     * This method will call at first time viewpager created and when we switch between each page
     * NOT called when we go to background or another activity (fragment) when we go back
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        mIsVisibleToUser = isVisibleToUser
    }

    override fun initializeView() {

    }

    override fun initializePicker() {

    }

    override fun initializeListeners() {

    }

    override fun initializeData() {

    }

    override fun initializeViewModel() {

    }

    override fun initializeTabView() {

    }

    override fun closeEverything() {

    }

    override fun initializeRecyclerView() {

    }

    override fun initializeEmptyView(isEmpty: Boolean) {

    }


    override fun initializeFragsView() {

    }
}
