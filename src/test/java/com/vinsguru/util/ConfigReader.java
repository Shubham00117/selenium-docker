package com.vinsguru.util;

import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final Properties properties = new Properties();

    static {
        try {
            InputStream is = ResourceLoader.getResource("config.properties");

            if (is == null) {
                throw new RuntimeException("❌ config.properties NOT found in classpath");
            }

            properties.load(is);
            System.out.println("✅ config.properties loaded");

        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to load config.properties", e);
        }
    }

    // 🔥 MAIN METHOD (with CLI override support)
    public static String get(String key) {

        // 1. Check command line override
        String value = System.getProperty(key);

        // 2. Else read from config file
        if (value == null) {
            value = properties.getProperty(key);
        }

        // 3. Validate
        if (value == null || value.trim().isEmpty()) {
            throw new RuntimeException("❌ Missing key in config: " + key);
        }

        return value.trim();
    }

    // 🔥 OPTIONAL method (with default value)
    public static String get(String key, String defaultValue) {

        String value = System.getProperty(key);

        if (value == null) {
            value = properties.getProperty(key);
        }

        return (value != null && !value.trim().isEmpty())
                ? value.trim()
                : defaultValue;
    }
}