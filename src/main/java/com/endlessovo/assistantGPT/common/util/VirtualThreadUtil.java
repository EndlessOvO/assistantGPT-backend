package com.endlessovo.assistantGPT.common.util;

import lombok.experimental.UtilityClass;

import java.util.concurrent.ThreadFactory;

@UtilityClass
public class VirtualThreadUtil {
    private static final ThreadFactory factory = Thread.ofVirtual().name("virtual-", 0).factory();

    public static void run(Runnable runnable){
        factory.newThread(runnable).start();
    }
}
