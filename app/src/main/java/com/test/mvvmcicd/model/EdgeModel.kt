package com.test.mvvmcicd.model

class EdgeModel {
    private var fromNode: NodeModel? = null
    private var toNode: NodeModel? = null
    private var signal_quality = false

    fun setFromNode(value: NodeModel){
        this.fromNode = value
    }

    fun getFromNode(): NodeModel?{
        return this.fromNode
    }

    fun setToNode(value: NodeModel){
        this.toNode = value
    }

    fun getToNode(): NodeModel?{
        return this.toNode
    }

    fun setSignQuality(value: Boolean){
        this.signal_quality = value
    }

    fun getSignQuality(): Boolean{
        return this.signal_quality
    }
}