package com.hashmob.aichat.main.home.ui.drawer

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import kotlin.math.roundToInt

object BlurBuilder {
    private const val BITMAP_SCALE = 0.2f
    private const val BLUR_RADIUS = 25f
    fun blur(context: Context?, image: Bitmap): Bitmap {
        val width = (image.width * BITMAP_SCALE).roundToInt()
            .toInt()
        val height = (image.height * BITMAP_SCALE).roundToInt()
            .toInt()
        val inputBitmap: Bitmap = Bitmap.createScaledBitmap(image, width, height, true)
        val outputBitmap: Bitmap = Bitmap.createBitmap(inputBitmap)
        val rs: RenderScript = RenderScript.create(context)
        val theIntrinsic: ScriptIntrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
        val tmpIn: Allocation = Allocation.createFromBitmap(rs, inputBitmap)
        val tmpOut: Allocation = Allocation.createFromBitmap(rs, outputBitmap)
        theIntrinsic.setRadius(BLUR_RADIUS)
        theIntrinsic.setInput(tmpIn)
        theIntrinsic.forEach(tmpOut)
        tmpOut.copyTo(outputBitmap)
        return outputBitmap
    }
}