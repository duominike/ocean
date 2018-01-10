package com.joker.pacific.imageload.cache;

import com.jakewharton.disklrucache.DiskLruCache;
import com.joker.pacific.imageload.CloseUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by joker on 18-1-10.
 */

public class DiskCache {
    private DiskLruCache mDiskLruCache;
    private String diskCachePath;
    private int diskCacheSize = 0;
    private boolean bDiskCacheOpened = false;
    private static final int DISK_CACHE_INDEX = 0;

    public DiskCache(String diskCachePathSet, int diskCacheSizeSet) {
        this.diskCachePath = diskCachePathSet;
        this.diskCacheSize = diskCacheSizeSet;
        File file = new File(diskCachePath);
        if (!file.exists()) {
            file.mkdirs();
        }

        try {
            mDiskLruCache = DiskLruCache.open(file, 1, 1, diskCacheSize);
            bDiskCacheOpened = true;
        } catch (IOException e) {
            bDiskCacheOpened = false;
        }

    }

    public InputStream get(String url) throws IOException {
        String key = hashKeyFromUrl(url);
        DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
        if (snapshot != null) {
            return snapshot.getInputStream(DISK_CACHE_INDEX);
        }
        return null;
    }

    public void putCache(String url, InputStream inputStream) throws IOException {
        checkLruCacheState();
        String key = hashKeyFromUrl(url);
        DiskLruCache.Editor editor = mDiskLruCache.edit(key);
        OutputStream outputStream = null;
        try{
            if (editor != null) {
                outputStream = editor.newOutputStream(0);
                byte[] buffer = new byte[1024];
                int size = 0;
                while ((size = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, size);
                }
                editor.commit();
            }
        }catch (Exception e){
            editor.abort();
        }finally {
            CloseUtil.close(outputStream);
        }
        mDiskLruCache.flush();
    }

    private void checkLruCacheState() {
        if (!bDiskCacheOpened) {
            throw new IllegalStateException("DiskLruCache not opened Success!");
        }
    }

    private String hashKeyFromUrl(String url) {
        checkLruCacheState();
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
