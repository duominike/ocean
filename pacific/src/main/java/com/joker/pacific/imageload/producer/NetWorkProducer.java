package com.joker.pacific.imageload.producer;

import android.net.Uri;
import android.util.Log;

import com.joker.pacific.imageload.CloseUtil;
import com.joker.pacific.imageload.ImageLoader;
import com.joker.pacific.log.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by joker on 18-1-9.
 */

class NetWorkProducer implements Producer<InputStream> {
    private Logger mLogger = Logger.getLogger(NetWorkProducer.class);
    private static final int HTTP_TEMPORARY_REDIRECT = 307;
    private static final int HTTP_PERMANENT_REDIRECT = 308;
    private static final int MAX_REDIRECTS = 5;

    public NetWorkProducer() {
    }

    private static boolean isHttpSuccess(int responseCode) {
        return (responseCode >= HttpURLConnection.HTTP_OK &&
                responseCode < HttpURLConnection.HTTP_MULT_CHOICE);
    }

    private static boolean isHttpRedirect(int responseCode) {
        switch (responseCode) {
            case HttpURLConnection.HTTP_MULT_CHOICE:
            case HttpURLConnection.HTTP_MOVED_PERM:
            case HttpURLConnection.HTTP_MOVED_TEMP:
            case HttpURLConnection.HTTP_SEE_OTHER:
            case HTTP_TEMPORARY_REDIRECT:
            case HTTP_PERMANENT_REDIRECT:
                return true;
            default:
                return false;
        }
    }

    private HttpURLConnection openConnectionTo(Uri uri) throws IOException {
        URL url = new URL(uri.toString());
        return (HttpURLConnection) url.openConnection();
    }

    private HttpURLConnection downloadFrom(Uri uri, int maxRedirects) throws IOException {
        HttpURLConnection connection = openConnectionTo(uri);
        int responseCode = connection.getResponseCode();

        if (isHttpSuccess(responseCode)) {
            return connection;

        } else if (isHttpRedirect(responseCode)) {
            String nextUriString = connection.getHeaderField("Location");
            connection.disconnect();

            Uri nextUri = (nextUriString == null) ? null : Uri.parse(nextUriString);
            String originalScheme = uri.getScheme();
            if (maxRedirects > 0 && nextUri != null && !nextUri.getScheme().equals(originalScheme)) {
                return downloadFrom(nextUri, maxRedirects - 1);
            } else {
                String errorMsg = "";
                if (maxRedirects == 0) {
                    errorMsg = error("URL %s follows too many redirects", uri.toString());
                } else {
                    errorMsg = error("URL %s returned %d without a valid redirect", uri.toString(), responseCode);
                }
                mLogger.error(errorMsg);
                throw new IOException(errorMsg);
            }

        } else {
            connection.disconnect();
            throw new IOException(String
                    .format("Image URL %s returned HTTP code %d", uri.toString(), responseCode));
        }
    }

    private String error(String format, Object... args) {
        return String.format(Locale.getDefault(), format, args);
    }


    @Override
    public Observable<InputStream> produce(final ImageLoader.ImageRequest imgRequest) {
        return Observable.create(new ObservableOnSubscribe<InputStream>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<InputStream> e) throws Exception {
                mLogger.info("produce: " + imgRequest.url);
                HttpURLConnection connection = null;
                InputStream inputStream = null;
                try {
                    connection = downloadFrom(Uri.parse(imgRequest.url), MAX_REDIRECTS);
                    if (connection != null) {
                        inputStream = connection.getInputStream();
                        e.onNext(inputStream);
                    } else {
                        e.onComplete();
                    }
                } catch (Exception exception) {
                    mLogger.error("produce: " + Log.getStackTraceString(exception));
                } finally {
                    mLogger.info("close networkStream");
                    CloseUtil.close(inputStream);
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
                e.onComplete();
            }
        });
    }
}
