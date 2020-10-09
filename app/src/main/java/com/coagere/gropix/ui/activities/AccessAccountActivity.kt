package com.coagere.gropix.ui.activities

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.text.Editable
import android.text.Selection
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.coagere.gropix.R
import com.coagere.gropix.databinding.ActivityAccessAccountBinding
import com.coagere.gropix.jetpack.viewmodels.UserVM
import com.coagere.gropix.prefs.UserStorage
import com.coagere.gropix.utils.UtilityClass
import com.tc.utils.elements.BaseActivity
import com.tc.utils.utils.utility.CheckConnection
import com.tc.utils.variables.abstracts.OnEventOccurListener
import com.tc.utils.variables.interfaces.Constants
import tk.jamun.ui.snacks.MySnackBar


class AccessAccountActivity : BaseActivity(), View.OnClickListener {
    private val utilityClass: UtilityClass by lazy { UtilityClass(this) }
    private var isAdded = false
    private var binding: ActivityAccessAccountBinding? = null
    private val viewModel: UserVM by lazy { ViewModelProvider(this).get(UserVM::class.java) }
    private var count: Int = 1
    private var countDownTimer: CountDownTimer? = null
    private var isTimerRunning = false
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (binding == null) {
            binding = ActivityAccessAccountBinding.inflate(LayoutInflater.from(this))
            binding.apply {
                this!!.clickListener = this@AccessAccountActivity
            }
        }
        lifecycleScope.launchWhenCreated {
            setContentView(binding!!.root)
            initializeViewModel()
            initializeListeners()
            initializeRecyclerView()
        }
    }

    override fun initializeListeners() {
        binding!!.idEditNumber.setText("")
        binding!!.idEditNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length < 4) {
                    binding!!.idEditNumber.setText(getString(R.string.string_label_default_country))
                    isAdded = true
                    Selection.setSelection(
                        binding!!.idEditNumber.text,
                        binding!!.idEditNumber.text!!.length
                    )
                }
                if (s.toString().isEmpty()) {
                    isAdded = false
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
        })
        binding!!.idEditNumber.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus && (binding!!.idEditNumber.text!!.isEmpty() || !binding!!.idEditNumber.text!!.startsWith(
                    getString(R.string.string_label_default_country)
                ))
            ) {
                binding!!.idEditNumber.setText(getString(R.string.string_label_default_country))
                isAdded = true
                Selection.setSelection(
                    binding!!.idEditNumber.text,
                    binding!!.idEditNumber.text!!.length
                )
            }
        }
        binding!!.idEditOtp.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length == 4) {
                    onClickSubmit()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length == 1) {
                    utilityClass.closeProgressDialog(
                        progressView = binding!!.idProgressBarOtp,
                        hideView = false
                    )
                    handler.removeCallbacks(runnable)
                    utilityClass.setEditTextMaxLength(binding!!.idEditOtp, 4)
                    binding!!.idEditOtp.hint = ""
                }
            }

        })

    }

    private fun onClickContact() {
        startActivity(Intent(this@AccessAccountActivity, ReachUsActivity::class.java))
    }

    private fun onClickOtp() {
        utilityClass.hideSoftKeyboard()
        if (validate() && CheckConnection.checkConnection(this)) {
            utilityClass.startProgressBar()
            binding!!.idEditNumber.isEnabled = false
            binding!!.idParentButtonSubmit.isEnabled = false
            viewModel.performAccessAccount(
                binding!!.idEditNumber.text.toString()
                    .substring(4, binding!!.idEditNumber.text.toString().length),
                object : OnEventOccurListener() {
                    override fun onErrorResponse(`object`: Any?, errorMessage: String?) {
                        super.onErrorResponse(`object`, errorMessage)
                        binding!!.idEditNumber.isEnabled = true
                        hideProgressBar()
                        MySnackBar.getInstance()
                            .showSnackBarForMessage(this@AccessAccountActivity, errorMessage)
                    }

                    override fun getEventData(`object`: Any?) {
                        super.getEventData(`object`)
                        showOtpView()
                        hideProgressBar()
                    }
                })
        }
    }

    private var processingCount = -1

    private val runnable = Runnable {
        processingCount += 1
        when (processingCount) {
            0 -> {
                binding!!.idEditOtp.hint = "Processing"
            }
            1 -> {
                binding!!.idEditOtp.hint = "Processing."
            }
            2 -> {
                binding!!.idEditOtp.hint = "Processing.."
            }
            else -> {
                processingCount = -1
                binding!!.idEditOtp.hint = "Processing..."
            }
        }
        callRunnable()
    }

    private fun callRunnable() {
        handler.postDelayed(runnable, 1000)
    }

    private fun showOtpView() {
        utilityClass.setEditTextMaxLength(binding!!.idEditOtp, 14)
        utilityClass.showProgressDialog(null, binding!!.idProgressBarOtp, false)
        binding!!.idEditOtp.requestFocus()
        callRunnable()
        setAutoSmsReceiver()
        binding!!.idParentCheckbox.visibility = View.GONE
        binding!!.idParentOtp.visibility = View.VISIBLE
        binding!!.idTextButton.text = getString(R.string.string_button_name_verify_otp)
        startTimer()
    }

    private fun hideOtpView() {
        binding!!.idEditOtp.setText("")
        utilityClass.closeProgressDialog(
            progressView = binding!!.idProgressBarOtp,
            hideView = false
        )
        utilityClass.closeProgressBar()
        binding!!.idProgressBar.setImageResource(R.drawable.icon_vd_arrow_forward)
        binding!!.idProgressBar.visibility = View.VISIBLE
        handler.removeCallbacks(runnable)
        binding!!.idTextButton.text = getString(R.string.string_button_name_submit_otp)
        binding!!.idParentCheckbox.visibility = View.VISIBLE
        binding!!.idEditNumber.isEnabled = true
        binding!!.idEditNumber.requestFocus()
        binding!!.idParentOtp.visibility = View.GONE
    }

    private fun setAutoSmsReceiver() {
//        SMSReceiver.setUpEverything(this) { messageText: String ->
//            binding!!.idEditOtp.setText(messageText)
//            cancelAutoPicker()
//        }
    }

    private fun cancelAutoPicker() {
//        StartAndStopReceivers().disableBroadcastReceiver(this, SMSReceiver::class.java)
    }

    private fun onClickSubmit() {
        utilityClass.hideSoftKeyboard()
        if (validateOtp() && CheckConnection.checkConnection(this)) {
            binding!!.idParentButtonSubmit.isEnabled = false
            binding!!.idEditOtp.isEnabled = false
            binding!!.idEditOtp.setTextColor(
                ContextCompat.getColor(
                    this@AccessAccountActivity,
                    R.color.colorTextWhite
                )
            )
            utilityClass.startProgressBar()
            viewModel.performOtpVerification(
                binding!!.idEditNumber.text.toString()
                    .substring(4, binding!!.idEditNumber.text.toString().length),
                binding!!.idEditOtp.text.toString(),
                object : OnEventOccurListener() {
                    override fun onErrorResponse(`object`: Any, errorMessage: String) {
                        hideProgressBar()
                        MySnackBar.getInstance()
                            .showSnackBarForMessage(this@AccessAccountActivity, errorMessage)
                    }

                    override fun getEventData(`object`: Any) {
                        super.getEventData(`object`)
                        UserStorage.instance.mobileNumber = binding!!.idEditNumber.text.toString()
                            .substring(4, binding!!.idEditNumber.text.toString().length)
                        hideProgressBar()
                        startActivity(
                            Intent(
                                this@AccessAccountActivity,
                                MainActivity::class.java
                            )
                        )
                        finish()
                    }
                })
        }
    }

    private fun hideProgressBar() {
        utilityClass.closeProgressBar()
        Handler().postDelayed({
            binding!!.idProgressBar.setImageResource(R.drawable.icon_vd_arrow_forward)
            binding!!.idProgressBar.visibility = View.VISIBLE
            binding!!.idParentButtonSubmit.isEnabled = true
            binding!!.idEditOtp.isEnabled = true

        }, Constants.THREAD_TIME_DELAY)
    }


    private fun onClickResend() {
        utilityClass.hideSoftKeyboard()
        if (CheckConnection.checkConnection(this)) {
            binding!!.idTextTimer.isEnabled = false
            utilityClass.showProgressView(
                binding!!.idTextTimer,
                progressView = binding!!.idProgressBarResend,
                true
            )
            viewModel.performResendOtp(
                object : OnEventOccurListener() {
                    override fun onErrorResponse(`object`: Any, errorMessage: String) {
                        binding!!.idTextTimer.isEnabled = true
                        utilityClass.closeProgressDialog(
                            binding!!.idTextTimer,
                            progressView = binding!!.idProgressBarResend
                        )
                        MySnackBar.getInstance()
                            .showSnackBarForMessage(this@AccessAccountActivity, errorMessage)
                    }

                    override fun getEventData(`object`: Any) {
                        count += 1
                        utilityClass.closeProgressDialog(
                            binding!!.idTextTimer,
                            progressView = binding!!.idProgressBarResend
                        )
                        MySnackBar.getInstance().showSnackBarForMessage(
                            this@AccessAccountActivity,
                            getString(R.string.string_toast_otp_resent)
                                    + Constants.SPACE + binding!!.idEditNumber.text.toString() + "!"
                        )
                        startTimer()
                    }
                })
        }
    }

    private fun startTimer() {
        binding!!.idTextTimer.isEnabled = false
        isTimerRunning = true
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer((count * 60000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                var seconds = (millisUntilFinished / 1000).toInt()
                val minutes = seconds / 60
                seconds %= 60
                binding!!.idTextTimer.text =
                    "Resend : " + (String.format("%02d", minutes) + ":" + String.format(
                        "%02d",
                        seconds
                    ))
            }

            override fun onFinish() {
                binding!!.idTextTimer.isEnabled = true
                isTimerRunning = false
                binding!!.idTextTimer.text = getString(R.string.string_button_name_resend_otp)
            }
        }
        countDownTimer!!.start()
    }


    private fun validateOtp(): Boolean {
        return !utilityClass.checkEditTextEmpty(
            binding!!.idEditOtp, resources.getInteger(R.integer.validation_min_otp),
            findViewById(R.id.id_text_error_otp)
        )
    }

    private fun validate(): Boolean {
        if (binding!!.idEditNumber.text!!.length != 14) {
            binding!!.idTextErrorNumber.visibility = View.VISIBLE
            return false
        } else if (!binding!!.idEditNumber.text!!.startsWith(
                getString(R.string.string_label_default_country)
            )
        ) {
            binding!!.idTextErrorNumber.visibility = View.VISIBLE
            return false
        }

        binding!!.idTextErrorNumber.visibility = View.GONE
        if (!binding!!.idCheckbox.isChecked) {
            binding!!.idCheckbox.isChecked = true
            return false
        }
        binding!!.idEditNumber.setTextColor(
            ContextCompat.getColor(
                this@AccessAccountActivity,
                R.color.colorTextWhite
            )
        )
        UserStorage.instance.mobileNumber =
            binding!!.idEditNumber.toString().substring(4, binding!!.idEditNumber.toString().length)
        return true
    }

    private fun cancelTimer() {
        isTimerRunning = false
        countDownTimer?.cancel()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        utilityClass.hideSoftKeyboard()
        binding!!.idEditNumber.clearFocus()
        return super.dispatchTouchEvent(ev)
    }

    override fun onDestroy() {
        closeEverything()
        super.onDestroy()
    }

    override fun closeEverything() {
        cancelTimer()
        viewModel.clearEverything()
        cancelAutoPicker()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.id_parent_button_submit -> {
                if (binding!!.idParentOtp.visibility == View.GONE) {
                    onClickOtp()
                } else
                    onClickSubmit()
            }
            R.id.id_parent_button_contact -> {
                onClickContact()
            }
            R.id.id_button_edit -> {
                hideOtpView()
//            viewModel.cancelOtpCall()
                cancelTimer()
            }
            R.id.id_text_timer -> {
                onClickResend()
            }

        }
    }

}

