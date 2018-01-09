package com.joker.pacific.imageload.producer;

import com.jakewharton.disklrucache.DiskLruCache;
import com.joker.pacific.imageload.CloseUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executor;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by joker on 18-1-9.
 */

public class DiskCacheCacheProducer implements Producer<InputStream> {
    private String diskCachePath;
    private int diskCacheSize = 0;
    private Executor mExecutor;
    private DiskLruCache mDiskLruCache;
    private boolean mDiskLruCacheInited = false;
    private Producer mProducer;
    private static final int DISK_CACHE_INDEX = 0;

    public DiskCacheCacheProducer(String diskCachePath, int diskCacheSize, Executor executor) {
        this.diskCachePath = diskCachePath;
        this.diskCacheSize = diskCacheSize;
        this.mExecutor = executor;
        File file = new File(diskCachePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            mDiskLruCache = DiskLruCache.open(file, 1, 1, diskCacheSize);
            mDiskLruCacheInited = true;
        } catch (IOException e) {
            mDiskLruCache = null;
            mDiskLruCacheInited = false;
        }
    }

    @Override
    public void setProducer(Producer producer) {
        this.mProducer = producer;
    }

    @Override
    public Observable<InputStream> produce(final ImageRequest imgRequest) {
        String key = hashKeyFromUrl(imgRequest.url);
        try {
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
            if (snapshot != null) {
                FileInputStream fis = (FileInputStream) snapshot.getInputStream(DISK_CACHE_INDEX);
                return Observable.just((InputStream) fis).subscribeOn(Schedulers.from(mExecutor));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (mProducer != null) {
            return ((Observable<InputStream>) (mProducer.produce(imgRequest)))
                    .filter(new Predicate<InputStream>() {
                        @Override
                        public boolean test(@NonNull InputStream inputStream) throws Exception {
                            BufferedOutputStream bos = null;
                            BufferedInputStream bis = null;
                            try {
                                String key = hashKeyFromUrl(imgRequest.url);
                                DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                                OutputStream outputStream = null;
                                if (editor != null) {
                                    outputStream = editor.newOutputStream(DISK_CACHE_INDEX);
                                }
                                if(outputStream == null){
                                    return true;
                                }
                                bis = new BufferedInputStream(inputStream, 8 * 1024);
                                bos = new BufferedOutputStream(outputStream, 8 * 1024);
                                int b;
                                while ((b = bis.read()) != -1) {
                                    bos.write(b);
                                }
                                return true;
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                CloseUtil.close(bis);
                                CloseUtil.close(bos);
                            }
                            return true;
                        }
                    }).subscribeOn(Schedulers.from(mExecutor));
        }
        return null;
    }

    private String hashKeyFromUrl(String url) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(url.getBytes());
            cacheKey = byteToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(url.hashCode());
        }
        return cacheKey;
    }

    private String byteToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);//得到十六进制字符串
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

}
