package com.interactive.ksi.propertyturkeybooking.interfaces

/**
 * Created by lenovo on 2/23/2016.
 */
interface HandleRetrofitResp {
    // id is selected id from dialog
    // name is selected name
    // flag witch dialog clciked
    fun onResponseSuccess(flag: String?, o: Any?)
    fun onNoContent(flag: String?, code: Int)
    fun onResponseSuccess(flag: String?, o: Any?, position: Int)
    fun onBadRequest(flag: String?, o: Any?)
}