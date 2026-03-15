package com.framework.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ConfigManager - Singleton configuration manager
 * Loads environment-specific properties and supports runtime overrides.
 */
public class ConfigManager {

    private static final Logger log = LogManager.getLogger(ConfigManager.class);
    private static ConfigManager instance;
    private final Properties properties = new Properties();

    private static final String DEFAULT_CONFIG = "src/test/resources/config/config.properties";
    private static final String ENV_CONFIG_PREFIX = "src/test/resources/config/config-";

    private ConfigManager() {
        loadConfig();
    }

    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    private void loadConfig() {
        // Load base config
        loadFile(DEFAULT_CONFIG);

        // Load env-specific config (overrides base)
        String env = System.getProperty("env", System.getenv("ENV") != null ? System.getenv("ENV") : "");
        if (env != null && !env.isEmpty()) {
            String envConfig = ENV_CONFIG_PREFIX + env + ".properties";
            loadFile(envConfig);
            log.info("Loaded environment config: {}", envConfig);
        }

        // System properties override everything
        properties.putAll(System.getProperties());
        log.info("Configuration loaded successfully. Environment: {}", env.isEmpty() ? "default" : env);
    }

    private void loadFile(String path) {
        try (InputStream input = new FileInputStream(path)) {
            Properties temp = new Properties();
            temp.load(input);
            properties.putAll(temp);
            log.debug("Loaded config file: {}", path);
        } catch (IOException e) {
            log.warn("Config file not found or unreadable: {}. Using defaults.", path);
        }
    }

    // ─── Getters ───────────────────────────────────────────────────────────────

    public String get(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            log.warn("Property '{}' not found in configuration.", key);
        }
        return value;
    }

    public String get(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(get(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            log.warn("Property '{}' is not a valid integer. Using default: {}", key, defaultValue);
            return defaultValue;
        }
    }

    public long getLong(String key, long defaultValue) {
        try {
            return Long.parseLong(get(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return Boolean.parseBoolean(get(key, String.valueOf(defaultValue)));
    }

    // ─── Convenience shortcuts ─────────────────────────────────────────────────

    public String getBrowser()          { return get("browser", "chrome"); }
    public String getBaseUrl()          { return get("base.url", "https://www.saucedemo.com"); }
    public boolean isHeadless()         { return getBoolean("headless", false); }
    public int getImplicitWait()        { return getInt("implicit.wait", 10); }
    public int getExplicitWait()        { return getInt("explicit.wait", 20); }
    public int getPageLoadTimeout()     { return getInt("page.load.timeout", 30); }
    public String getScreenshotDir()    { return get("screenshot.dir", "target/screenshots"); }
    public String getReportDir()        { return get("report.dir", "target/reports"); }
    public boolean isRemote()           { return getBoolean("remote.execution", false); }
    public String getRemoteUrl()        { return get("remote.url", "http://localhost:4444/wd/hub"); }
    public String getApiBaseUrl()       { return get("api.base.url", ""); }
}
