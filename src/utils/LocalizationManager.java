package utils;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationManager {
    private ResourceBundle bundle;
    public LocalizationManager(Locale locale) {
        bundle = ResourceBundle.getBundle("messages", locale);  // 加载资源文件
    }

    // 获取指定键的本地化字符串
    public String getString(String key) {
        return bundle.getString(key);
    }

    // 设置新语言环境
    public void setLocale(Locale locale) {
        bundle = ResourceBundle.getBundle("messages", locale);
    }
}
