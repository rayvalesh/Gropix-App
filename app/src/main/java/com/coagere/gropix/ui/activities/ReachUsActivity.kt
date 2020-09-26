package com.coagere.gropix.ui.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.coagere.gropix.R
import com.coagere.gropix.jetpack.entities.ContactModel
import com.coagere.gropix.jetpack.repos.UserRepo
import com.coagere.gropix.prefs.UserStorage
import com.coagere.gropix.utils.MyApplication.Companion.isLoggedIn
import com.coagere.gropix.utils.UtilityClass
import com.tc.utils.elements.BaseActivity
import com.tc.utils.utils.helpers.HelperActionBar.setSupportActionBar
import com.tc.utils.variables.abstracts.OnEventOccurListener
import kotlinx.android.synthetic.main.activity_reach_us.*
import tk.jamun.ui.snacks.MySnackBar

/**
 * Activity for user feedback or query to server side
 */
class ReachUsActivity : BaseActivity() {
    private var model: ContactModel? = null
    private var utilityClass: UtilityClass? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reach_us)
        model = ContactModel()
        utilityClass = UtilityClass(this)
        setToolbar()
        initializeView()
        initializeListeners()
    }

    override fun setToolbar() {
        super.setToolbar()
        val toolbar = findViewById<Toolbar>(R.id.id_app_bar)
        setSupportActionBar(toolbar)
        setSupportActionBar(supportActionBar, R.string.string_activity_name_contact_us)
    }

    override fun initializeView() {
        super.initializeView()
        if (isLoggedIn) {
            id_edit_name.setText(UserStorage.instance.name)
            if (!UserStorage.instance.email.isNullOrEmpty())
                id_edit_email.setText(UserStorage.instance.email)
        }
    }

    override fun initializeListeners() {
        super.initializeListeners()
        id_button_submit.setOnClickListener { v: View? -> submitClick() }
    }

    private fun submitClick() {
        if (validate()) {
            utilityClass!!.startProgressBar()
            id_button_submit.visibility = View.GONE
            UserRepo().postFeedback(model!!, object : OnEventOccurListener() {
                override fun getEventData(`object`: Any) {
                    super.getEventData(`object`)
                    utilityClass!!.closeProgressBar()
                    Toast.makeText(this@ReachUsActivity, getString(R.string.string_toast_success_feedback), Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }

                override fun onErrorResponse(`object`: Any, errorMessage: String) {
                    super.onErrorResponse(`object`, errorMessage)
                    utilityClass!!.closeProgressBar()
                    id_button_submit.visibility = View.VISIBLE
                    MySnackBar.getInstance().showSnackBarForMessage(this@ReachUsActivity, errorMessage)
                }
            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    private fun validate(): Boolean {
        if (utilityClass!!.checkEditTextEmpty(id_edit_name, resources.getInteger(R.integer.validation_min_name), findViewById(R.id.id_text_error_name))) return false
        if (utilityClass!!.checkEmailEditTextEmpty(id_edit_email!!, resources.getInteger(R.integer.validation_min_email), findViewById(R.id.id_text_error_email))) return false
        if (utilityClass!!.checkEditTextEmpty(id_edit_message, resources.getInteger(R.integer.validation_min_message), findViewById(R.id.id_text_error_message))) return false
        model!!.name = id_edit_name!!.text.toString()
        model!!.message = id_edit_message!!.text.toString()
        model!!.email = id_edit_email!!.text.toString()
        return true
    }
}