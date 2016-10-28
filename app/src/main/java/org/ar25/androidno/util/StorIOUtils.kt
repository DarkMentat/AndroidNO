package org.ar25.androidno.util

import com.pushtorefresh.storio.sqlite.operations.put.PreparedPut
import com.pushtorefresh.storio.sqlite.operations.put.PreparedPutObject

fun <T> PreparedPut.Builder.someObject(someObject: T): PreparedPutObject.Builder<T>{
    return this.`object`(someObject)
}
