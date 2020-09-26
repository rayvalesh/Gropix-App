package com.tc.utils.utils.utility


fun isNotNull(`object`: Any?): Boolean {
    return `object` != null
}


fun isNull(`object`: Any?): Boolean {
    return `object` == null
}

fun isNotNull(objects: Array<Any>?): Boolean {
    return objects != null
}

fun isNullAndEmpty(objects: Array<Any>): Boolean {
    return objects.isNullOrEmpty()
}

fun isNullAndEmpty(`object`: String?): Boolean {
    `object`?.let {
        return it.isEmpty() || it == "null"
    }
    return true
}
//fun isNullAndEmpty(vararg `object`: Any?): Boolean {
//    return if (`object` == null) {
//        true
//    } else {
//        for (obj in `object`) {
//
//        }
//    }
//}
//
//fun isNullAndEmpty(objectList: Any?): Boolean {
//    return if (objectList == null) {
//        true
//    } else {
//        val list = objectList as List<*>?
//        list!!.isEmpty()
//    }
//}

fun isNullAndEmpty(collection: Collection<*>?): Boolean {
    return collection?.isEmpty() ?: true
}

fun isNull(collection: Collection<*>?): Boolean {
    return collection == null
}