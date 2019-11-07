package fobo66.exchangecourcesbelarus.model

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.apache.commons.io.IOUtils
import java.io.File
import java.io.FileInputStream
import java.io.Reader
import javax.inject.Inject

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/4/19.
 */
class CacheDataSource @Inject constructor(
  private val cacheDirectory: File,
  private val cacheFileName: String = "data.xml",
  private val ioDispatcher: CoroutineDispatcher
) {

  suspend fun writeToCache(dataStream: Reader) = withContext(ioDispatcher) {
    File(cacheDirectory, cacheFileName).bufferedWriter().use {
      IOUtils.copy(dataStream, it)
    }
  }

  suspend fun readCached(block: FileInputStream.() -> Unit) = withContext(ioDispatcher) {
    val cacheFile = File(cacheDirectory, cacheFileName)

    FileInputStream(cacheFile).use {
      block(it)
    }
  }
}