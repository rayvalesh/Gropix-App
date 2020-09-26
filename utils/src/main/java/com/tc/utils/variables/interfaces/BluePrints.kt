package com.tc.utils.variables.interfaces

interface BluePrints {
    interface ofActivity {
        fun setToolbar()
    }

    interface ofSheet {
        fun initializeSheet()
    }

    interface ofView {
        fun initializeView()
        fun initializeListeners()
        fun initializeData()
        fun initializePicker()
        fun initializeViewModel()
        fun initializeTabView()
        fun closeEverything()
    }

    interface ofRecycler {
        fun initializeRecyclerView()
        fun initializeEmptyView(isEmpty: Boolean)
    }

    interface ofFrag {
        fun initializeFragsView()
    }

    interface ofMap {
        fun initializeMapView()
        fun initializeMapThings()
        fun initializeMapListeners()
        fun goToMyLocation()
        fun goToMapLocation(latLng: Any?)
        fun initializeMarker()
        fun initializeMarkers(`object`: Any?)
        fun initializeMapData()
    }
}