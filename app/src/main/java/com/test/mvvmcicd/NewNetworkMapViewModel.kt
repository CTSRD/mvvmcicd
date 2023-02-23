package com.test.mvvmcicd

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.test.mvvmcicd.model.*
import com.test.mvvmcicd.utils.screenRectPx
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class NewNetworkMapViewModel (private val repository: NodesRepository): ViewModel() {
    private val SCREEN_WIDTH = screenRectPx.width()
    private val SCREEN_HEIGHT = screenRectPx.height()
    private val ICON_WIDTH = 60 + 24
    private val ICON_HEIGHT = 60 + 24
    private val TRACK = 3
    private val RADIUS = SCREEN_WIDTH / 5 * TRACK
    private val ORIGIN_X = (SCREEN_WIDTH) /2 - ICON_WIDTH
    private val ORIGIN_Y = (SCREEN_HEIGHT) /2  - ICON_HEIGHT

    companion object {
        var data_backup: String? = null
    }

    val earth: MutableLiveData<NodeModel> by lazy {
        MutableLiveData<NodeModel>()
    }
    val cap: MutableLiveData<NodeModel> by lazy {
        MutableLiveData<NodeModel>()
    }
    val nodes: MutableLiveData<HashMap<String, NodeModel>> by lazy {
        MutableLiveData<HashMap<String, NodeModel>>()
    }
    private val nodeViews: MutableLiveData<HashMap<String, NodeModel>> by lazy {
        MutableLiveData<HashMap<String, NodeModel>>()
    }
    val edge_list: MutableLiveData<ArrayList<EdgeModel>> by lazy {
        MutableLiveData<ArrayList<EdgeModel>>(ArrayList())
    }

    var hasInternet = false

    var linkup = false

    var nodesList = NodesData()

    val responsed: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)

    var nodeRE_count = 0
    var nodeRE_index = 0
    private var saveNode = 0
    private var mTimer: Timer? = null

    init {
        //sync with IOS
//        loadSavedNetworkMap()
        this.nodeViews.observeForever {
            updateLine(it)
        }
    }

    private fun loadSavedNetworkMap(){
        val data = "{\"internet\":true,\"linkup\":true,\"nodes\":[{\"connected\":false,\"devices\":8,\"distance\":0,\"lanmacaddr\":\"00:06:19:FF:00:12\",\"location\":\"CAP\",\"role\":\"CAP\",\"uplink\":\"\",\"uplinkwired\":false},{\"connected\":false,\"devices\":14,\"distance\":3,\"lanmacaddr\":\"00:06:36:00:00:12\",\"location\":\"RE1\",\"role\":\"RE\",\"uplink\":\"00:06:19:FF:00:12\",\"uplinkwired\":false},{\"connected\":false,\"devices\":9,\"distance\":3,\"lanmacaddr\":\"00:06:19:FF:00:42\",\"location\":\"RE2\",\"role\":\"RE\",\"uplink\":\"00:06:19:FF:00:12\",\"uplinkwired\":false}]}"
        if(data.isNotEmpty()) {
            inputNodes(data)
//            setNodeDisconnect()
        }
    }

    fun getNodeNumbers(){
        val response = "{ \"status\": 200, \"message\": \"Success\", \"data\": { \"totalnodes\": 3 } }"
        var totalnodes = 0
        try {
            Log.i("getNodeNumbers", "$response")
            if(response != null) {
                val jsonData = JSONObject(response)
                totalnodes = jsonData.getJSONObject("data").getInt("totalnodes")
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
        saveNode = totalnodes
    }

    fun getSaveNodeNumbers(): Int{
        return saveNode
    }

    fun stopPolling(){
        mTimer?.cancel()
        mTimer = null
    }
    fun startPolling(){
        stopPolling()
        mTimer = Timer().apply {
            val timerTask: TimerTask = object : TimerTask() {
                override fun run() {
                    try {
                        if(mTimer == null){
                            cancel()
                            return
                        }else{
                            getNetworkMap()
                            getNodeList()
                            getNodeNumbers()
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                        return
                    }
                }
            }
            schedule(timerTask, 0L, 10 * 1000L)
        }
    }

    fun getNetworkMap(){
        val response = "{ \"status\": 200, \"message\": \"Success\", \"data\": {\"internet\":true,\"linkup\":true,\"nodes\":[{\"lanmacaddr\":\"00:06:19:FF:00:12\",\"distance\":0,\"location\":\"CAP\",\"role\":\"CAP\",\"connected\":true,\"uplink\":\"\",\"uplinkwired\":false,\"devices\":8},{\"lanmacaddr\":\"00:06:36:00:00:12\",\"distance\":3,\"location\":\"RE1\",\"role\":\"RE\",\"connected\":true,\"uplink\":\"00:06:19:FF:00:12\",\"uplinkwired\":false,\"devices\":11},{\"lanmacaddr\":\"00:06:19:FF:00:42\",\"distance\":3,\"location\":\"RE2\",\"role\":\"RE\",\"connected\":true,\"uplink\":\"00:06:19:FF:00:12\",\"uplinkwired\":false,\"devices\":10}]}}"
        MyApplication.runOnUiThread {
            responsed.value = true
            val data = JSONObject(response).getString("data")
            Log.i("getNetworkMap", "onSuccess$data")
            if (!data.isNullOrEmpty()) {
                inputNodes(data)
                data_backup = data
                saveDisconnectNetworkMap(data)
            }
        }
    }

    private fun saveDisconnectNetworkMap(data: String) {
        val dataModel = Gson().fromJson(data, Data::class.java)
        dataModel.nodes.forEach {
            it.connected = false
        }
        val dataString = Gson().toJson(dataModel)
        Log.i("saveDisconnectNetworkMap", "dataString:$dataString")
//        SharedPreferencesManager.getInstance().saveNetworkMap(dataString)
    }

    fun getNodeList(){
        val response = "{\"status\":200,\"message\":\"Success\",\"data\":{\"nodes\":[{\"lanmacaddr\":\"00:06:19:FF:00:12\",\"distance\":0,\"location\":\"CAP\",\"ipaddr\":\"192.168.0.1\",\"macaddr\":\"00:06:19:FF:00:11\",\"role\":\"CAP\",\"connected\":true},{\"lanmacaddr\":\"00:06:36:00:00:12\",\"distance\":3,\"location\":\"RE1\",\"ipaddr\":\"192.168.0.142\",\"macaddr\":\"00:06:36:00:00:11\",\"role\":\"RE\",\"connected\":true},{\"lanmacaddr\":\"00:06:19:FF:00:42\",\"distance\":3,\"location\":\"RE2\",\"ipaddr\":\"192.168.0.174\",\"macaddr\":\"00:06:19:FF:00:41\",\"role\":\"RE\",\"connected\":true}]}}"
        nodesList = NodesData.objectFromData(response)
    }

    fun findNodeBean(dataModel: NodeModel): String? {
        nodesList.data.nodes.forEach {
            if(it.lanmacaddr.uppercase() == dataModel.getNode().lanmacaddr){
                return it.macaddr
            }
        }
        return null
    }

    fun inputNodes(value: String){
        // Do an asynchronous operation to fetch nodes.
        Log.i("getNetworkMap", "input Node:$value")
        //{"internet":true,"linkup":true,"nodes":[{"lanmacaddr":"00:06:19:FF:00:12","distance":0,"location":"CAP","role":"CAP","connected":true,"uplink":"","uplinkwired":false,"devices":8},{"lanmacaddr":"00:06:36:00:00:12","distance":3,"location":"RE1","role":"RE","connected":true,"uplink":"00:06:19:FF:00:12","uplinkwired":false,"devices":11},{"lanmacaddr":"00:06:19:FF:00:42","distance":3,"location":"RE2","role":"RE","connected":true,"uplink":"00:06:19:FF:00:12","uplinkwired":false,"devices":10}]}
        val dataModel = Gson().fromJson(value, Data::class.java)
        hasInternet = dataModel.internet
        linkup = dataModel.linkup
        val nodeListData = dataModel.nodes //.sortedWith(compareBy({it.role}, {it.location}))
        val nodeModelList = ArrayList<NodeModel>()
        nodeListData.forEach {
            val nodeModel = NodeModel()
            if(it.role.isEmpty()){
                val node = it
                node.role = NodeType.CAP.name
                nodeModel.setNode(node)
            }else
                nodeModel.setNode(it)
            nodeModelList.add(nodeModel)
        }
        countHowManyNodeRe(nodeModelList.toList())

        nodeRE_index = 0
        this.nodes.value = HashMap()
        storedNode.clear()
        nodeModelList.forEach {
            configNodeForDisplay(it)
        }
        this.nodes.value = storedNode
    }

    private fun updateLine(nodeModelList: HashMap<String, NodeModel>) {
        this.edge_list.value = ArrayList()
        val list = nodeModelList.values.toList()
        list.forEach {
            configEdgeForDisplay(it)
        }
    }

    private fun setNodeDisconnect(){
        val cap = this.cap.value
        if (cap != null) {
            cap.getNode().connected = false
            this.cap.value = cap
        }
        val nodes = this.nodes.value
        if(nodes != null) {
            this.nodes.value?.forEach {
                it.value.getNode().connected = false
            }
            this.nodes.value = nodes
        }
    }

    private fun countHowManyNodeRe(nodeListData: List<NodeModel>) {
        nodeRE_count = nodeListData.count { it.getNode().role == "RE" }
        Log.i("Observe1", "nodeRE_count:$nodeRE_count")
    }

    private fun countNodeReIndex(role: String): Int {
        if(role == NodeType.RE.name) {
            nodeRE_index += 1
        }
        return nodeRE_index
    }

    private fun configNodeForDisplay(node: NodeModel){
        node.setId(countNodeReIndex(node.getNode().role))
        calculateXY(node)
        filterNode(node)
    }

    private fun configEdgeForDisplay(payload: NodeModel) {
        if(payload.getNode().uplink.isNullOrEmpty()){
            return
        }

        val fromNode = findNode(payload.getNode().uplink!!) ?: return
        val edge = EdgeModel()
        edge.setFromNode(fromNode)
        edge.setToNode(payload)
        val edges = this.edge_list.value
        if (edges != null) {
            edges.add(edge)
            edge_list.postValue(edges)
        }
    }

    fun findUplink(uplink: String?): String{
        if(uplink != null && uplink.isNotEmpty()) {
            val fromNode = findNode(uplink) as NodeModel
            return fromNode.getNode().location
        }else{
            return ""
        }
    }

    private fun findNode(key: String): NodeModel? {
        return (findNodeFromCap(key) ?: findNodeFromRE(key))
    }

    private fun findNodeFromCap(key: String): NodeModel? {
        val capNode = this.cap.value
        if (capNode != null) {
            if(capNode.getNode().lanmacaddr == key){
                return capNode
            }
        }
        return null
    }

    private fun findNodeFromRE(uplink: String): NodeModel? {
        val nodeMap = this.nodes.value
        if(nodeMap != null) {
            if(nodeMap[uplink] != null){
                return nodeMap[uplink]
            }
        }
        return null
    }

    private fun filterNode(value: NodeModel) {
        when(value.getNode().role){
            NodeType.CAP.name -> {
                saveCAP(value)
                generateEarth()
            }
            NodeType.RE.name -> {
                saveRE(value)
            }
        }
    }

    private fun generateEarth() {
        val node = if(earth.value != null){
            earth.value
        }else {
            NodeModel()
        }
        if (node != null) {
            node.getNode().role = NodeType.EARTH.name
            node.getNode().location = "Internet"
            calculateXY(node)
            saveEarth(node)
        }
    }

    private fun saveCAP(value: NodeModel) {
        this.cap.value = value
    }

    private fun saveEarth(value: NodeModel) {
        this.earth.value = value
    }

    val storedNode = HashMap<String, NodeModel>()
    private fun saveRE(value: NodeModel) {
//        val nodeMap = this.nodes.value ?: HashMap()
//        nodeMap[value.getNode().lanmacaddr] = value
//        this.nodes.value = nodeMap
        storedNode[value.getNode().lanmacaddr] = value
    }

    fun removeRE(macAddress: String){
        storedNode.remove(macAddress)
        updateRE(storedNode)
    }

    private fun updateRE(nodeMap: HashMap<String, NodeModel>) {
        nodeRE_index = 0
        nodeRE_count = nodeMap.size
        nodeMap.forEach {
            configNodeForDisplay(it.value)
        }
        this.nodes.value = storedNode
    }


    fun saveMWR(value: NodeModel) {
        val nodeMap = this.nodeViews.value ?: HashMap()
        nodeMap[value.getNode().lanmacaddr] = value
        this.nodeViews.value = nodeMap
    }

    fun removeMWR(macAddress: String) {
        val nodeMap = this.nodeViews.value ?: HashMap()
        nodeMap.remove(macAddress)
        this.nodeViews.value = nodeMap
    }

    private fun calculateXY(node: NodeModel) {
//        Log.i("calculateXY", "screenWidth:$SCREEN_WIDTH,screenHeight:$SCREEN_HEIGHT")

        when(node.getNode().role){
            NodeType.EARTH.name -> {
                val degrees = 270
                val radians = (degrees * Math.PI) / 180
                val x = ORIGIN_X + cos(radians) * RADIUS
                val y = ORIGIN_Y + sin(radians) * RADIUS
                node.setPositionX(x.toFloat())
                node.setPositionY(y.toFloat())
                node.setAngle(0F)
                node.setRadius(RADIUS)
                Log.i("calculateXY", "Earth angle:${node.getAngle()}, radius:${node.getRadius()}")
            }

            NodeType.CAP.name -> {
                node.setPositionX(ORIGIN_X.toFloat())
                node.setPositionY(ORIGIN_Y.toFloat())
            }
            NodeType.RE.name -> {
                val degrees = 180 - (180 / (nodeRE_count + 1) * node.getId())
                val radians = (degrees * Math.PI) / 180
                val x = ORIGIN_X + cos(radians) * RADIUS
                val y = ORIGIN_Y + sin(radians) * RADIUS
                node.setPositionX(x.toFloat())
                node.setPositionY(y.toFloat())
                node.setAngle(degrees.toFloat()+90)
                node.setRadius(RADIUS)
                Log.i("calculateXY", "Node angle:${node.getAngle()}, radius:${node.getRadius()}")
            }
        }
    }

    fun findRoleByMac(macAddress: String): String {
        var role = ""
        role = when {
            cap.value!!.getNode().lanmacaddr == macAddress -> {
                cap.value!!.getNode().role
            }
            nodes.value!!.containsKey(macAddress) -> {
                nodes.value!![macAddress]!!.getNode().role
            }
            else -> {
                "internet"
            }
        }
        return role
    }
}