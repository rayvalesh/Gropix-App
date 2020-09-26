package com.coagere.gropix.utils

import android.app.Activity
import android.content.IntentSender.SendIntentException
import android.location.Location
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.tc.utils.utils.utility.UtilityCheckPermission.MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
import com.tc.utils.utils.utility.UtilityCheckPermission.checkPermission

class HelperLocation {
    private var activity: Activity? = null
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var interfaceGetMyLocation: InterfaceGetMyLocation? = null
    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            for (location in locationResult.locations) {
                interfaceGetMyLocation!!.getLatLng(location.latitude, location.longitude)
            }
        }
    }

    interface InterfaceGetMyLocation {
        fun getLatLng(lat: Double?, lng: Double?)
    }

    fun startPicker(activity: Activity?, interfaceGet: InterfaceGetMyLocation?) {
        interfaceGetMyLocation = interfaceGet
        this.activity = activity
        setAccurate()
    }

    private var mLocationRequest: LocationRequest? = null
    private fun setAccurate() {
        if (checkPermission(
                activity!!,
                MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        ) {
            mLocationRequest = LocationRequest.create()
            mLocationRequest?.interval = 2000
            mLocationRequest?.fastestInterval = 2000
            mLocationRequest?.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
            mLocationRequest?.smallestDisplacement = 5f //added
            val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest!!)
            val client = LocationServices.getSettingsClient(activity!!)
            val task =
                client.checkLocationSettings(builder.build())
            task.addOnSuccessListener(
                activity!!
            ) {
                startLocationUpdates()
            }
            task.addOnFailureListener(activity!!) { e: Exception ->
                when ((e as ApiException).statusCode) {
                    CommonStatusCodes.RESOLUTION_REQUIRED -> try {
                        val resolvable = e as ResolvableApiException
                        resolvable.startResolutionForResult(
                            activity,
                            1
                        )
                    } catch (ignored: SendIntentException) {
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE, CommonStatusCodes.CANCELED -> {
                    }
                }
            }
        }
    }

    fun startLocationUpdates() {
        if (checkPermission(
                activity!!,
                MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        ) {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
            mFusedLocationClient?.lastLocation
                ?.addOnSuccessListener(
                    activity!!
                ) { location: Location? ->
                    if (location != null && interfaceGetMyLocation != null) {
                        interfaceGetMyLocation!!.getLatLng(
                            location.latitude,
                            location.longitude
                        )
                    }
                }
            mFusedLocationClient?.requestLocationUpdates(
                mLocationRequest,
                mLocationCallback,
                null
            )
        }
    }

    fun stopUpdates() {
        mFusedLocationClient?.removeLocationUpdates(mLocationCallback)
    }
}