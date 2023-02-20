package com.test.mvvmcicd.model

import com.google.gson.annotations.SerializedName

data class MisconnectModel(
        @SerializedName("misconnected")
        val misconnected: Boolean
)