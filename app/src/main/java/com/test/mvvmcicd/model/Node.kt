package com.test.mvvmcicd.model

data class Node(
        var lanmacaddr: String = "earth",
        var distance: Int = 0,
        var location: String = "",
        var role: String = "",
        var connected: Boolean = false,
        var uplink: String? = null,
        var uplinkwired: Boolean = false,
        var devices: Int = 0
)