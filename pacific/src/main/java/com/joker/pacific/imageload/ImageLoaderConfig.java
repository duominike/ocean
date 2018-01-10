package com.joker.pacific.imageload;

/**
 * Created by joker on 18-1-9.
 */

public class ImageLoaderConfig {
    public static int DEFAULT_MEM_CACHESIZE = 12; // 总ram可用内存的百分比
    public static int DEFAULT_DISK_CACHE_SIZE = 1024 * 1024 * 200; // mb
    private int memCacheSize;
    private int diskCacheSize;
    private String diskCachePath;
    private int targetDensity;

    private ImageLoaderConfig() {

    }

    public int getMemCacheSize() {
        return memCacheSize;
    }


    public int getDiskCacheSize() {
        return diskCacheSize;
    }


    public String getDiskCachePath() {
        return diskCachePath;
    }


    public int getTargetDensity() {
        return targetDensity;
    }

    public static class Builder {
        private int memCacheSize;
        private int diskCacheSize;
        private String diskCachePath;
        private int targetDensity;

        public Builder memCacheSize(int memCacheSize) {
            this.memCacheSize = memCacheSize;
            return this;
        }

        public Builder targetDensity(int targetDensity) {
            this.targetDensity = targetDensity;
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
            config.targetDensity = this.targetDensity;
            config.memCacheSize = this.memCacheSize;
            config.diskCacheSize = this.diskCacheSize;
            return config;
        }
    }
}
