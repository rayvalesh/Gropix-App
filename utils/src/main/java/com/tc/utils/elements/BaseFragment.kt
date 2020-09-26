package com.tc.utils.elements

import androidx.fragment.app.Fragment
import com.tc.utils.variables.interfaces.BluePrints

open class BaseFragment : Fragment(), BluePrints.ofView,BluePrints.ofMap, BluePrints.ofFrag, BluePrints.ofRecycler {
    private var mIsVisibleToUser: Boolean =
        false // you can see this variable may absolutely <=> getUserVisibleHint() but it not. Currently, after many test I find that

    /**
     * This method will call at first time viewpager created and when we switch between each page
     * NOT called when we go to background or another activity (fragment) when we go back
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
       mIsVisibleToUser = isVisibleToUser
    }

    override fun initializeMapView() {
    }

    override fun initializeMapThings() {
    }

    override fun initializeMapListeners() {
    }

    override fun goToMyLocation() {
    }

    override fun goToMapLocation(latLng: Any?) {
    }

    override fun initializeMarker() {
    }

    override fun initializeMarkers(`object`: Any?) {
    }

    override fun initializeMapData() {
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
