package fobo66.exchangecourcesbelarus.util

import java.io.IOException
import java.io.InputStream
import java.security.GeneralSecurityException
import java.security.KeyStore
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateFactory
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

/**
 * (c) 2018 Andrey Mukamolov
 * Created 2/18/18.
 */
@Singleton
class CertificateManager @Inject constructor() {

  private val sslContext: SSLContext = try {
    SSLContext.getInstance("TLSv1.2")
  } catch (e: NoSuchAlgorithmException) {
    throw IllegalStateException("Cannot load TLS socket factory", e)
  }

  lateinit var trustManager: X509TrustManager

  @Throws(GeneralSecurityException::class)
  fun createTrustManagerForCertificate(cert: InputStream) {
    val certificateFactory = CertificateFactory.getInstance("X.509")
    val certificates =
      certificateFactory.generateCertificates(cert)
    require(!certificates.isEmpty()) { "expected non-empty set of trusted certificates" }
    // Put the certificates a key store.
    val password = "password".toCharArray() // Any password will work.
    val keyStore = newEmptyKeyStore(password)
    for ((index, certificate) in certificates.withIndex()) {
      val certificateAlias = index.toString()
      keyStore.setCertificateEntry(certificateAlias, certificate)
    }
    // Use it to build an X509 trust manager.
    val keyManagerFactory =
      KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
    keyManagerFactory.init(keyStore, password)
    val trustManagerFactory =
      TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
    trustManagerFactory.init(keyStore)
    val trustManagers = trustManagerFactory.trustManagers
    check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
      "Unexpected default trust managers:" + Arrays.toString(
        trustManagers
      )
    }
    trustManager = trustManagers[0] as X509TrustManager
    sslContext.init(null, trustManagers, null)
  }

  val trustedSocketFactory: SSLSocketFactory
    get() = sslContext.socketFactory

  @Throws(GeneralSecurityException::class)
  private fun newEmptyKeyStore(password: CharArray): KeyStore {
    return try {
      val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
      val inputStream: InputStream? = null // By convention, 'null' creates an empty key store.
      keyStore.load(inputStream, password)
      keyStore
    } catch (e: IOException) {
      throw AssertionError("Failed to create keystore", e)
    }
  }
}
