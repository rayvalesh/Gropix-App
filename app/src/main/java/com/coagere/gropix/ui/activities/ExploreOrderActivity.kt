package com.coagere.gropix.ui.activities

import android.os.Bundle
import android.view.LayoutInflater
import com.coagere.gropix.databinding.ActivityExploreOrderBinding
import com.coagere.gropix.jetpack.entities.OrderModel
import com.tc.utils.elements.BaseActivity
import com.tc.utils.variables.interfaces.IntentInterface

/**
 * @author Jatin Sahgal by 28-Sep-2020 13:54
 */
class ExploreOrderActivity : BaseActivity() {
    private var binding: ActivityExploreOrderBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (binding == null) {
            binding = ActivityExploreOrderBinding.inflate(LayoutInflater.from(this))
            binding!!.apply {
                this.model =
                    intent.getParcelableExtra<OrderModel>(IntentInterface.INTENT_FOR_MODEL)
            }
        }
    }


}
