/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.Internationalisation;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author Adam Bublavý
 */
public final class I18n {

    private final static String BUNDLE_PATH = "sk.stu.fiit.Internationalisation.bundle";

    private static ResourceBundle bundle
            = ResourceBundle.getBundle(BUNDLE_PATH, Locale.getDefault());

    private I18n() {
    }

    public static ResourceBundle getBundle() {
        return bundle;
    }

    public static void setLocale(Locale locale) {
        Locale.setDefault(locale);
        bundle = ResourceBundle.getBundle(BUNDLE_PATH, locale);
    }

    public static String getMessage(String key) {
        return bundle.getString(key);
    }

    public static String getMessage(Enum<?> enumValue) {
        return getMessage(enumValue.toString());
    }

}
