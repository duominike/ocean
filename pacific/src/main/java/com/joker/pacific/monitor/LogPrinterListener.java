package com.joker.pacific.monitor;

/**
 * Created by joker on 17-5-14.
 */

public interface LogPrinterListener {
    void onStartLoop();
    void onEndLoop(long startTime, long endTime, String logInfo, int level);
}
