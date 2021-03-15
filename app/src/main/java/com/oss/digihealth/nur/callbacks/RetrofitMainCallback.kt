package com.oss.digihealth.nur.callbacks

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetrofitMainCallback<T>(callback: RetrofitCallback<T>) : Callback<T> {

    private val callback: RetrofitCallback<T>

    override fun onResponse(
        call: Call<T?>,
        response: Response<T?>
    ) {
        callback.onEverytime()
        val responseCode = response.code()
        if (responseCode == 200 || responseCode == 201) {
            if (response.body() != null) {
                callback.onSuccessfulResponse(response)
            }
            return
        }
        if (responseCode >= 500) {
            callback.onServerError(response)
            return
        }
        if (responseCode == 401) {
            callback.onUnAuthorized()
            return
        }
        if (responseCode == 403) {
            callback.onForbidden()
            return
        }
        if (responseCode >= 400) {
            callback.onBadRequest(response)
        }
    }

    override fun onFailure(call: Call<T?>, t: Throwable) {
        callback.onEverytime()
        t.printStackTrace()
        println("tttttttttttttttttttttttttttttttttttt.toString() = $t")
        callback.onFailure("Connectivity Lost")
    }

    init {
        this.callback = callback
    }
}