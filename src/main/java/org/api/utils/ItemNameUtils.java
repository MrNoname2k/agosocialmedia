package org.api.utils;

import org.api.constants.ConstantProperite;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ItemNameUtils {

    private static final String COMMON_COLUMNS = "common_columns";

    private static String commonColumns;

    private static ResourceBundle rsItemNameEn;

    public static String getItemName(String key, String alias) {
        return getItemName(key, null, alias);
    }

    public static String getItemName(String key, String locate, String alias) {
        ResourceBundle rsItemName;
        if (rsItemNameEn == null) {
            Locale.setDefault(new Locale("en", "US"));
            rsItemNameEn = ResourceBundle.getBundle(ConstantProperite.PROPERTIE_ITEM_NAME);
            commonColumns = rsItemNameEn.getString(COMMON_COLUMNS);
        }
        rsItemName = rsItemNameEn;
        String message;
        try {
            if (DataUtil.isEmpty(alias) && commonColumns.contains(key + ";")) {
                message = rsItemName.getString(key);
            } else {
                message = rsItemName.getString(alias + "." + key);
            }
            if (DataUtil.isEmpty(message)) {
                message = key;
            }
        } catch (MissingResourceException e) {
            message = key;
        }
        return message;
    }
}
