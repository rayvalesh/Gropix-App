package com.coagere.gropix.jetpack.viewmodels

import androidx.lifecycle.ViewModel
import com.coagere.gropix.jetpack.repos.UserRepo
import com.tc.utils.variables.abstracts.OnEventOccurListener

class UserVM : ViewModel() {

    fun performOtpVerification(number: String, otp: String, listener: OnEventOccurListener) {
        UserRepo.instance.apiOtpVerification(number, otp, listener)
    }

    fun performResendOtp( listener: OnEventOccurListener) {
        UserRepo.instance.apiResendOtp(listener)
    }


    fun performAccessAccount(number: String, listener: OnEventOccurListener) {
        UserRepo.instance.apiAccessAccount(number, listener)
    }

    fun clearEverything() {
        UserRepo.instance.cancelApiCalls()
    }

    fun cancelOtpCall() {
        UserRepo.instance.cancelOtpCall()
    }


}
