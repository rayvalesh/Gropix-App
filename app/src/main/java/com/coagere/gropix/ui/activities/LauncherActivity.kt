package com.coagere.gropix.ui.activities

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.coagere.gropix.R
import com.coagere.gropix.databinding.ActivityLauncherBinding
import com.coagere.gropix.prefs.TempStorage
import com.coagere.gropix.prefs.UserStorage
import com.coagere.gropix.utils.MyApplication
import com.tc.utils.elements.BaseActivity
import com.tc.utils.utils.utility.CheckConnection
import com.tc.utils.variables.interfaces.Constants
import com.tc.utils.variables.interfaces.IntentInterface

class LauncherActivity : BaseActivity() {
    private var handler: Handler = Handler()
    private var binding: ActivityLauncherBinding? = null
    private var runnableHome = Runnable { this.homeStart() }

    private var runnable: Runnable? = null


    private fun onClickPostDevice() {
//        UserRepo().apiGetCommonData(object : OnEventOccurListener() {
//            override fun getEventData(`object`: Any?) {
//                super.getEventData(`object`)
//                TempStorage.instance.saveAppStatus = `object` as Boolean
//            }
//
//            override fun onErrorResponse(`object`: Any?, errorMessage: String?) {
//                super.onErrorResponse(`object`, errorMessage)
//                TempStorage.instance.saveAppStatus = true
//            }
//        })

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (binding == null) {
            binding = ActivityLauncherBinding.inflate(LayoutInflater.from(this))
        }
        lifecycleScope.launchWhenCreated {
            setContentView(binding!!.root)
            initializeView()
            initializeListener()
            checkAndGo()
            handler.postDelayed({
                initializePicker()
            }, Constants.THREAD_TIME_DELAY)
            runnable = object : Runnable {
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
                            binding?.idProgressBar?.progress = progress + 4
                            handler.postDelayed(this, 200)
                            binding!!.idTextProgress.text = getString(R.string.string_label_config)
                        }
                        else -> {
                            binding?.idProgressBar?.progress = progress + 4
                            handler.postDelayed(this, 200)
                        }
                    }
                }
            }

        }
    }

    private fun startAnimation() {
//        val animation = AnimationUtils.loadAnimation(this@LauncherActivity, R.anim.bounce)
//        id_image.animation = animation
//        animation.setAnimationListener(object : Animation.AnimationListener {
//            override fun onAnimationRepeat(p0: Animation?) {
//            }
//
//            override fun onAnimationEnd(p0: Animation?) {
//                binding!!.idParent.visibility = View.VISIBLE
//                binding!!.idParent.animation = AnimationUtils.loadAnimation(this@LauncherActivity, R.anim.alpha)
//            }
//
//            override fun onAnimationStart(p0: Animation?) {
//            }
//
//        })
    }


    private fun checkAndGo() {
        binding!!.idLinearConnection.visibility = View.GONE
        binding!!.idLinearParent.visibility = View.VISIBLE
        this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (CheckConnection.checkCon(this)) {
            if (TempStorage.instance.saveAppStatus) {
                binding!!.idParent.visibility = View.VISIBLE
                handler.postDelayed(runnableHome, 2000)
            } else {
                binding!!.idLinearProgress.visibility = View.VISIBLE
                handler.postDelayed(runnable!!, 200)
                startAnimation()
            }
            onClickPostDevice()
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
        handler.removeCallbacks(runnable!!)
        handler.removeCallbacks(runnableHome)
    }

    private fun homeStart() {
        handler.removeCallbacks(runnableHome)
        handler.removeCallbacks(runnable!!)
        when (UserStorage.instance.lastPage) {
            1 -> startActivity(
                Intent(this, InformationActivity::class.java)
                    .putExtra(IntentInterface.INTENT_COME_FROM, true)
            )
            else -> {
                if (MyApplication.isLoggedIn) {
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    startActivity(Intent(this, AccessAccountActivity::class.java))
                }
            }
        }
        finish()
    }
}
