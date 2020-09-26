package com.coagere.gropix.utils

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.google.firebase.FirebaseApp
import com.coagere.gropix.R
import com.coagere.gropix.prefs.UserStorage
import tk.jamun.ui.snacks.MySnackBar
import tk.jamun.volley.helpers.VolleyNeeds
import tk.jamun.volley.helpers.VolleyValues
import tk.jamun.volley.main.VolleySingleton

class MyApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        instance = this
        appContext = instance.applicationContext
        VolleySingleton.setInstance(this)
        VolleyValues.get().imageCompressValue = 80
        checkForLogin()
        MySnackBar.getInstance().setParentId(R.id.id_coordinator_layout)
        setAndRefreshVolleyHeaderCredentials()
    }

    private fun checkForLogin() {
        isLoggedIn = UserStorage.instance.isLoggedIn
    }


    fun setAndRefreshVolleyHeaderCredentials() {
        VolleyNeeds.get().upHeaders = UserStorage.instance.headerCredentials
    }


    companion object {
        @get:Synchronized
        @Volatile
        lateinit var instance: MyApplication

        var isLoggedIn = false

        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }

        @JvmStatic
        lateinit var appContext: Context

    }
}
