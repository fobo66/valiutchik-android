package fobo66.exchangecourcesbelarus.model

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
  private val cacheDirectory: File,
  private val cacheFileName: String = "data.xml",
  @Io private val ioDispatcher: CoroutineDispatcher
) {

  suspend fun writeToCache(dataStream: Source) = withContext(ioDispatcher) {
    File(cacheDirectory, cacheFileName).sink().buffer().use {
      it.writeAll(dataStream)
    }
  }

  suspend fun readCached(block: FileInputStream.() -> Unit) = withContext(ioDispatcher) {
    val cacheFile = File(cacheDirectory, cacheFileName)

    FileInputStream(cacheFile).use {
      block(it)
    }
  }
}