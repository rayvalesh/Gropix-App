package com.tc.utils.utils.utility

import org.json.JSONObject

class Validate {

    fun dataCheck(jsonObject: JSONObject?, key: String): Boolean {
        return jsonObject != null && jsonObject.has(key) && !jsonObject.isNull(key)
    }

    companion object {

        private var validate: Validate? = null

        fun get(): Validate {
            if (validate == null) validate = Validate()
            return validate as Validate
        }
    }

}
