package fobo66.exchangecourcesbelarus.model

import fobo66.exchangecourcesbelarus.model.datasource.CacheDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import okio.source
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/7/19.
 */
class CacheDataSourceTest {

  @get:Rule
  val temporaryFolder = TemporaryFolder()

  @ObsoleteCoroutinesApi
  val ioDispatcher = newSingleThreadContext("IO")

  private lateinit var cacheDataSource: CacheDataSource

  @ObsoleteCoroutinesApi
  @Before
  fun setUp() {
    cacheDataSource =
      CacheDataSource(
        temporaryFolder.root,
        ioDispatcher = ioDispatcher
      )
  }

  @ObsoleteCoroutinesApi
  @After
  fun tearDown() {
    ioDispatcher.close()
  }

  @ObsoleteCoroutinesApi
  @ExperimentalCoroutinesApi
  @Test
  fun writeToCache() {
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
  fun readCached() {
    val tempFile = temporaryFolder.newFile("data.xml")
    tempFile.writeText("test")

    runBlocking {
      cacheDataSource.readCached {
        assertEquals("test", String(readBytes()))
      }
    }
  }
}