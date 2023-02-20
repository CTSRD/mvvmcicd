package com.test.mvvmcicd.utils

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getColor
import com.test.mvvmcicd.model.NodeModel
import com.test.mvvmcicd.R


class NodeItemView(context: Context) : ConstraintLayout(context) {

    private val STATUS_CAP_OFFLINE = 1001
    private val STATUS_CAP_ONLINE = 1002
    private val STATUS_RE_OFFLINE = 2001
    private val STATUS_RE_ONLINE = 2002
    private val STATUS_RE_BAD_SIGNAL = 2003

    private lateinit var ivNode: ImageView
    private lateinit var tvDeviceNumber: TextView
    private lateinit var tvNodeTitle: TextView
    private lateinit var tvNodeBody: TextView

    private var node: NodeModel? = null

    private var isCap = false
    private var isCapConnected = false
    private var connected = false
    private var signal_quality = false

    init {
        loadViews()
    }

    private fun loadViews() {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val ll = layoutInflater.inflate(R.layout.content_node_format, this, true)
        ivNode = ll.findViewById(R.id.iv_node_icon)
        tvDeviceNumber = ll.findViewById(R.id.iv_node_numbers)
        tvNodeTitle = ll.findViewById(R.id.tv_node_subtitle)
        tvNodeBody = ll.findViewById(R.id.tv_node_body)
        tvNodeTitle.maxWidth = screenRectPx.width() /3
        tvNodeBody.maxWidth = screenRectPx.width() /3
    }

    fun setNode(payload: NodeModel) {
//        if (this.node != payload) {
//            Log.i("NodeItemView", "Node change -> "+ Gson().toJson(payload))
//            this.node = payload

//        ivNode?.setNode(payload)

            setRole(payload.getNode().role)
            setConnectionState(payload.getNode().connected)
            setDistance(payload.getNode().distance)
            val status = verifyStatus()
        Log.i("NodeItemView", "setNode:"+payload.getNode().role+" status:"+status)
            setNodeIcon(status)
            setDeviceNumber(status, payload.getNode().devices.toString())
            if(payload.getNode().location.isNotEmpty()) {
                setNodeTitle(payload.getNode().location)
            }
//            setNodeBadge(status)

            // Refresh the drawable state so that it includes the message unread state if required.
            refreshDrawableState()
//        }else{
//            Log.i("NodeItemView", "Node not change -> "+ Gson().toJson(payload))
//        }
    }

    private fun setRole(role: String) {
        isCap = role == "CAP"
    }

    fun setCapConnectState(connected: Boolean){
        isCapConnected = connected
    }

    private fun setConnectionState(connected: Boolean) {
        // Performance optimisation: only update the state if it has changed.
        if (this.connected != connected) {
            this.connected = connected
        }
    }

    private fun setDistance(distance: Int) {
        signal_quality = distance == 3
    }

    private fun verifyStatus(): Int{
        return if(isCap){
            if(connected)
                STATUS_CAP_ONLINE
            else
                STATUS_CAP_OFFLINE
        }else {
            if (connected) {
                if (signal_quality)
                    STATUS_RE_ONLINE
                else
                    STATUS_RE_BAD_SIGNAL
            } else
                STATUS_RE_OFFLINE
        }
    }

    private fun setNodeBadge(status: Int) {
        val backgroundResource: Int = if(isCap){
            if(connected)
                R.drawable.shape_number_circle
            else
                R.drawable.ic_x
        }else {
            if (connected) {
                if (signal_quality)
                    R.drawable.shape_number_circle
                else
                    R.drawable.shape_number_circle
            } else
                R.drawable.shape_help_circle
        }
        tvDeviceNumber.setBackgroundResource(backgroundResource)
    }

    private fun setNodeIcon(status: Int) {
        when(status){
            STATUS_CAP_OFFLINE -> {
//                ivNode.setImageResource(R.drawable.map_node)
                tvDeviceNumber.setBackgroundResource(R.drawable.shape_help_circle)
            }
            STATUS_CAP_ONLINE -> {
//                ivNode.setImageResource(R.drawable.map_node)
                tvDeviceNumber.setBackgroundResource(R.drawable.shape_number_circle)
            }
            STATUS_RE_OFFLINE -> {
//                ivNode.setImageResource(R.drawable.map_node)
                if(isCapConnected) {
                    tvDeviceNumber.visibility = View.VISIBLE
                    tvDeviceNumber.setBackgroundResource(R.drawable.shape_help_circle)
                }else{
                    tvDeviceNumber.visibility = View.GONE
                }
            }
            STATUS_RE_ONLINE -> {
//                ivNode.setImageResource(R.drawable.map_node)
                if(isCapConnected) {
                    tvDeviceNumber.visibility = View.VISIBLE
                    tvDeviceNumber.setBackgroundResource(R.drawable.shape_number_circle)
                }else{
                    tvDeviceNumber.visibility = View.GONE
                }
            }
            STATUS_RE_BAD_SIGNAL -> {
//                ivNode.setImageResource(R.drawable.map_node)
                if(isCapConnected) {
                    tvDeviceNumber.visibility = View.VISIBLE
                    tvDeviceNumber.setBackgroundResource(R.drawable.shape_number_circle)
                }else{
                    tvDeviceNumber.visibility = View.GONE
                }
            }
        }
    }

    private fun setDeviceNumber(status: Int, payload: String?) {
        when(status){
            STATUS_CAP_OFFLINE -> {
                tvDeviceNumber.text = "?"
            }
            STATUS_CAP_ONLINE -> {
                tvDeviceNumber.text = payload
            }
            STATUS_RE_OFFLINE -> {
                tvDeviceNumber.text = "?"
            }
            STATUS_RE_ONLINE -> {
                var num = 0
                try {
                    num = payload!!.toInt()
                } catch (e: Exception){
                    e.printStackTrace()
                }
                if(num > 100){
                    tvDeviceNumber.text = "N"
                }else {
                    tvDeviceNumber.text = payload
                }
            }
            STATUS_RE_BAD_SIGNAL -> {
                var num = 0
                try {
                    num = payload!!.toInt()
                } catch (e: Exception){
                    e.printStackTrace()
                }
                if(num > 100){
                    tvDeviceNumber.text = "N"
                }else {
                    tvDeviceNumber.text = payload
                }
            }
        }
    }

    private fun setNodeTitle(location: String) {
        tvNodeTitle.visibility = View.VISIBLE
        tvNodeTitle.text = location
    }

}