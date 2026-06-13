package com.illera.peakprofit

import android.app.Application
import android.os.Build
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PeakProFitApp : Application(), ImageLoaderFactory {
    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .components {
                // ExerciseDB devuelve GIF reales en el endpoint /image. Registramos
                // el decoder explícitamente para que Coil los trate como animados.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
    }
}
