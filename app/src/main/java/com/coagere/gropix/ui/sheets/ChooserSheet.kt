package com.coagere.gropix.ui.sheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.coagere.gropix.R
import com.tc.utils.elements.BaseSheetDialogFragment
import com.tc.utils.variables.abstracts.OnEventOccurListener
import com.tc.utils.variables.enums.ActionType
import com.tc.utils.variables.enums.ModuleType
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @author Jatin Sahgal by 08-Oct-2020 15:19
 */
class ChooserSheet : BaseSheetDialogFragment(), View.OnClickListener {
    private var parentListener: OnEventOccurListener? = null
    private var moduleType: Int = 0

    init {
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }

    fun showDialog(
        moduleType: Int? = 0,
        parentListener: OnEventOccurListener,
        fragmentManager: FragmentManager
    ) {
        this.moduleType = moduleType!!
        this.parentListener = parentListener
        show(fragmentManager, tag)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sheet_chooser, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
        initializeListeners()
    }

    override fun initializeView() {
        super.initializeView()
        if (moduleType == 1) {
            requireView().findViewById<TextView>(R.id.id_text).text = "Add more pics"
        }
    }

    override fun initializeListeners() {
        super.initializeListeners()
        id_parent_camera.setOnClickListener(this)
        id_parent_manager.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        dismiss()
        when (v?.id) {
            R.id.id_parent_camera -> {
                parentListener?.getEventData(ActionType.ACTION_CAMERA)
            }
            R.id.id_parent_manager -> {
                parentListener?.getEventData(ActionType.ACTION_GALLERY)
            }
        }
    }
}