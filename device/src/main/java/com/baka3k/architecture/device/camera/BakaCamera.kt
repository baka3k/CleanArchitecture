/*
 * Created by Le Quang Hiep on 4/22/20 11:33 AM.
 * Copyright © 2020 Baka3k@gmail.com. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details
 */
package com.baka3k.architecture.device.camera

import android.util.DisplayMetrics
import android.util.Log
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.baka3k.architecture.device.barcode.decoder.BarcodeAnalyzer
import java.util.concurrent.ExecutorService
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class BakaCamera private constructor(
    private val lifecycleOwner: LifecycleOwner,
    private val viewFinder: PreviewView,
    private val cameraExecutor: ExecutorService?,
    private val barcodeAnalyzer: BarcodeAnalyzer?,
) {
    private constructor(builder: Builder) : this(
        builder.lifecycleOwner!!,
        builder.viewFinder!!,
        builder.cameraExecutor,
        builder.barcodeAnalyzer
    )

    companion object {
        private const val TAG = "BakaCamera"
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0
    }

    private var lensFacing: Int = CameraSelector.LENS_FACING_BACK
    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null

    data class Builder(
        var lifecycleOwner: LifecycleOwner? = null,
        var viewFinder: PreviewView? = null,
        var cameraExecutor: ExecutorService? = null,
        var barcodeAnalyzer: BarcodeAnalyzer? = null,
    ) {
        fun barcodeAnalyzer(barcodeAnalyzer: BarcodeAnalyzer) =
            apply { this.barcodeAnalyzer = barcodeAnalyzer }

        fun onExecutor(cameraExecutor: ExecutorService) =
            apply { this.cameraExecutor = cameraExecutor }

        fun lifeCycleOwner(lifecycleOwner: LifecycleOwner) =
            apply { this.lifecycleOwner = lifecycleOwner }

        fun bindToViewFinder(viewFinder: PreviewView) =
            apply { this.viewFinder = viewFinder }

        fun build() = BakaCamera(
            lifecycleOwner = lifecycleOwner!!,
            viewFinder = viewFinder!!,
            cameraExecutor = cameraExecutor,
            barcodeAnalyzer = barcodeAnalyzer
        )
    }

    fun startPreview() {
        val metrics = DisplayMetrics().also { viewFinder.display.getRealMetrics(it) }
        Log.d(TAG, "Screen metrics: ${metrics.widthPixels} x ${metrics.heightPixels}")
        val screenAspectRatio = aspectRatio(metrics.widthPixels, metrics.heightPixels)
        Log.d(TAG, "Preview aspect ratio: $screenAspectRatio")
        val rotation = viewFinder.display.rotation

        // Bind the CameraProvider to the LifeCycleOwner
        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
        val cameraProviderFuture = ProcessCameraProvider.getInstance(viewFinder.context)
        cameraProviderFuture.addListener({
            // CameraProvider
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            // Preview
            preview = Preview.Builder()
                .setTargetAspectRatio(screenAspectRatio)
                .setTargetRotation(rotation)
                .build()
            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .setTargetAspectRatio(screenAspectRatio)
                .setTargetRotation(rotation)
                .build()
            imageAnalyzer = ImageAnalysis.Builder()
                .setTargetAspectRatio(screenAspectRatio)
                .setTargetRotation(rotation)
                .build()
            if (barcodeAnalyzer != null && cameraExecutor != null) {
                imageAnalyzer?.setAnalyzer(cameraExecutor, barcodeAnalyzer)
            }
            cameraProvider.unbindAll()
            try {
                camera = cameraProvider.bindToLifecycle(
                    lifecycleOwner, cameraSelector, preview, imageCapture, imageAnalyzer
                )
                preview?.setSurfaceProvider(viewFinder.createSurfaceProvider(camera?.cameraInfo))
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(viewFinder.context))
    }

    fun startPreview(barcodeAnalyzer: BarcodeAnalyzer) {
        val metrics = DisplayMetrics().also { viewFinder.display.getRealMetrics(it) }
        Log.d(TAG, "Screen metrics: ${metrics.widthPixels} x ${metrics.heightPixels}")
        val screenAspectRatio = aspectRatio(metrics.widthPixels, metrics.heightPixels)
        Log.d(TAG, "Preview aspect ratio: $screenAspectRatio")
        val rotation = viewFinder.display.rotation

        // Bind the CameraProvider to the LifeCycleOwner
        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
        val cameraProviderFuture = ProcessCameraProvider.getInstance(viewFinder.context)
        cameraProviderFuture.addListener({
            // CameraProvider
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            // Preview
            preview = Preview.Builder()
                .setTargetAspectRatio(screenAspectRatio)
                .setTargetRotation(rotation)
                .build()
            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .setTargetAspectRatio(screenAspectRatio)
                .setTargetRotation(rotation)
                .build()
            imageAnalyzer = ImageAnalysis.Builder()
                .setTargetAspectRatio(screenAspectRatio)
                .setTargetRotation(rotation)
                .build()
            if (barcodeAnalyzer != null && cameraExecutor != null) {
                imageAnalyzer?.setAnalyzer(cameraExecutor, barcodeAnalyzer)
            }
            cameraProvider.unbindAll()
            try {
                camera = cameraProvider.bindToLifecycle(
                    lifecycleOwner, cameraSelector, preview, imageCapture, imageAnalyzer
                )
                preview?.setSurfaceProvider(viewFinder.createSurfaceProvider(camera?.cameraInfo))
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(viewFinder.context))
    }

    fun stopPreview() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(viewFinder.context)
        val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
        cameraProvider.unbindAll()
    }

    fun updateRotation(rotation: Int) {
        imageCapture?.targetRotation = rotation
        imageAnalyzer?.targetRotation = rotation
    }

    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }
}