package com.coagere.gropix.jetpack.viewmodels

import androidx.lifecycle.ViewModel
import com.coagere.gropix.jetpack.entities.UserModel
import com.tc.utils.variables.abstracts.OnEventOccurListener

class UserVM : ViewModel() {

    fun performOtpVerification(number: String, otp: String, listener: OnEventOccurListener) {
    }

    fun performResendOtp(number: String, listener: OnEventOccurListener) {
    }

    fun getProfileData(listener: OnEventOccurListener) {
    }

    fun performAccessAccount(number: String, listener: OnEventOccurListener) {
    }

    fun clearEverything() {
    }

    fun performUpdateProfile(
        model: UserModel,
        referralCode: String?,
        listener: OnEventOccurListener
    ) {
    }


}
