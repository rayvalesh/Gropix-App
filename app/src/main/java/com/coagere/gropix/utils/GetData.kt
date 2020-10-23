package com.coagere.gropix.utils

import android.view.View
import com.coagere.gropix.jetpack.entities.CityModel
import com.google.gson.reflect.TypeToken
import tk.jamun.volley.classes.VolleyGSON
import java.io.InputStream

object GetData {
    fun getCities(): Array<CityModel> {
        return VolleyGSON.get().fromJson(
            VolleyGSON.get().getTypeToken(object : TypeToken<Array<CityModel>>() {}),
            assetJSONFile()
        ) as Array<CityModel>
    }

    fun getCitiesName(): Array<String> {
        val cities: ArrayList<String> = arrayListOf()
        for (model in getCities()) {
            cities.add(model.name)
        }
        return cities.toTypedArray()
    }

    fun getStateName(): Array<String> {
        val states: ArrayList<String> = arrayListOf(
            "Andaman and Nicobar Islands",
            "Andhra Pradesh",
            "Arunachal Pradesh",
            "Assam",
            "Bihar",
            "Chandigarh",
            "Chhattisgarh",
            "Dadra and Nagar Haveli",
            "Daman and Diu",
            "Delhi",
            "Goa",
            "Gujarat",
            "Haryana",
            "Himachal Pradesh",
            "Jammu and Kashmir",
            "Jharkhand",
            "Karnataka",
            "Kerala",
            "Ladakh",
            "Lakshadweep",
            "Madhya Pradesh",
            "Maharashtra",
            "Manipur",
            "Meghalaya",
            "Mizoram",
            "Nagaland",
            "Odisha",
            "Puducherry",
            "Punjab",
            "Rajasthan",
            "Sikkim",
            "Tamil Nadu",
            "Telangana",
            "Tripura",
            "Uttar Pradesh",
            "Uttarakhand",
            "West Bengal"
        )
        return states.toTypedArray()
    }


    fun assetJSONFile(): String? {
        val file: InputStream = MyApplication.appContext.assets.open("cities.json")
        val formArray = ByteArray(file.available())
        file.read(formArray)
        file.close()
        return String(formArray)
    }

    fun setVisibility(isVisible: Boolean, view: View) {
        view.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun getPinCode(): Array<String> {
        return arrayOf(
            "534201",
            "534202",
            "534204",
            "522202",
            "522201",
            "533101",
            "533104",
            "533103",
            "533105",
            "533106",
            "522002",
            "522003",
            "522004",
            "520001",
            "520002",
            "515001",
        )
    }

}