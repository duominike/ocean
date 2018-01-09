package com.joker.pacific.imageload;

import java.util.concurrent.Executor;

/**
 * Created by joker on 18-1-9.
 */

public class ImageLoaderConfig {
    public static int DEFAULT_MEM_CACHESIZE = 12; // 总ram可用内存的百分比
    public static int DEFAULT_DISK_CACHE_SIZE = 1024 * 1024 * 200; // kb
    private int memCacheSize;
    private int diskCacheSize;
    private String diskCachePath;
    private Executor executor;

    public int getMemCacheSize() {
        return memCacheSize;
    }

    public void setMemCacheSize(int memCacheSize) {
        this.memCacheSize = memCacheSize;
    }

    public int getDiskCacheSize() {
        return diskCacheSize;
    }

    public void setDiskCacheSize(int diskCacheSize) {
        this.diskCacheSize = diskCacheSize;
    }

    public String getDiskCachePath() {
        return diskCachePath;
    }

    public void setDiskCachePath(String diskCachePath) {
        this.diskCachePath = diskCachePath;
    }

    public Executor getExecutors() {
        return executor;
    }

    public void setExecutor(Executor executors) {
        this.executor = executors;
    }

    public static class Builder {
        private int memCacheSize;
        private int diskCacheSize;
        private String diskCachePath;
        private Executor executor;

        public Builder memCacheSize(int memCacheSize) {
            this.memCacheSize = memCacheSize;
            return this;
        }

        public Builder fromExecutors(Executor executors) {
            this.executor = executors;
            return this;
        }

        public Builder diskCacheSize(int diskCacheSize) {
            this.diskCacheSize = diskCacheSize;
            return this;
        }

        public Builder diskCachePath(String diskCachePath) {
            this.diskCachePath = diskCachePath;
            return this;
        }

        public ImageLoaderConfig build() {
            ImageLoaderConfig config = new ImageLoaderConfig();
            config.diskCachePath = this.diskCachePath;
            config.executor = this.executor;
            config.memCacheSize = this.memCacheSize;
            config.diskCacheSize = this.diskCacheSize;
            return config;
        }
    }
}
