package com.test.mvvmcicd

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.doOnLayout
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.test.mvvmcicd.customDialog.NodeInfoBottomSheetDialog
import com.test.mvvmcicd.databinding.ActivityMainBinding
import com.test.mvvmcicd.model.EdgeModel
import com.test.mvvmcicd.model.Node
import com.test.mvvmcicd.model.NodeModel
import com.test.mvvmcicd.model.NodeType
import com.test.mvvmcicd.utils.DrawLinkView
import com.test.mvvmcicd.utils.NodeItemView
import com.test.mvvmcicd.utils.dp2px
import java.util.concurrent.ConcurrentHashMap

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val tag = this.javaClass.simpleName

    private lateinit var binding : ActivityMainBinding
    private val networkMapViewModel : NewNetworkMapViewModel by lazy {
        NewNetworkMapViewModel()
    }

    private lateinit var nodeInfoBottomSheetDialog: NodeInfoBottomSheetDialog
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private lateinit var vDim: View
    private lateinit var btnActionbarBack: ImageView
    private lateinit var btnActionbarAddNode: ImageView
    private lateinit var bottomSheetInfo: ConstraintLayout
    private lateinit var listLink: ListView
    private lateinit var tvTitle: TextView
    private lateinit var tvSubTitle: TextView
    private lateinit var tvDeviceIcon: ExtendedFloatingActionButton
    private lateinit var ivLocationIcon: ExtendedFloatingActionButton
    private lateinit var ivMoreIcon: ExtendedFloatingActionButton
    private lateinit var ivAddNodeIcon: ExtendedFloatingActionButton
    private lateinit var ivSettingsIcon: ImageView
    private lateinit var ivClose: ImageView
    private lateinit var tvTroubleShooting: TextView
    private lateinit var loading: ProgressBar

    private lateinit var clDevices: ConstraintLayout
    private lateinit var clLocation: ConstraintLayout
    private lateinit var clAddNode: ConstraintLayout
    private lateinit var clMoreInfo: ConstraintLayout

    private var iconWidth: Int = 0
    private var iconHeight: Int = 0
    private val numberWidth: Int = 24.dp2px
    private val numberHeight: Int = 24.dp2px
    private var viewMap = HashMap<String, View>()

    private val delayTime: Long = 1000
    private var isShowSnackbar = false
    private var isCapConnected = false
    private var isCheckAddedNode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()

    }

    override fun onPause() {
        super.onPause()
        networkMapViewModel.stopPolling()
    }

    override fun onResume() {
        super.onResume()
        networkMapViewModel.startPolling()
    }

    fun initViews() {

        loading = findViewById(R.id.pbProgressBar)
        vDim = findViewById(R.id.v_dim_layer)
        btnActionbarBack = findViewById(R.id.btnActionbarBack)
        btnActionbarAddNode = findViewById(R.id.btnActionbarAction)
        bottomSheetInfo = findViewById(R.id.bottom_sheet_info)
        tvTitle = bottomSheetInfo.findViewById<TextView>(R.id.tv_title)
        tvSubTitle = bottomSheetInfo.findViewById<TextView>(R.id.tv_subtitle)
        listLink = bottomSheetInfo.findViewById<ListView>(R.id.table_view)!!
        tvDeviceIcon = bottomSheetInfo.findViewById<ExtendedFloatingActionButton>(R.id.tvDeviceIcon)
        ivLocationIcon = bottomSheetInfo.findViewById<ExtendedFloatingActionButton>(R.id.ivLocationIcon)
        ivMoreIcon = bottomSheetInfo.findViewById<ExtendedFloatingActionButton>(R.id.ivMoreIcon)
        ivAddNodeIcon = bottomSheetInfo.findViewById(R.id.ivAddNodeIcon)
        ivClose = bottomSheetInfo.findViewById(R.id.ivClose)
        tvTroubleShooting = bottomSheetInfo.findViewById(R.id.tvTroubleShooting)

        clDevices = bottomSheetInfo.findViewById<ConstraintLayout>(R.id.cl_device_info)
        clLocation = bottomSheetInfo.findViewById<ConstraintLayout>(R.id.cl_location)
        clAddNode = bottomSheetInfo.findViewById<ConstraintLayout>(R.id.cl_add_nodes)
        clMoreInfo = bottomSheetInfo.findViewById<ConstraintLayout>(R.id.cl_more_info)

        btnActionbarAddNode.setOnClickListener(this)
        clDevices.setOnClickListener(this)
        clLocation.setOnClickListener(this)
        clAddNode.setOnClickListener(this)
        clMoreInfo.setOnClickListener(this)
        ivClose.setOnClickListener(this)
        tvTroubleShooting.setOnClickListener(this)
        btnActionbarBack.setOnClickListener(this)
        vDim.setOnClickListener(this)

        btnActionbarAddNode.isEnabled = false
        btnActionbarAddNode.visibility = View.VISIBLE

        networkMapViewModel.cap.observe(this, Observer {
            if (it != null) {
                Log.i("NetworkMapActivity", "Observe cap -> ${it.getNode()}")
                addCAP(it)
                loading.visibility = View.GONE
                binding.emptyNetworkMap.visibility = View.GONE
            }
        })
        networkMapViewModel.earth.observe(this, Observer {
            if (it != null) {
                Log.i("Observe1", "get EARTH data")
                addEarth(it)
                loading.visibility = View.GONE
                binding.emptyNetworkMap.visibility = View.GONE
            }
        })
        networkMapViewModel.nodes.observe(this, Observer {
            if (it != null) {
                Log.i("getNetworkMap", "Observe node -> ${it.values}")
                drawNetworkMap(it)
                Log.i("Observe1", "node count:" + it.size)
                loading.visibility = View.GONE
                binding.emptyNetworkMap.visibility = View.GONE
                btnActionbarAddNode.isEnabled = true //it.values.size < 2
            }
        })
        networkMapViewModel.edge_list.observe(this, Observer {
            if (it != null) {
                Log.i("getNetworkMap", "Observe edge_list")
                Log.i("Observe1", "edge count:" + it.size)
                drawMapLine(it)
            }
        })

        networkMapViewModel.responsed.observe(this) {
            Handler(Looper.getMainLooper()).postDelayed({
                loading.visibility = View.GONE
                if (viewMap.isEmpty()) {
                    binding.emptyNetworkMap.visibility = View.VISIBLE
                } else {
                    binding.emptyNetworkMap.visibility = View.GONE
                }
            }, 5000)
        }

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetInfo)
        bottomSheetBehavior.isFitToContents = true
