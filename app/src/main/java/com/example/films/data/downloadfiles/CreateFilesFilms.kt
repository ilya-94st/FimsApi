package com.example.films.data.downloadfiles

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.util.Log
import com.example.films.common.FileUtils
import com.example.films.data.api.ApiFilms
import com.example.films.domain.model.entinity.EntityFilms
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class CreateFilesFilms(private val api: ApiFilms, private val app: Context) {

    private fun downloadFile(fileUrl: String): ResponseBody? {
        val call = api.downloadFile(fileUrl)
        val response = call.execute()
        return if (response.isSuccessful && response.body() != null) {
            response.body()
        } else {
            response.errorBody()
        }
    }

    suspend fun downloadArticle(films: EntityFilms) {
        withContext(Dispatchers.IO){
            val response = films.Poster?.let { downloadFile(it) }
            response.let {
                if (it != null) {
                    val path = writeResponseBodyToDisk(it, ".png")
                    films.path = path
                } else {
                    Log.e("RepositoryNews", "!!!!!!!!!!!!!!!!!error")
                }
            }
        }
    }

    private fun writeResponseBodyToDisk(body: ResponseBody, fileName: String): String {
        var path = ""
        try {
            val file = FileUtils.createFile(fileName, Environment.DIRECTORY_DOWNLOADS, app)
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null

            try {
                val fileReader = ByteArray(4096)
                val fileSize = body.contentLength()
                var fileSizeDownloaded: Long = 0

                inputStream = body.byteStream()
                outputStream = FileOutputStream(file)

                loop@while(true) {
                    when(val read = inputStream.read(fileReader)) {
                        0,
                        -1 -> break@loop
                        else -> {
                            outputStream.write(fileReader, 0, read)
                            fileSizeDownloaded += read.toLong()
                            Log.d(ContentValues.TAG, "file download: $fileSizeDownloaded of $fileSize")
                        }
                    }
                }
                path = file.absolutePath
            } catch (e: IOException) {
                Log.e("RepositoryNews", "$e")
                try {
                    file.delete()
                } catch (ignore: Exception) {

                }
            } finally {
                inputStream?.close()

                outputStream?.close()
            }
        } catch (e: IOException) {
            Log.e("RepositoryNews", "$e")
        }
        return path
    }
}