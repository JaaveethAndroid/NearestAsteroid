package dev.filipebezerra.android.nearearthasteroids.util.ext

import android.graphics.Bitmap
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.signature.ObjectKey

/**
 * Apply the following Glide improvements:
 * - Enable disk cache using [DiskCacheStrategy.RESOURCE] which writes resources to disk after they've been decoded
 * - Add Image Signature using the [objectKey] which is used for versioning in cache
 * - Set compression format to use PNG
 */
fun <T> RequestBuilder<T>.applyImprovements(objectKey: Any? = null) = apply {
    diskCacheStrategy(DiskCacheStrategy.RESOURCE)
    objectKey?.let { key -> signature(ObjectKey(key)) }
    encodeFormat(Bitmap.CompressFormat.PNG)
}