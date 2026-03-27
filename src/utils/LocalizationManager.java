package utils;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationManager {
    private Locale locale;
    private ResourceBundle bundle;

    public LocalizationManager(Locale locale) {
        setLocale(locale);
    }

    public String getString(String key) {
        return bundle.getString(key);
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
        bundle = ResourceBundle.getBundle("assets.messages", locale);
    }

    public Locale getLocale() {
        return locale;
    }
}
