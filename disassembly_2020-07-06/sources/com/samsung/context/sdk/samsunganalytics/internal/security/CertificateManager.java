package com.samsung.context.sdk.samsunganalytics.internal.security;

import com.samsung.context.sdk.samsunganalytics.internal.util.Debug;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class CertificateManager {
    private static final String CA_STORE_NAME = "AndroidCAStore";
    private static final String SSL_PROTOCOL = "TLS";
    private SSLContext sslContext;

    private CertificateManager() {
        pinningSystemCA();
    }

    private static class Singleton {
        /* access modifiers changed from: private */
        public static final CertificateManager instance = new CertificateManager();

        private Singleton() {
        }
    }

    public static CertificateManager getInstance() {
        return Singleton.instance;
    }

    private void pinningSystemCA() {
        try {
            KeyStore instance = KeyStore.getInstance(KeyStore.getDefaultType());
            instance.load((InputStream) null, (char[]) null);
            KeyStore instance2 = KeyStore.getInstance(CA_STORE_NAME);
            instance2.load((InputStream) null, (char[]) null);
            Enumeration<String> aliases = instance2.aliases();
            while (aliases.hasMoreElements()) {
                String nextElement = aliases.nextElement();
                X509Certificate x509Certificate = (X509Certificate) instance2.getCertificate(nextElement);
                if (nextElement.startsWith("system:")) {
                    instance.setCertificateEntry(nextElement, x509Certificate);
                }
            }
            TrustManagerFactory instance3 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            instance3.init(instance);
            this.sslContext = SSLContext.getInstance(SSL_PROTOCOL);
            this.sslContext.init((KeyManager[]) null, instance3.getTrustManagers(), (SecureRandom) null);
            Debug.LogENG("pinning success");
        } catch (Exception e) {
            Debug.LogENG("pinning fail : " + e.getMessage());
        }
    }

    public SSLContext getSSLContext() {
        return this.sslContext;
    }
}
