package fobo66.exchangecourcesbelarus.util;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.Collection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * (c) 2018 Andrey Mukamolov
 * Created 2/18/18.
 */

public class CertificateManager {
  private final SSLContext sslContext;

  private X509TrustManager trustManager;

  public CertificateManager() {
    try {
      sslContext = SSLContext.getInstance("TLSv1.2");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Cannot load TLS socket factory", e);
    }
  }

  public void createTrustManagerForCertificate(InputStream cert) throws GeneralSecurityException {
    CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
    Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(cert);
    if (certificates.isEmpty()) {
      throw new IllegalArgumentException("expected non-empty set of trusted certificates");
    }

    // Put the certificates a key store.
    char[] password = "password".toCharArray(); // Any password will work.
    KeyStore keyStore = newEmptyKeyStore(password);
    int index = 0;
    for (Certificate certificate : certificates) {
      String certificateAlias = Integer.toString(index++);
      keyStore.setCertificateEntry(certificateAlias, certificate);
    }

    // Use it to build an X509 trust manager.
    KeyManagerFactory keyManagerFactory =
        KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    keyManagerFactory.init(keyStore, password);
    TrustManagerFactory trustManagerFactory =
        TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    trustManagerFactory.init(keyStore);
    TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
    if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
      throw new IllegalStateException(
          "Unexpected default trust managers:" + Arrays.toString(trustManagers));
    }
    trustManager = (X509TrustManager) trustManagers[0];
    sslContext.init(null, trustManagers, null);
  }

  public SSLSocketFactory getTrustedSocketFactory() {
    return sslContext.getSocketFactory();
  }

  public X509TrustManager getTrustManager() {
    return trustManager;
  }

  private KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
    try {
      KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
      InputStream in = null; // By convention, 'null' creates an empty key store.
      keyStore.load(in, password);
      return keyStore;
    } catch (IOException e) {
      throw new AssertionError(e);
    }
  }
}
