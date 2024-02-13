package com.goznak.utils;

import com.goznak.COMterminalApplication;
import org.slf4j.LoggerFactory;

public class Logger {
    static final org.slf4j.Logger logger = LoggerFactory.getLogger(COMterminalApplication.class);
    public static void info(String text){
        logger.info(text);
    }
    public static void error(String text, Throwable throwable){
        logger.error(text, throwable);
    }
}
