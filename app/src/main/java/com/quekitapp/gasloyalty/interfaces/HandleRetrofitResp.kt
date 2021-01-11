package com.interactive.ksi.propertyturkeybooking.interfaces

/**
 * Created by lenovo on 2/23/2016.
 */
interface HandleRetrofitResp {
    fun onResponseSuccess(flag: String?, o: Any?)

    fun onResponseFailure(flag: String?, o: String?)

    fun onNoContent(flag: String?, code: Int)

    fun onBadRequest(flag: String?, o: Any?)
}