//        bottomSheetBehavior.isHideable = true
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_DRAGGING -> vDim.visibility = View.VISIBLE
                    BottomSheetBehavior.STATE_HIDDEN -> vDim.visibility = View.GONE
                    BottomSheetBehavior.STATE_EXPANDED -> vDim.visibility = View.VISIBLE
                    BottomSheetBehavior.STATE_COLLAPSED -> vDim.visibility = View.VISIBLE
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> vDim.visibility = View.VISIBLE
                    BottomSheetBehavior.STATE_SETTLING -> vDim.visibility = View.VISIBLE
                }
            }
        })

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.v_dim_layer -> {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
            R.id.btnActionbarAction -> {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                btnActionbarAddNode.isEnabled = false
            }
            R.id.ivClose -> {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
            R.id.btnActionbarBack -> {
                finish()
            }
        }
    }

    private fun drawLinkCapEarth() {
        val nodeEarth = networkMapViewModel.earth.value
        val nodeCap = networkMapViewModel.cap.value
        if(nodeEarth == null) return
        if(nodeCap == null) return
        //for new API, change connect via internet
        nodeEarth.getNode().connected = networkMapViewModel.hasInternet
        addLine(nodeEarth, nodeCap)
    }

    private fun drawMapLine(edgeArrayList: ArrayList<EdgeModel>) {
        val layout = findViewById<View>(R.id.line_network_map) as ConstraintLayout
        layout.removeAllViews()

        drawLinkCapEarth()

        edgeArrayList.forEach {
            val fromNode = it.getFromNode()
            val toNode = it.getToNode()
            if (fromNode != null && toNode != null) {
                addLine(fromNode, toNode)
            }
        }
    }

    private fun addLine(nodeFrom: NodeModel, nodeTo: NodeModel) {
        getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val ll = findViewById<View>(R.id.line_network_map) as ConstraintLayout
        val drawLine = DrawLinkView(this)
        drawLine.tag = "Line"
        iconWidth = 72.dp2px
        iconHeight = 72.dp2px
        if(nodeFrom.getNode().role == NodeType.EARTH.name){
        }else if(!nodeTo.getNode().uplinkwired){
            drawLine.setDottedLine()
        }
        val fromX = countFromX(nodeFrom, nodeTo)
        val fromY = countFromY(nodeFrom, nodeTo)
        val toX = countToX(nodeFrom, nodeTo)
        val toY = countToY(nodeFrom, nodeTo)
        if(nodeFrom.getNode().role == NodeType.EARTH.name){
            if(!networkMapViewModel.linkup){
                drawLine.setColor(R.color.warning)
            }
            drawLine.addSourcePoint(
                fromX,
                fromY
            )
            drawLine.addDestinationPoint(
                toX,
                toY
            )
            ll.addView(drawLine, -1)
        }else
            if(nodeTo.getNode().connected) {
                when (nodeTo.getNode().distance) {
                    0 -> drawLine.setSingalQuality(true)
                    1 -> {
                        drawLine.setSingalQuality(false)
                        setStrongSignalMessage(nodeFrom, nodeTo)
                    }
                    2 -> {
                        drawLine.setSingalQuality(false)
                        setWeakSignalMessage(nodeFrom, nodeTo)
                    }
                    3 -> drawLine.setSingalQuality(true)
                    else -> {
                        drawLine.setSingalQuality(false)
                    }
                }
                drawLine.addSourcePoint(
                    fromX,
                    fromY
                )
                drawLine.addDestinationPoint(
                    toX,
                    toY
                )
                ll.addView(drawLine, -1)
            }else if(nodeTo.getNode().role == NodeType.CAP.name){
                drawLine.setColor(R.color.warning)
                drawLine.addSourcePoint(
                    fromX,
                    fromY
                )
                drawLine.addDestinationPoint(
                    toX,
                    toY
                )
                ll.addView(drawLine, -1)
            }
    }

    private fun countFromX(nodeFrom: NodeModel, nodeTo: NodeModel): Float {
        val fromView = viewMap[nodeFrom.getNode().lanmacaddr]
        val fromViewX:Float = (fromView?.x ?: 0F)
        val fromViewWidth:Int = (fromView?.width ?: 0)
        val fromX = when(nodeFrom.getNode().role){
            NodeType.EARTH.name -> {
                fromViewX + fromViewWidth / 2
            }
            NodeType.CAP.name -> {
                when {
                    viewMap.size < 4 -> {
                        fromViewX + fromViewWidth / 2
                    }
                    fromViewX > nodeTo.getPositionX() -> {
                        fromViewX + fromViewWidth / 3
                    }
                    else -> {
                        fromViewX + fromViewWidth * 2/3
                    }
                }
            }
            NodeType.RE.name -> {
                if(fromViewX > nodeTo.getPositionX()){
                    fromViewX
                }else {
                    fromViewX + fromViewWidth
                }
            }
            else -> fromViewX + fromViewWidth / 2
        }
        return fromX
    }

    private fun countFromY(nodeFrom: NodeModel, nodeTo: NodeModel): Float {
        val fromView = viewMap[nodeFrom.getNode().lanmacaddr]
        val fromViewY:Float = (fromView?.y ?: nodeTo.getPositionY())
        val fromViewHeight:Int = (fromView?.height ?: 0)
        Log.i(tag, "size countFromY:("+ fromViewY +", "+ (fromView?.height ?: 0) +")")

        val fromY = when(nodeFrom.getNode().role){
            NodeType.EARTH.name -> {
                fromViewY + fromViewHeight / 2
            }
            NodeType.CAP.name -> {
                if(fromViewY > nodeTo.getPositionY()) {
                    fromViewY
                }else{
                    fromViewY + fromViewHeight
                }
            }
            NodeType.RE.name -> {
                fromViewY + fromViewHeight / 2
            }
            else -> fromViewY + fromViewHeight / 2
        }
        return fromY
    }

    private fun countToX(nodeFrom: NodeModel, nodeTo: NodeModel): Float {
        val toView = viewMap[nodeTo.getNode().lanmacaddr]
        val toViewX:Float = (toView?.x ?: 0F)
        val toViewWidth:Int = (toView?.width ?: 0)
        val toX = when(nodeFrom.getNode().role){
            NodeType.EARTH.name -> {
                toViewX + toViewWidth / 2
            }
            NodeType.CAP.name -> {
                toViewX + toViewWidth / 2
            }
            NodeType.RE.name -> {
                if(nodeFrom.getPositionX() > toViewX){
                    toViewX + toViewWidth
                }else {
                    toViewX
                }
            }
            else -> toViewX + toViewWidth / 2
        }
        return toX
    }

    private fun countToY(nodeFrom: NodeModel, nodeTo: NodeModel): Float {
        val toView = viewMap[nodeTo.getNode().lanmacaddr]
        val toViewY:Float = (toView?.y ?: nodeTo.getPositionY())
        val toViewHeight:Int = (toView?.height ?: 0)
        Log.i(tag, "size countToY:("+ toViewY +", "+ (toView?.height ?: 0) +")")
        val toY = when(nodeFrom.getNode().role){
            NodeType.EARTH.name -> {
                toViewY + numberHeight
            }
            NodeType.CAP.name -> {
                toViewY + numberHeight
            }
            NodeType.RE.name -> {
                toViewY + toViewHeight / 2
            }
            else -> toViewY + toViewHeight / 2
        }
        return toY
    }

    private fun setStrongSignalMessage(nodeFrom: NodeModel, nodeTo: NodeModel) {
        if(nodeFrom.getNode().role == NodeType.EARTH.name) return
        val fromNode = networkMapViewModel.findUplink(nodeTo.getNode().uplink)
        Log.i(tag, "setStrongSignalMessage:$fromNode")
    }

    private fun setWeakSignalMessage(nodeFrom: NodeModel, nodeTo: NodeModel) {
        if(nodeFrom.getNode().role == NodeType.EARTH.name) return
        val fromNode = networkMapViewModel.findUplink(nodeTo.getNode().uplink)
        Log.i(tag, "setWeakSignalMessage:$fromNode")
    }

    private fun getNodeIcon(node: NodeModel): Int {
        return when(node.getNode().role){
            NodeType.EARTH.name -> R.layout.content_earth_format
            NodeType.CAP.name -> R.layout.content_cap_format
            NodeType.RE.name -> R.layout.content_node_format
            else -> R.layout.content_node_format
        }
    }

    private fun addNode(node: NodeModel) {
        getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val ll = findViewById<View>(R.id.node_network_map) as ConstraintLayout
        val item: NodeItemView = if(viewMap.containsKey(node.getNode().lanmacaddr)) {
            viewMap[node.getNode().lanmacaddr] as NodeItemView
        }else {
            NodeItemView(this)
        }

        item.setCapConnectState(isCapConnected)
        item.setNode(node)
        item.tag = node.getNode()
        if(!viewMap.containsKey(node.getNode().lanmacaddr)) {
            item.id = View.generateViewId()
            ll.addView(item, 0)

            val constraintSet = ConstraintSet()
            constraintSet.clone(ll)

            constraintSet.constrainCircle(item.id, capItem.id, node.getRadius(), node.getAngle())

            constraintSet.applyTo(ll)
        }


        item.setOnClickListener {
            if(!networkMapViewModel.cap.value?.getNode()?.connected!!){
                return@setOnClickListener
            }
            setNodeInfoBottomSheetDialog(node)
            Log.i("Nodes", "Click Node:" + node.getNode().role)
        }

        viewMap[node.getNode().lanmacaddr] = item
        ll.doOnLayout {
            networkMapViewModel.saveMWR(node)
        }
    }

    private lateinit var capItem: NodeItemView
    private lateinit var capNode: NodeModel
    private fun addCAP(node: NodeModel) {
        getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val ll = findViewById<View>(R.id.node_network_map) as ConstraintLayout
        capItem = if(viewMap.containsKey(node.getNode().lanmacaddr)) {
            viewMap[node.getNode().lanmacaddr] as NodeItemView
        }else {
            NodeItemView(this)
        }
        capNode = node
        capItem.setNode(node)
        isCapConnected = node.getNode().connected
        capItem.tag = node.getNode()


        capItem.setOnClickListener {
            setNodeInfoBottomSheetDialog(node)
            Log.i("Nodes", "Click CAP " + node.getNode().role)
        }

        if(!viewMap.containsKey(node.getNode().lanmacaddr)) {
            capItem.id = View.generateViewId()
            ll.addView(capItem, 0)

            val constraintSet = ConstraintSet()
            constraintSet.clone(ll)

            constraintSet.connect(capItem.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
            constraintSet.connect(capItem.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
            constraintSet.connect(capItem.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
            constraintSet.connect(capItem.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)

            constraintSet.applyTo(ll)
        }

        ll.doOnLayout {
            viewMap[node.getNode().lanmacaddr] = capItem
            networkMapViewModel.saveMWR(node)
        }
    }

    private fun setNodeInfoBottomSheetDialog(node: NodeModel) {
        nodeInfoBottomSheetDialog = NodeInfoBottomSheetDialog(this, R.style.Theme_NoWiredStrapInNavigationBar, node, networkMapViewModel.findUplink(node.getNode().uplink))
        nodeInfoBottomSheetDialog.setListener(object : NodeInfoBottomSheetDialog.Listener {
            override fun onCancel() {
            }

            override fun onSelect(action: Int, node: NodeModel) {
                when(action){
                    R.id.cl_add_nodes -> {
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    }
                    R.id.cl_device_info -> {
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                        //device function
                    }
                    R.id.cl_location -> {
                        //location function
                    }
                    R.id.cl_more_info -> {
                        //info function
                    }
                    R.id.tvTroubleShooting -> {
                        //help function
                    }
                }
                Log.i("NodeInfoBottomSheetDialog", "Click Fab:" + getString(action))
            }
        })
        nodeInfoBottomSheetDialog.show()
    }

    private fun addEarth(node: NodeModel) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val ll = findViewById<View>(R.id.node_network_map) as ConstraintLayout

        val nodeIcon = getNodeIcon(node)
        val item: View = if(viewMap.containsKey(node.getNode().lanmacaddr)) {
            viewMap[node.getNode().lanmacaddr]!!
        }else {
            inflater.inflate(nodeIcon, null)
        }

        val ivEarthNumber = item.findViewById<TextView>(R.id.iv_earth_numbers)
        if(networkMapViewModel.hasInternet){
            ivEarthNumber.visibility = View.GONE
        }else{
            ivEarthNumber.visibility = View.VISIBLE
        }
        val tvEarthTitle = item.findViewById<TextView>(R.id.tv_earth_subtitle)
        tvEarthTitle.visibility = View.VISIBLE
        tvEarthTitle.text = node.getNode().location
        item.tag = node.getNode()
        if(!viewMap.containsKey(node.getNode().lanmacaddr)) {
            item.id = View.generateViewId()
            ll.addView(item, 0)

            val constraintSet = ConstraintSet()
            constraintSet.clone(ll)
            constraintSet.constrainCircle(item.id, capItem.id, node.getRadius(), node.getAngle())

            constraintSet.applyTo(ll)
        }

        ll.doOnLayout {
            viewMap[node.getNode().lanmacaddr] = item
            networkMapViewModel.saveMWR(node)
        }

    }

    private fun drawNetworkMap(nodeMap: HashMap<String, NodeModel>) {
        clearReFromMap()

        nodeMap.forEach {
            addNode(it.value)
        }
    }

    private fun clearReFromMap() {
        val ll = findViewById<View>(R.id.node_network_map) as ConstraintLayout
        val map: ConcurrentHashMap<String, View> = ConcurrentHashMap<String, View>()
        map.putAll(viewMap)
        for (entry in map.entries) {
            val mNode: Node = entry.value.tag as Node
            if(mNode.role == NodeType.RE.name){
                ll.removeView(map[mNode.lanmacaddr])
                map.remove(mNode.lanmacaddr)
                networkMapViewModel.removeMWR(mNode.lanmacaddr)
            }
        }
        viewMap.clear()
        viewMap.putAll(map)
    }
}