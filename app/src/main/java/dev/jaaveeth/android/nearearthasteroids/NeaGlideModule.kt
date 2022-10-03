package dev.filipebezerra.android.nearearthasteroids

import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DecodeFormat.PREFER_ARGB_8888
import com.bumptech.glide.load.DecodeFormat.PREFER_RGB_565
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions

@GlideModule
class NeaGlideModule : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        // Prefer higher quality images unless we're on a low RAM device
        val activityManager = (context.getSystemService(ACTIVITY_SERVICE) as ActivityManager)
        builder.setDefaultRequestOptions {
            RequestOptions()
                .format(if (activityManager.isLowRamDevice) PREFER_RGB_565 else PREFER_ARGB_8888)
                // Disable hardware bitmaps as they don't play nicely with Palette
                .disallowHardwareConfig()
        }
    }

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}