package com.joker.pacific.monitor;

/**
 * Created by joker on 17-5-14.
 */

public interface UiPerfMonitorConfig {
    // 定义卡顿靠警阈值
    public static int TIME_WARNING_LEVEL_1 = 100;
    // 需要上报线程信息阈值
    public static int TIME_WARNING_LEVEL_2 = 300;
}
