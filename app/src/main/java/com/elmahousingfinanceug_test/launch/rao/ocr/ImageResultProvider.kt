package com.elmahousingfinanceug_test.launch.rao.ocr

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageView
import com.yalantis.ucrop.util.BitmapLoadUtils.calculateInSampleSize
import java.io.File
import java.io.FileInputStream


fun options(): CropImageContractOptions {
    return com.canhub.cropper.options {
        setGuidelines(CropImageView.Guidelines.ON)
        setImageSource(includeGallery = true, includeCamera = true)
        setOutputCompressQuality(50)
    }

}

fun getImageFromStorage(path: String): Bitmap? {
    val f = File(path)
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = false
    options.inSampleSize = calculateInSampleSize(options, 512, 512)
    return BitmapFactory.decodeStream(FileInputStream(f), null, options)
}