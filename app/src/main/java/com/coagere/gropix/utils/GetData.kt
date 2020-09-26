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

}