package org.api.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

public class DataUtil {

    private static final SimpleDateFormat YYYYMMDD_FORMAT = new SimpleDateFormat("yyyyMMdd");

    public static boolean isEmpty(String value) {
        if (null == value || 0 == value.length()) {
            return true;
        }
        return false;
    }

    public static boolean isEmpty(List value) {
        if (value == null || value.isEmpty()) {
            return true;
        }
        return false;
    }

    public static String getJsonString(JsonObject json, String memberName) {
        return getJsonString(json, memberName, null);
    }

    public static String getJsonString(JsonObject json, String memberName, String defaultValue) {
        if (!json.has(memberName) || json.get(memberName).isJsonNull()) {
            return defaultValue;
        }
        return json.get(memberName).getAsString().trim();
    }

    public static boolean hasMember(JsonObject json, String memberName) {
        return json.has(memberName);
    }

    public static boolean checkMinLength(String value, int minlength) {
        if (DataUtil.isEmpty(value) || value.length() < minlength) {
            return false;
        }
        return true;
    }

    public static boolean checkMaxLength(String value, int maxlength) {
        if (DataUtil.isEmpty(value) || value.length() > maxlength) {
            return false;
        }
        return true;
    }

    public static boolean checkPattern(String value, String sPattern) {
        if (DataUtil.isEmpty(value)) {
            return true;
        }
        Pattern pattern = Pattern.compile(sPattern);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    public static boolean checkDecimal(String value, String pattern) {
        if (DataUtil.isEmpty(value)) {
            return true;
        }
        return value.matches(pattern);
    }

    public static boolean isEmailValid(String email) {
        String emailRegex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern pat = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public static boolean checkNumberPhone(String number) {
        if (isEmpty(number)) {
            return true;
        }
        Pattern pattern = Pattern.compile("^[0-9.-]{0,11}$");
        return pattern.matcher(number).matches();
    }

    public static boolean isDate(String value, String sPattern) {
        if (isEmpty(value)) {
            return true;
        }
        SimpleDateFormat format = new SimpleDateFormat(sPattern);
        format.setLenient(false);
        try {
            format.parse(value);
        } catch (ParseException ex) {
            return false;
        }
        return true;
    }

    public static String dateToString(Date date) {
        if (date == null) {
            return null;
        }

        try {
            return YYYYMMDD_FORMAT.format(date);
        } catch (Exception e) {
            return null;
        }
    }

    public static String dateToString(Date date, String patttern) {
        if (date == null) {
            return null;
        }

        try {
            SimpleDateFormat format = new SimpleDateFormat(patttern);
            return format.format(date);
        } catch (Exception e) {
            return null;
        }
    }

    public static Integer getJsonInteger(JsonObject json, String memberName) {
        return getJsonInteger(json, memberName, null);
    }

    public static Long getJsonLong(JsonObject json, String memberName) {
        return getJsonLong(json, memberName, null);
    }

    public static Integer getJsonInteger(JsonObject json, String memberName, Integer defaultValue) {
        if (!json.has(memberName) || json.get(memberName).isJsonNull()) {
            return defaultValue;
        }
        try {
            return json.get(memberName).getAsInt();
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Long getJsonLong(JsonObject json, String memberName, Long defaultValue) {
        if (!json.has(memberName) || json.get(memberName).isJsonNull()) {
            return defaultValue;
        }
        try {
            return json.get(memberName).getAsLong();
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static BigDecimal getJsonBigDecimal(JsonObject json, String memberName) throws ApiValidateException {
        if (!json.has(memberName) || json.get(memberName).isJsonNull()) {
            return null;
        }
        try {
            return json.get(memberName).getAsBigDecimal();
        } catch (Exception e) {
            throw new ApiValidateException("", "BigDecimal format no correct!");
        }
    }

    public static JsonObject getJsonObject(String strJson) throws ApiValidateException {
        JsonObject json = null;
        try {
            if (null != strJson && 0 != strJson.length()) {
                json = new Gson().fromJson(strJson, JsonObject.class);
            }
            if (json != null && hasMember(json, "data")) {
                json = json.getAsJsonObject("data");
            }
        } catch (Exception ex) {
            json = null;
        }
        if (null == json) {
            throw new ApiValidateException(null, null, "Incorrect JSON format.");
        }
        return json;
    }

    public static JsonObject getJsonObjectWithMember(JsonObject json, String memberName) {
        try {
            if (json != null && hasMember(json, memberName)) {
                json = json.getAsJsonObject(memberName);
                return json;
            }
        } catch (Exception e) {
            //
        }
        return null;
    }

    public static boolean isEmptyImage(MultipartFile file) {
        if (file.isEmpty() || file.getSize() == 0) {
            return true;
        }
        return false;
    }

    public static boolean isLengthImage(MultipartFile[] files) {
        if (files.length == 0) {
            return true;
        }
        return false;
    }
}
