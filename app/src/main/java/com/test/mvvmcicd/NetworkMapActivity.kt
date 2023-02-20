package com.test.mvvmcicd

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.test.mvvmcicd.databinding.ActivityNetworkMapBinding

class NetworkMapActivity : AppCompatActivity() {
    private val tag = this.javaClass.simpleName

    private lateinit var binding : ActivityNetworkMapBinding
    private val networkMapViewModel : NewNetworkMapViewModel by lazy {
        NewNetworkMapViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network_map)
    }
}