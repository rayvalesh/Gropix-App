package com.coagere.gropix.jetpack.viewmodels

import androidx.lifecycle.ViewModel
import com.coagere.gropix.jetpack.entities.UserModel
import com.coagere.gropix.jetpack.repos.UserRepo
import com.tc.utils.variables.abstracts.OnEventOccurListener

class UserVM : ViewModel() {

    fun performOtpVerification(number: String, otp: String, listener: OnEventOccurListener) {
        UserRepo.instance.apiOtpVerification(number, otp, listener)
    }

    fun performResendOtp(number: String, listener: OnEventOccurListener) {
        UserRepo.instance.apiResendOtp(number,listener)
    }

    fun getProfileData(listener: OnEventOccurListener) {
    }

    fun performAccessAccount(number: String, listener: OnEventOccurListener) {
        UserRepo.instance.apiAccessAccount(number, listener)
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
