package fobo66.exchangecourcesbelarus.model

import fobo66.exchangecourcesbelarus.model.datasource.CacheDataSource
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.runBlocking
import okio.source
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File
import java.util.concurrent.Executors

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/7/19.
 */
class CacheDataSourceTest {

  @get:Rule
  val temporaryFolder = TemporaryFolder()

  private val ioDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

  private lateinit var cacheDataSource: CacheDataSource

  @Before
  fun setUp() {
    cacheDataSource =
      CacheDataSource(
        temporaryFolder.root,
        ioDispatcher = ioDispatcher
      )
  }

  @After
  fun tearDown() {
    ioDispatcher.close()
  }

  @Test
  fun `write to cache`() {
    val tempFile = temporaryFolder.newFile()
    tempFile.writeText("test")

    val reader = tempFile.source()
    runBlocking {
      cacheDataSource.writeToCache(reader)
    }
    val cachedFile = File(temporaryFolder.root, "data.xml")
    assertEquals("test", cachedFile.readText())
  }

  @Test
  fun `read from cache`() {
    val tempFile = temporaryFolder.newFile("data.xml")
    tempFile.writeText("test")

    runBlocking {
      cacheDataSource.readCached {
        assertEquals("test", String(readBytes()))
      }
    }
  }
}