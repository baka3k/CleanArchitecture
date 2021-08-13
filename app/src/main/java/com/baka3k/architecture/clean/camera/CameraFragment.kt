/*
 * Created by Le Quang Hiep on 4/21/20 1:59 PM.
 * Copyright © 2020 All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details
 */

package com.baka3k.architecture.clean.camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.hardware.display.DisplayManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.baka3k.architecture.clean.R
import com.baka3k.architecture.clean.base.ui.BaseFragment
import com.baka3k.architecture.device.barcode.decoder.BarcodeAnalyzer
import com.baka3k.architecture.device.barcode.decoder.utils.Display
import com.baka3k.architecture.device.barcode.decoder.zxing.ZxingDecoder
import com.baka3k.architecture.device.camera.BakaCamera
import com.baka3k.architecture.device.camera.CameraPreview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class CameraFragment : BaseFragment() {
    companion object {
        private val PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.CAMERA)
        private const val PERMISSIONS_REQUEST_CODE = 10
    }


    private lateinit var viewFinder: CameraPreview
    private lateinit var tvBarcodeContent: TextView
    private var displayId: Int = -1
    private var bakaCamera: BakaCamera? = null
    private val displayManager by lazy {
        requireContext().getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
    }
    private lateinit var cameraExecutor: ExecutorService
    private val displayListener = object : DisplayManager.DisplayListener {
        override fun onDisplayAdded(displayId: Int) = Unit
        override fun onDisplayRemoved(displayId: Int) = Unit
        override fun onDisplayChanged(displayId: Int) = view?.let { view ->
            if (displayId == this@CameraFragment.displayId) {
                bakaCamera?.updateRotation(view.display.rotation)
            }
        } ?: Unit
    }

    override fun onCreated() {
        super.onCreated()
        initViewModel()
    }

    private fun initViewModel() {
//        viewModel = ViewModelProvider(this, ViewModelFactory(networkConnection)).get(
//            MoviesViewModel::class.java
//        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvBarcodeContent = view.findViewById(R.id.tvBarcodeContent)
        viewFinder = view.findViewById(R.id.view_finder)
        cameraExecutor = Executors.newSingleThreadExecutor()
        displayManager.registerDisplayListener(displayListener, null)
        viewFinder.post {
            displayId = viewFinder.display.displayId
        }
        val display = Display(requireContext())
        val barcodeDecoder = ZxingDecoder(display)
        val barcodeAnalyzer = BarcodeAnalyzer(barcodeDecoder) {
            tvBarcodeContent.text = it
        }
        bakaCamera = BakaCamera.Builder()
            .barcodeAnalyzer(barcodeAnalyzer)
            .lifeCycleOwner(lifecycleOwner = this)
            .onExecutor(cameraExecutor)
            .bindToViewFinder(viewFinder)
            .build()
    }

    override fun onDestroyView() {
        cameraExecutor.shutdown()
        displayManager.unregisterDisplayListener(displayListener)
        super.onDestroyView()
    }

    private fun updateCameraUi() {
        bakaCamera?.startPreview()
    }


    private fun startCameraPreview() {
        bakaCamera?.startPreview()
    }

    private fun stopCameraPreview() {
        lifecycleScope.launch(Dispatchers.Main) {
            bakaCamera?.stopPreview()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        updateCameraUi()
    }

    private fun hasPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PERMISSIONS_REQUIRED.all {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    it
                ) == PackageManager.PERMISSION_GRANTED
            }
        } else {
            true
        }
    }

    override fun onStart() {
        super.onStart()
        startCamera()
    }

    override fun onStop() {
        stopCameraPreview()
        super.onStop()
    }

    private fun startCamera() {
        if (!hasPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                    PERMISSIONS_REQUIRED,
                    PERMISSIONS_REQUEST_CODE
                )
            }
        } else {
            startCameraPreview()
        }
    }
}