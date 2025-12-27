package com.example.networktest;

import android.content.Context;
import java.io.IOException;
import java.util.Properties;

/**
 * 增强的配置管理类 - 从 local.properties 或环境变量读取敏感信息
 * 这样可以避免在代码中硬编码密钥
 */
public class ConfigManager {
    
    private static final String TAG = "ConfigManager";
    private static Properties properties = null;
    
    /**
     * 从 assets 中的 local.properties 加载配置（生产环境）
     * 或从系统环境变量读取（开发环境）
     */
    public static void init(Context context) {
        properties = new Properties();
        try {
            // 尝试从 assets 加载
            properties.load(context.getAssets().open("local.properties"));
        } catch (IOException e) {
            // 如果找不到文件，将在 getProperty 时使用环境变量
            e.printStackTrace();
        }
    }
    
    /**
     * 获取属性值，优先从 properties 文件，其次从环境变量
     */
    private static String getProperty(String key, String defaultValue) {
        if (properties != null && properties.containsKey(key)) {
            return properties.getProperty(key);
        }
        String envValue = System.getenv(key);
        return envValue != null ? envValue : defaultValue;
    }
    
    /**
     * 从环境变量获取 API 密钥
     */
    public static String getApiKey() {
        return getProperty("API_KEY", "default_api_key");
    }
    
    /**
     * 从环境变量获取 Secret Key
     */
    public static String getSecretKey() {
        return getProperty("SECRET_KEY", "default_secret_key");
    }
    
    /**
     * 从环境变量获取教师 URL
     */
    public static String getTeacherUrl() {
        return getProperty("TEACHER_URL", "https://www.httpbin.org/image/png");
    }
    
    /**
     * 从环境变量获取文本 URL
     */
    public static String getTextUrl() {
        return getProperty("TEXT_URL", "https://www.baidu.com");
    }
    
    /**
     * 从环境变量获取图片 URL
     */
    public static String getImageUrl() {
        return getProperty("IMAGE_URL", "https://www.baidu.com/img/flexible/logo/pc/result.png");
    }
    
    /**
     * 从环境变量获取调试模式
     */
    public static boolean isDebugMode() {
        String debug = getProperty("DEBUG", "false");
        return debug.equals("true");
    }
}
