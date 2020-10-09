package com.coagere.gropix.ui.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.coagere.gropix.R
import com.coagere.gropix.databinding.ActivityLauncherBinding
import com.coagere.gropix.jetpack.repos.UserRepo
import com.coagere.gropix.prefs.TempStorage
import com.coagere.gropix.utils.MyApplication
import com.tc.utils.elements.BaseActivity
import com.tc.utils.utils.helpers.Utils
import com.tc.utils.utils.utility.CheckConnection
import com.tc.utils.variables.abstracts.OnEventOccurListener
import kotlinx.android.synthetic.main.activity_launcher.*

class LauncherActivity : BaseActivity() {
    private var handler: Handler = Handler()
    private var binding: ActivityLauncherBinding? = null
    private var runnableHome = Runnable { this.homeStart() }
    private var runnable: Runnable = object : Runnable {
        override fun run() {
            val progress = binding!!.idProgressBar.progress
            when {
                progress > 98 -> {
                    homeStart()
                }
                progress >= 70 -> {
                    binding?.idTextProgress?.text =
                        getString(R.string.string_label_setting_up)
                    binding?.idProgressBar?.progress = progress + 4
                    handler.postDelayed(this, 200)
                }
                progress >= 50 -> {
                    onClickPostDevice()
                    binding?.idProgressBar?.progress = progress + 4
                    binding!!.idTextProgress.text = getString(R.string.string_label_config)
                }
                else -> {
                    binding?.idProgressBar?.progress = progress + 4
                    handler.postDelayed(this, 200)
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLauncherBinding.inflate(LayoutInflater.from(this))
        lifecycleScope.launchWhenCreated {
            setContentView(binding!!.root)
            Utils.doStatusColorWhite(window)
            initializeView()
            initializeListener()
            checkAndGo()
        }
    }


    private fun onClickPostDevice() {
        UserRepo().apiPostDeviceByApi(object : OnEventOccurListener() {
            override fun getEventData(`object`: Any?) {
                super.getEventData(`object`)
                id_progress_bar.progress = 72
                handler.postDelayed(runnable, 200)
            }

            override fun onErrorResponse(`object`: Any?, errorMessage: String?) {
                super.onErrorResponse(`object`, errorMessage)
            }
        })

    }

    private fun checkAndGo() {
        binding!!.idLinearConnection.visibility = View.GONE
        binding!!.idLinearParent.visibility = View.VISIBLE
        if (CheckConnection.checkCon(this)) {
            if (TempStorage.instance.appRegistration) {
                binding!!.idParent.visibility = View.VISIBLE
                handler.postDelayed(runnableHome, 2000)
            } else {
                binding!!.idLinearProgress.visibility = View.VISIBLE
                handler.postDelayed(runnable, 200)
                TempStorage.instance.appRegistration = true
            }
        } else {
            binding!!.idLinearConnection.visibility = View.VISIBLE
            binding!!.idLinearParent.visibility = View.GONE
            binding!!.idTextContent.setText(R.string.string_error_no_internet)
        }
    }

    private fun initializeListener() {
        binding!!.idButtonTryAgain.setOnClickListener { checkAndGo() }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        closeEverything()
    }

    override fun closeEverything() {
        handler.removeCallbacks(runnable)
        handler.removeCallbacks(runnableHome)
    }

    private fun homeStart() {
        handler.removeCallbacks(runnableHome)
        handler.removeCallbacks(runnable)
        if (MyApplication.isLoggedIn) {
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            startActivity(Intent(this, AccessAccountActivity::class.java))
        }
        finish()
    }
}
