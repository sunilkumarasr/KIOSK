package com.provizit.kioskcheckin.services;
// GlobalSettings.java
public class GlobalSettings {
    private static GlobalSettings instance;
    public boolean isScannerConnected = false;

    private GlobalSettings() {
        // Private constructor to prevent instantiation
    }

    public static synchronized GlobalSettings getInstance() {
        if (instance == null) {
            instance = new GlobalSettings();
        }
        return instance;
    }
}
