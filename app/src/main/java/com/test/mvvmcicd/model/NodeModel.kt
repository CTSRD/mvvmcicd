package com.test.mvvmcicd.model

import com.google.gson.annotations.SerializedName

class NodeModel {
    @SerializedName("node")
    private var node: Node = Node()
    @SerializedName("id")
    private var id: Int = 0
    @SerializedName("positionX")
    private var positionX: Float = 0f
    @SerializedName("positionY")
    private var positionY: Float = 0f
    @SerializedName("angle")
    private var angle: Float = 0f
    @SerializedName("radius")
    private var radius: Int = 0

    fun setId(value: Int){
        this.id = value
    }

    fun getId(): Int{
        return this.id
    }

    fun setNode(value: Node){
        this.node = value
    }

    fun getNode(): Node{
        return this.node
    }

    fun setPositionX(position: Float){
        this.positionX = position
    }

    fun getPositionX(): Float{
        return this.positionX
    }

    fun setPositionY(position: Float){
        this.positionY = position
    }

    fun getPositionY(): Float{
        return this.positionY
    }

    fun setAngle(angle: Float){
        this.angle = angle
    }

    fun getAngle(): Float{
        return this.angle
    }

    fun setRadius(radius: Int){
        this.radius = radius
    }

    fun getRadius(): Int{
        return this.radius
    }
}