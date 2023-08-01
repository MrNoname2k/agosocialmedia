package org.api.utils;

import org.api.constants.ConstantProperite;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MessageUtils {

    private static ResourceBundle rsMessagesEn;

    public static String getMessage(String key, String locate, Object... param) {
        ResourceBundle rsMessages;
        if (rsMessagesEn == null) {
            Locale.setDefault(new Locale("en", "US"));
            rsMessagesEn = ResourceBundle.getBundle(ConstantProperite.PROPERTIE_MESSAGE);
        }
        rsMessages = rsMessagesEn;
        String message;
        try {
            message = rsMessages.getString(key);
            if (DataUtil.isEmpty(message)) {
                return key;
            }
            if (param != null && param.length > 0) {
                message = MessageFormat.format(message, param);
            }
        } catch (MissingResourceException e) {
            message = key;
        }
        return message;
    }

    public static String getMessage(String key, Object... param) {
        return getMessage(key, null, param);
    }
}
