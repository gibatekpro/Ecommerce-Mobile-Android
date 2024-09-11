package com.gibatekpro.ecommerceandroid.util;

import android.app.Activity;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class SsLTrust {

    static SSLContext sslContext;

    public X509TrustManager X509TrustManager(Activity context) {


        try {
// Load the .p12 file from disk
            InputStream p12InputStream = context.getAssets().open("gibatekpro-keystore.p12");
// Load the password for the .p12 file
            char[] password = "secret".toCharArray(); // Replace "password" with your actual password

// Create a KeyStore instance and load the .p12 file
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(p12InputStream, password);

// Create a KeyManagerFactory with the loaded KeyStore and password
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, password);

            TrustManagerFactory trustManagerFactory = TrustManagerFactory
                    .getInstance(TrustManagerFactory.getDefaultAlgorithm());

            trustManagerFactory.init(keyStore);

            X509TrustManager trustManager = (X509TrustManager) trustManagerFactory.getTrustManagers()[0];

// Create an SSLContext and initialize it with the KeyManagerFactory
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

            sslContext.init(null, new TrustManager[]{trustManager}, null);

            return trustManager;

        } catch (UnrecoverableKeyException e) {
            throw new RuntimeException(e);
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        }

    }

    public SSLContext getSslContext() {
        return sslContext;
    }
}