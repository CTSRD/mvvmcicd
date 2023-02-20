package com.test.mvvmcicd.model

import com.test.mvvmcicd.model.Node

data class Data(
        var internet: Boolean,
        var linkup: Boolean,
        var nodes: List<Node>
)

