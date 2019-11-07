package fobo66.exchangecourcesbelarus.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.commons.io.IOUtil
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
  private val cacheFileName: String = "data.xml"
) {

  suspend fun writeToCache(dataStream: Reader) = withContext(Dispatchers.IO) {
    val cacheFile = File(cacheDirectory, cacheFileName).bufferedWriter()

    IOUtil.copy(dataStream, cacheFile)
  }

  suspend fun readCached(block: FileInputStream.() -> Unit) = withContext(Dispatchers.IO) {
    val cacheFile = File(cacheDirectory, cacheFileName)

    FileInputStream(cacheFile).use {
      block(it)
    }
  }
}