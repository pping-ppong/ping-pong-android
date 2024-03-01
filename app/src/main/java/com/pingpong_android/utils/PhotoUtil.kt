package com.pingpong_android.utils

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

object PhotoUtil {

    private const val MAX_WIDTH = 1280
    private const val MAX_HEIGHT = 960

    fun makeImageFileName(): String {
        val simpleDateFormat = SimpleDateFormat("yyyyMMdd_hhmmss")
        val date = Date()
        val strDate = simpleDateFormat.format(date)
        return "$strDate.png"
    }

    fun optimizeBitmap(context: Context, uri: Uri): Bitmap? {
        try {
            val storage = context.cacheDir
            val fileName = String.format("%s.%s", UUID.randomUUID(), "jpg")

            val tempFile = File(storage, fileName)
            tempFile.createNewFile()

            val fos = FileOutputStream(tempFile)

            decodeBitmapFromUri(uri, context)?.apply {
                compress(Bitmap.CompressFormat.JPEG, 100, fos)
                recycle()
            } ?: throw NullPointerException()

            fos.flush()
            fos.close()

            return pathToBitmap(tempFile.absolutePath)

        } catch (e:Exception) {
            Log.e(TAG, "FileUtil - ${e.message}")
        }

        return null
    }

    fun pathToBitmap(path: String?): Bitmap? {
        return try {
            val f = File(path)
            val options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            BitmapFactory.decodeStream(FileInputStream(f), null, options)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun decodeBitmapFromUri(uri: Uri, context: Context): Bitmap? {

        val input = BufferedInputStream(context.contentResolver.openInputStream(uri))

        input.mark(input.available())

        var bitmap: Bitmap?

        BitmapFactory.Options().run {
            inJustDecodeBounds = true
            bitmap = BitmapFactory.decodeStream(input, null, this)

            input.reset()

            inSampleSize = calculateInSampleSize(this)
            inJustDecodeBounds = false

            bitmap = BitmapFactory.decodeStream(input, null, this)?.apply {
                rotateImageIfRequired(context, this, uri)
            }
        }

        input.close()

        return bitmap

    }

    private fun calculateInSampleSize(options: BitmapFactory.Options): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > MAX_HEIGHT || width > MAX_WIDTH) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= MAX_HEIGHT && halfWidth / inSampleSize >= MAX_WIDTH) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    private fun rotateImageIfRequired(context: Context, bitmap: Bitmap, uri: Uri): Bitmap? {
        val input = context.contentResolver.openInputStream(uri) ?: return null

        val exif = if (Build.VERSION.SDK_INT > 23) {
            ExifInterface(input)
        } else {
            ExifInterface(uri.path!!)
        }

        val orientation =
            exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270)
            else -> bitmap
        }
    }

    private fun rotateImage(bitmap: Bitmap, degree: Int): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
}