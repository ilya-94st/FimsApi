package com.example.films.common

import android.content.Context
import java.io.File
import java.util.*

object FileUtils {

    fun createFile(fileName: String, dir: String, app: Context): File {
        val suffix = if (fileName.contains('.')) {
            ".${fileName.substringAfterLast('.').lowercase(Locale.getDefault())}"
        } else {
            ""
        }
        val randomPostfix = UUID.randomUUID().toString()

        val name = if (fileName.startsWith('.')) {
            "${
                when (suffix) {
                    ".jpg", ".png", ".gif", "bmp" -> "IMG"
                    ".mp4" -> "VID"
                    else -> "DOC"
                }
            }_$randomPostfix"
        } else {
            fileName.substringBeforeLast('.')
        }

        val storageDir = File(app.filesDir, dir)

        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }

        var imageFile: File

        var counter = 0

        do {
            val postfix = if (fileName.startsWith('.')) {
                if (counter != 0) "$counter" else ""
            } else {
                if (counter != 0) "($counter)" else ""
            }

            imageFile = File(storageDir, "$name$postfix$suffix")

            counter++
        } while (imageFile.exists())

        return imageFile
    }
}