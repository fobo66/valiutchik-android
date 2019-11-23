package fobo66.exchangecourcesbelarus.model.datasource

import fobo66.exchangecourcesbelarus.di.CacheDirectory
import fobo66.exchangecourcesbelarus.di.Io
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okio.Source
import okio.buffer
import okio.sink
import java.io.File
import java.io.FileInputStream
import javax.inject.Inject

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/4/19.
 */
class CacheDataSource @Inject constructor(
  @CacheDirectory private val cacheDirectory: File,
  @Io private val ioDispatcher: CoroutineDispatcher
) {

  suspend fun writeToCache(dataStream: Source?, cacheFileName: String = "data.xml") =
    withContext(ioDispatcher) {
      if (dataStream != null) {
        val file = File(cacheDirectory, cacheFileName)
        file.createNewFile()
        file.sink().buffer().use { sink ->
          sink.writeAll(dataStream)
        }
      }
    }

  suspend fun readCached(cacheFileName: String = "data.xml", block: FileInputStream.() -> Unit) =
    withContext(ioDispatcher) {
      val cacheFile = File(cacheDirectory, cacheFileName)

      if (cacheFile.exists() && cacheFile.length() > 0) {
        FileInputStream(cacheFile).use {
          block(it)
        }
      }
    }
}