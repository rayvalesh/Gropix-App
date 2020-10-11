package com.coagere.gropix.ui.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.coagere.gropix.R
import com.coagere.gropix.databinding.ActivityReachUsBinding
import com.coagere.gropix.jetpack.entities.ContactModel
import com.coagere.gropix.jetpack.repos.UserRepo
import com.coagere.gropix.prefs.UserStorage
import com.coagere.gropix.utils.MyApplication.Companion.isLoggedIn
import com.coagere.gropix.utils.UtilityClass
import com.tc.utils.elements.BaseActivity
import com.tc.utils.utils.helpers.HelperActionBar
import com.tc.utils.utils.helpers.Utils
import com.tc.utils.variables.abstracts.OnEventOccurListener
import kotlinx.android.synthetic.main.activity_reach_us.*
import tk.jamun.ui.snacks.MySnackBar

/**
 * Activity for user feedback or query to server side
 */
class ReachUsActivity : BaseActivity(), View.OnClickListener {
    private var model: ContactModel? = null
    private var binding: ActivityReachUsBinding? = null
    private var utilityClass: UtilityClass? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReachUsBinding.inflate(LayoutInflater.from(this))
        binding!!.apply {
            clickListener = this@ReachUsActivity
            executePendingBindings()
        }
        lifecycleScope.launchWhenCreated {
            setContentView(binding!!.root)
            model = ContactModel()
            utilityClass = UtilityClass(this@ReachUsActivity)
            setToolbar()
            initializeView()
            initializeListeners()
        }
    }

    override fun setToolbar() {
        super.setToolbar()
        HelperActionBar.setSupportActionBar(
            activity = this@ReachUsActivity,
            view = binding!!.idAppBar,
            title = getString(
                R.string.string_activity_name_contact_us,
            )
        )
    }


    override fun initializeView() {
        super.initializeView()
        if (isLoggedIn) {
            id_edit_name.setText(UserStorage.instance.name)
            if (!UserStorage.instance.email.isNullOrEmpty())
                id_edit_email.setText(UserStorage.instance.email)
        }
    }


    /**
     * Api call to post Information
     */
    private fun submitClick() {
        if (validate()) {
            utilityClass!!.startProgressBar()
            id_button_submit.visibility = View.GONE
            UserRepo().postFeedback(model!!, object : OnEventOccurListener() {
                override fun getEventData(`object`: Any) {
                    super.getEventData(`object`)
                    utilityClass!!.closeProgressBar()
                    Toast.makeText(
                        this@ReachUsActivity,
                        getString(R.string.string_toast_success_feedback),
                        Toast.LENGTH_SHORT
                    ).show()
                    onBackPressed()
                }

                override fun onErrorResponse(`object`: Any, errorMessage: String) {
                    super.onErrorResponse(`object`, errorMessage)
                    utilityClass!!.closeProgressBar()
                    id_button_submit.visibility = View.VISIBLE
                    MySnackBar.getInstance()
                        .showSnackBarForMessage(this@ReachUsActivity, errorMessage)
                }
            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    private fun validate(): Boolean {
        if (utilityClass!!.checkEditTextEmpty(
                id_edit_name,
                resources.getInteger(R.integer.validation_min_name),
                findViewById(R.id.id_text_error_name)
            )
        ) return false
        if (utilityClass!!.checkEmailEditTextEmpty(
                id_edit_email!!,
                resources.getInteger(R.integer.validation_min_email),
                findViewById(R.id.id_text_error_email)
            )
        ) return false
        if (utilityClass!!.checkEditTextEmpty(
                id_edit_message,
                resources.getInteger(R.integer.validation_min_message),
                findViewById(R.id.id_text_error_message)
            )
        ) return false
        model!!.name = id_edit_name!!.text.toString()
        model!!.message = id_edit_message!!.text.toString()
        model!!.email = id_edit_email!!.text.toString()
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onClick(v: View?) {
        submitClick()
    }
}