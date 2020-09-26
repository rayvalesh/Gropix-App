package com.tc.utils.elements

import androidx.appcompat.app.AppCompatActivity
import com.tc.utils.variables.interfaces.BluePrints


open class BaseActivity : AppCompatActivity(), BluePrints.ofView, BluePrints.ofActivity,
    BluePrints.ofSheet, BluePrints.ofFrag, BluePrints.ofRecycler {


    override fun initializeSheet() {

    }

    override fun initializeView() {

    }

    override fun initializeListeners() {

    }

    override fun initializePicker() {

    }

    override fun initializeData() {

    }

    override fun initializeViewModel() {

    }

    override fun initializeTabView() {

    }

    override fun initializeRecyclerView() {

    }

    override fun initializeEmptyView(isEmpty: Boolean) {

    }

    override fun initializeFragsView() {

    }

    override fun closeEverything() {

    }


    override fun setToolbar() {

    }
}
