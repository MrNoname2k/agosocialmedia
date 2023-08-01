package org.api.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.api.payload.request.ItemValidate;
import org.api.payload.request.ValidateInfo;
import org.api.constants.ConstantDate;
import org.api.constants.ConstantMessage;
import org.api.constants.ConstantProperite;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class ValidateData {

    public static final String REQUIRED_TYPE_ALL = "0";

    public static final String REQUIRED_TYPE_ADD = "1";

    public static final String REQUIRED_TYPE_UPDATE = "2";

    public static final String REQUIRED_TYPE_HASH_MEMBER = "3";

    public static final String TYPE_DATE = "DATE";

    public static final String TYPE_INT = "INT";

    public static final String TYPE_BIG_DECIMAL = "BIGDECIMAL";

    public static final String PARAM_SYSDATE = "{sysdate}";

    public static final String PATH_FILE_JSON = "path_file_json";

    private static ResourceBundle resouceApp;

    public static void validate(String fileName, JsonObject json, boolean isUpdate) throws Exception {
        ItemValidate validate = getItemValidate(fileName);
        if (Objects.isNull(validate)) {
            throw new Exception(MessageUtils.getMessage(ConstantMessage.ID_BKE00010, new Object[] { fileName }));
        }
        String alias = validate.getAlias();
        List<ValidateInfo> checks = validate.getChecks();
        if (DataUtil.isEmpty(checks)) {
            return;
        }
        for (ValidateInfo check : checks) {
            checks = validate.getChecks();
            check(alias, json, check, isUpdate);
        }
    }

    public static void validate(String fileName, JsonObject json, boolean isUpdate, String locate) throws Exception {
        ItemValidate validate = getItemValidate(fileName, locate);
        if (Objects.isNull(validate)) {
            throw new Exception(MessageUtils.getMessage(ConstantMessage.ID_BKE00010, locate, new Object[] { fileName }));
        }
        String alias = validate.getAlias();
        List<ValidateInfo> checks = validate.getChecks();
        if (DataUtil.isEmpty(checks)) {
            return;
        }
        for (ValidateInfo check : checks) {
            checks = validate.getChecks();
            check(alias, json, check, isUpdate, locate);
        }
    }

    public static ItemValidate getItemValidate(String fileName, String locate) {
        BufferedReader red = null;
        if (resouceApp == null) {
            resouceApp = ResourceBundle.getBundle(ConstantProperite.PROPERTIE_APP);
        }
        try {
            red = new BufferedReader(new FileReader(resouceApp.getString(PATH_FILE_JSON) + fileName));
            return new Gson().fromJson(red, ItemValidate.class);
        } catch (FileNotFoundException e) {
            return null;
        } finally {
            if (Objects.nonNull(red)) {
                try {
                    red.close();
                } catch (IOException e) {
                    //
                }
            }
        }
    }

    public static ItemValidate getItemValidate(String fileName) {
        return getItemValidate(fileName, null);

    }

    public static void check(String alias, JsonObject json, ValidateInfo check, boolean isUpdate) throws ApiValidateException {
        check(alias, json, check, isUpdate, null);
    }

    public static void check(String alias, JsonObject json, ValidateInfo check, boolean isUpdate, String locate) throws ApiValidateException {
        String id = check.getId();
        String value = DataUtil.getJsonString(json, id);
        String name = ItemNameUtils.getItemName(id, locate, alias);
        String required = check.getRequired();
        boolean hasMember = DataUtil.hasMember(json, id);
        String formatDate = ConstantDate.DATE_PATTEN;
        if (isUpdate && !hasMember && !REQUIRED_TYPE_HASH_MEMBER.equals(required)) {
            return;
        }
        if (DataUtil.isEmpty(value) && ((REQUIRED_TYPE_ALL.equals(required)) || (REQUIRED_TYPE_ADD.equals(required) && !isUpdate)
                || (REQUIRED_TYPE_UPDATE.equals(required) && isUpdate && hasMember) || REQUIRED_TYPE_HASH_MEMBER.equals(required) && isUpdate)) {
            throw new ApiValidateException(ConstantMessage.ID_BKE00004, id, MessageUtils.getMessage(ConstantMessage.ID_BKE00004, locate, new Object[] { name }));
        }
        if (DataUtil.isEmpty(value)) {
            return;
        }
        if (check.getMinLength() > -1) {
            if (!DataUtil.checkMinLength(value, check.getMinLength())) {
                if (check.getMaxLength() > 0) {
                    throw new ApiValidateException(ConstantMessage.ID_BKE00007, id,
                            MessageUtils.getMessage(ConstantMessage.ID_BKE00007, locate, new Object[] { name, check.getMinLength(), check.getMaxLength() }));
                }
                throw new ApiValidateException(ConstantMessage.ID_BKE00005, id,
                        MessageUtils.getMessage(ConstantMessage.ID_BKE00005, locate, new Object[] { name, check.getMaxLength() }));
            }
        }

        if (check.getMaxLength() > 0) {
            if (!DataUtil.checkMaxLength(value, check.getMaxLength())) {
                if (check.getMinLength() > -1) {
                    throw new ApiValidateException(ConstantMessage.ID_BKE00007, id,
                            MessageUtils.getMessage(ConstantMessage.ID_BKE00007, locate, new Object[] { name, check.getMinLength(), check.getMaxLength() }));
                }
                throw new ApiValidateException(ConstantMessage.ID_BKE00006, id,
                        MessageUtils.getMessage(ConstantMessage.ID_BKE00006, locate, new Object[] { name, check.getMaxLength() }));
            }
        }

        if (!DataUtil.isEmpty(check.getNumber())) {
            if (!DataUtil.checkPattern(value, check.getNumber())) {
                throw new ApiValidateException(ConstantMessage.ID_BKE00008, id, MessageUtils.getMessage(ConstantMessage.ID_BKE00008, locate, new Object[] { name }));
            }
        }

        if (!DataUtil.isEmpty(check.getDecimal())) {
            if (!DataUtil.checkDecimal(value, check.getDecimal())) {
                throw new ApiValidateException(ConstantMessage.ID_BKE00008, id, MessageUtils.getMessage(ConstantMessage.ID_BKE00008, locate, new Object[] { name }));
            }
        }
        if (check.isEmail()) {
            if (!DataUtil.isEmailValid(value)) {
                throw new ApiValidateException(ConstantMessage.ID_BKE00009, id, MessageUtils.getMessage(ConstantMessage.ID_BKE00009, locate, new Object[] { name }));
            }
        }
        if (check.isPhoneNumber()) {
            if (!DataUtil.checkNumberPhone(value)) {
                throw new ApiValidateException(ConstantMessage.ID_BKE00009, id, MessageUtils.getMessage(ConstantMessage.ID_BKE00009, locate, new Object[] { name }));
            }
        }
        if (check.isDate()) {
            if (!DataUtil.isDate(value, ConstantDate.DATE_PATTEN)) {
                throw new ApiValidateException(ConstantMessage.ID_BKE00009, id, MessageUtils.getMessage(ConstantMessage.ID_BKE00009, locate, new Object[] { name }));
            }
        }

        if (check.isDate()) {
            if (!DataUtil.isDate(value, ConstantDate.DATE_PATTEN)) {
                throw new ApiValidateException(ConstantMessage.ID_BKE00009, id, MessageUtils.getMessage(ConstantMessage.ID_BKE00009, locate, new Object[] { name }));
            }
        }

        if (check.isDateTime()) {
            if (!DataUtil.isDate(value, ConstantDate.DATE_TIME_PATTEN)) {
                throw new ApiValidateException(ConstantMessage.ID_BKE00009, id, MessageUtils.getMessage(ConstantMessage.ID_BKE00009, locate, new Object[] { name }));
            }
            formatDate = ConstantDate.DATE_TIME_PATTEN;
        }

        if (check.isDateFull()) {
            if (!DataUtil.isDate(value, ConstantDate.DATE_PATTERN_FULL)) {
                throw new ApiValidateException(ConstantMessage.ID_BKE00009, id, MessageUtils.getMessage(ConstantMessage.ID_BKE00009, locate, new Object[] { name }));
            }
            formatDate = ConstantDate.DATE_PATTERN_FULL;
        }

        if (!DataUtil.isEmpty(check.getPattern())) {
            if (!DataUtil.checkPattern(value, check.getPattern())) {
                throw new ApiValidateException(ConstantMessage.ID_BKE00009, id, MessageUtils.getMessage(ConstantMessage.ID_BKE00009, locate, new Object[] { name }));
            }
        }

        if (!DataUtil.isEmpty(check.getRange())) {
            String range = check.getRange();
            if (range.indexOf(value + ";") < 0) {
                throw new ApiValidateException(ConstantMessage.ID_BKE00017, id, MessageUtils.getMessage(ConstantMessage.ID_BKE00017, locate, new Object[] { name }));
            }
        }
        String type = check.getType();
        if (!TYPE_DATE.equalsIgnoreCase(type) && !TYPE_INT.equalsIgnoreCase(type) && !TYPE_BIG_DECIMAL.equalsIgnoreCase(type)) {
            return;
        }
        if (!DataUtil.isEmpty(check.getMin())) {
            String min = check.getMin();
            if (TYPE_DATE.compareToIgnoreCase(check.getType()) == 0) {
                if (PARAM_SYSDATE.compareToIgnoreCase(min) == 0) {
                    String date = DataUtil.dateToString(new Date(), formatDate);
                    if (value.compareTo(date) <= 0) {
                        throw new ApiValidateException(ConstantMessage.ID_BKE00011, id,
                                MessageUtils.getMessage(ConstantMessage.ID_BKE00011, locate, new Object[] { name }));
                    }
                } else if (min.startsWith("{")) {
                    String idCompare = min.replace("{", "").replace("}", "").trim();
                    String valueCompare = DataUtil.getJsonString(json, idCompare);
                    if (DataUtil.isDate(valueCompare, formatDate) && value.compareTo(valueCompare) <= 0) {
                        throw new ApiValidateException(ConstantMessage.ID_BKE00028, id, MessageUtils.getMessage(ConstantMessage.ID_BKE00028, locate,
                                new Object[] { name, ItemNameUtils.getItemName(idCompare, locate, alias) }));
                    }
                } else {
                    if (value.compareTo(min) <= 0) {
                        throw new ApiValidateException(ConstantMessage.ID_BKE00028, id,
                                MessageUtils.getMessage(ConstantMessage.ID_BKE00028, locate, new Object[] { name, min }));
                    }
                }
            } else if (TYPE_INT.compareToIgnoreCase(check.getType()) == 0) {
                int intValue = Integer.valueOf(value);
                if (min.startsWith("{")) {
                    String idCompare = min.replace("{", "").replace("}", "").trim();
                    Integer valueCompare = DataUtil.getJsonInteger(json, idCompare);
                    if (intValue <= valueCompare) {
                        throw new ApiValidateException(ConstantMessage.ID_BKE00015, id, MessageUtils.getMessage(ConstantMessage.ID_BKE00015, locate,
                                new Object[] { name, ItemNameUtils.getItemName(idCompare, locate, alias) }));
                    }
                } else {
                    int intValueCompare = Integer.valueOf(min);
                    if (intValue <= intValueCompare) {
                        throw new ApiValidateException(ConstantMessage.ID_BKE00015, id,
                                MessageUtils.getMessage(ConstantMessage.ID_BKE00015, locate, new Object[] { name, min }));
                    }
                }
            } else if (TYPE_BIG_DECIMAL.compareToIgnoreCase(check.getType()) == 0) {
                BigDecimal bigValue = new BigDecimal(value);
                if (min.startsWith("{")) {
                    String idCompare = min.replace("{", "").replace("}", "").trim();
                    BigDecimal valueCompare = DataUtil.getJsonBigDecimal(json, idCompare);
                    if (bigValue.compareTo(valueCompare) <= 0) {
                        throw new ApiValidateException(ConstantMessage.ID_BKE00015, id, MessageUtils.getMessage(ConstantMessage.ID_BKE00015, locate,
                                new Object[] { name, ItemNameUtils.getItemName(idCompare, locate, alias) }));
                    }
                } else {
                    BigDecimal valueCompare = new BigDecimal(min);
                    if (bigValue.compareTo(valueCompare) <= 0) {
                        throw new ApiValidateException(ConstantMessage.ID_BKE00015, id,
                                MessageUtils.getMessage(ConstantMessage.ID_BKE00015, locate, new Object[] { name, min }));
                    }
                }
            }
        }
        if (!DataUtil.isEmpty(check.getMinEqual())) {
            String min = check.getMinEqual();
            if (TYPE_DATE.equalsIgnoreCase(check.getType())) {
                if (PARAM_SYSDATE.compareToIgnoreCase(min) == 0) {
                    String date = DataUtil.dateToString(new Date(), formatDate);
                    if (value.compareTo(date) < 0) {
                        throw new ApiValidateException(ConstantMessage.ID_BKE00012, id,
                                MessageUtils.getMessage(ConstantMessage.ID_BKE00012, locate, new Object[] { name }));
                    }
                } else if (min.startsWith("{")) {
                    String idCompare = min.replace("{", "").replace("}", "").trim();
                    String valueCompare = DataUtil.getJsonString(json, idCompare);
                    if (DataUtil.isDate(valueCompare, formatDate) && value.compareTo(valueCompare) < 0) {
                        throw new ApiValidateException(ConstantMessage.ID_BKE00029, id, MessageUtils.getMessage(ConstantMessage.ID_BKE00029, locate,
                                new Object[] { name, ItemNameUtils.getItemName(idCompare, locate, alias) }));
                    }
                } else {
                    if (value.compareTo(min) <= 0) {
                        throw new ApiValidateException(ConstantMessage.ID_BKE00029, id,
                                MessageUtils.getMessage(ConstantMessage.ID_BKE00029, locate, new Object[] { name, min }));
                    }
                }
            } else if (TYPE_INT.compareToIgnoreCase(check.getType()) == 0) {
                int intValue = Integer.valueOf(value);
                if (min.startsWith("{")) {
                    String idCompare = min.replace("{", "").replace("}", "").trim();
                    Integer valueCompare = DataUtil.getJsonInteger(json, idCompare);
                    if (intValue < valueCompare) {
                        throw new ApiValidateException(ConstantMessage.ID_BKE00016, id, MessageUtils.getMessage(ConstantMessage.ID_BKE00016, locate,
                                new Object[] { name, ItemNameUtils.getItemName(idCompare, locate, alias) }));
                    }
                } else {
                    int intValueCompare = Integer.valueOf(min);
                    if (intValue < intValueCompare) {
                        throw new ApiValidateException(ConstantMessage.ID_BKE00016, id,
                                MessageUtils.getMessage(ConstantMessage.ID_BKE00016, locate, new Object[] { name, min }));
                    }
                }
            } else if (TYPE_BIG_DECIMAL.compareToIgnoreCase(check.getType()) == 0) {
                BigDecimal bigValue = new BigDecimal(value);
                if (min.startsWith("{")) {
                    String idCompare = min.replace("{", "").replace("}", "").trim();
                    BigDecimal valueCompare = DataUtil.getJsonBigDecimal(json, idCompare);
                    if (bigValue.compareTo(valueCompare) < 0) {
                        throw new ApiValidateException(ConstantMessage.ID_BKE00016, id, MessageUtils.getMessage(ConstantMessage.ID_BKE00016, locate,
                                new Object[] { name, ItemNameUtils.getItemName(idCompare, locate, alias) }));
                    }
                } else {
                    BigDecimal valueCompare = new BigDecimal(min);
                    if (bigValue.compareTo(valueCompare) < 0) {
                        throw new ApiValidateException(ConstantMessage.ID_BKE00016, id,
                                MessageUtils.getMessage(ConstantMessage.ID_BKE00016, locate, new Object[] { name, min }));
                    }
                }
            }
        }

        if (!DataUtil.isEmpty(check.getMax())) {
            String max = check.getMax();
            if (TYPE_DATE.compareToIgnoreCase(check.getType()) == 0) {
                if (PARAM_SYSDATE.compareToIgnoreCase(max) == 0) {
                    String date = DataUtil.dateToString(new Date(), formatDate);
                    if (value.compareTo(date) >= 0) {
                        throw new ApiValidateException(ConstantMessage.ID_BKE00013, id,
                                MessageUtils.getMessage(ConstantMessage.ID_BKE00013, locate, new Object[] { name }));
                    }
                } else if (max.startsWith("{")) {
                    String idCompare = max.replace("{", "").replace("}", "").trim();
                    String valueCompare = DataUtil.getJsonString(json, idCompare);
                    if (DataUtil.isDate(valueCompare, formatDate) && value.compareTo(valueCompare) >= 0) {
                        throw new ApiValidateException(ConstantMessage.ID_BKE00030, id, MessageUtils.getMessage(ConstantMessage.ID_BKE00030, locate,
                                new Object[] { name, ItemNameUtils.getItemName(idCompare, locate, alias) }));
                    }
                } else {
                    if (value.compareTo(max) >= 0) {
                        throw new ApiValidateException(ConstantMessage.ID_BKE00030, id,
                                MessageUtils.getMessage(ConstantMessage.ID_BKE00030, locate, new Object[] { name, max }));
                    }
                }
            } else if (TYPE_INT.compareToIgnoreCase(check.getType()) == 0) {
                int intValue = Integer.valueOf(value);
                if (max.startsWith("{")) {
                    String idCompare = max.replace("{", "").replace("}", "").trim();
                    Integer valueCompare = DataUtil.getJsonInteger(json, idCompare);
                    if (intValue >= valueCompare) {
                        throw new ApiValidateException(ConstantMessage.ID_BKE00032, id, MessageUtils.getMessage(ConstantMessage.ID_BKE00032, locate,
                                new Object[] { name, ItemNameUtils.getItemName(idCompare, locate, alias) }));
                    }
                } else {
                    int intValueCompare = Integer.valueOf(max);
                    if (intValue >= intValueCompare) {
                        throw new ApiValidateException(ConstantMessage.ID_BKE00032, id,
                                MessageUtils.getMessage(ConstantMessage.ID_BKE00032, locate, new Object[] { name, max }));
                    }
                }
            } else if (TYPE_BIG_DECIMAL.compareToIgnoreCase(check.getType()) == 0) {
                BigDecimal bigValue = new BigDecimal(value);
                if (max.startsWith("{")) {
                    String idCompare = max.replace("{", "").replace("}", "").trim();
                    BigDecimal valueCompare = DataUtil.getJsonBigDecimal(json, idCompare);
                    if (bigValue.compareTo(valueCompare) >= 0) {
                        throw new ApiValidateException(ConstantMessage.ID_BKE00032, id, MessageUtils.getMessage(ConstantMessage.ID_BKE00032, locate,
                                new Object[] { name, ItemNameUtils.getItemName(idCompare, locate, alias) }));
                    }
                } else {
                    BigDecimal valueCompare = new BigDecimal(max);
                    if (bigValue.compareTo(valueCompare) >= 0) {
                        throw new ApiValidateException(ConstantMessage.ID_BKE00032, id,
                                MessageUtils.getMessage(ConstantMessage.ID_BKE00032, locate, new Object[] { name, max }));
                    }
                }
            }
        }
        if (!DataUtil.isEmpty(check.getMaxEqual())) {
            String max = check.getMaxEqual();
            if (TYPE_DATE.compareToIgnoreCase(check.getType()) == 0) {
                if (PARAM_SYSDATE.compareToIgnoreCase(max) == 0) {
                    String date = DataUtil.dateToString(new Date(), formatDate);
                    if (value.compareTo(date) > 0) {
                        throw new ApiValidateException(ConstantMessage.ID_BKE00014, id,
                                MessageUtils.getMessage(ConstantMessage.ID_BKE00014, locate, new Object[] { name }));
                    }
                } else if (max.startsWith("{")) {
                    String idCompare = max.replace("{", "").replace("}", "").trim();
                    String valueCompare = DataUtil.getJsonString(json, idCompare);
                    if (DataUtil.isDate(valueCompare, formatDate) && value.compareTo(valueCompare) > 0) {
                        throw new ApiValidateException(ConstantMessage.ID_BKE00031, id, MessageUtils.getMessage(ConstantMessage.ID_BKE00031, locate,
                                new Object[] { name, ItemNameUtils.getItemName(idCompare, locate, alias) }));
                    }
                } else {
                    if (value.compareTo(max) > 0) {
                        throw new ApiValidateException(ConstantMessage.ID_BKE00031, id,
                                MessageUtils.getMessage(ConstantMessage.ID_BKE00031, locate, new Object[] { name, max }));
                    }
                }
            } else if (TYPE_INT.compareToIgnoreCase(check.getType()) == 0) {
                int intValue = Integer.valueOf(value);
                if (max.startsWith("{")) {
                    String idCompare = max.replace("{", "").replace("}", "").trim();
                    Integer valueCompare = DataUtil.getJsonInteger(json, idCompare);
                    if (intValue > valueCompare) {
                        throw new ApiValidateException(ConstantMessage.ID_BKE00033, id, MessageUtils.getMessage(ConstantMessage.ID_BKE00033, locate,
                                new Object[] { name, ItemNameUtils.getItemName(idCompare, locate, alias) }));
                    }
                } else {
                    int intValueCompare = Integer.valueOf(max);
                    if (intValue > intValueCompare) {
                        throw new ApiValidateException(ConstantMessage.ID_BKE00033, id,
                                MessageUtils.getMessage(ConstantMessage.ID_BKE00033, locate, new Object[] { name, max }));
                    }
                }
            } else if (TYPE_BIG_DECIMAL.compareToIgnoreCase(check.getType()) == 0) {
                BigDecimal bigValue = new BigDecimal(value);
                if (max.startsWith("{")) {
                    String idCompare = max.replace("{", "").replace("}", "").trim();
                    BigDecimal valueCompare = DataUtil.getJsonBigDecimal(json, idCompare);
                    if (bigValue.compareTo(valueCompare) > 0) {
                        throw new ApiValidateException(ConstantMessage.ID_BKE00033, id, MessageUtils.getMessage(ConstantMessage.ID_BKE00033, locate,
                                new Object[] { name, ItemNameUtils.getItemName(idCompare, locate, alias) }));
                    }
                } else {
                    BigDecimal valueCompare = new BigDecimal(max);
                    if (bigValue.compareTo(valueCompare) > 0) {
                        throw new ApiValidateException(ConstantMessage.ID_BKE00033, id,
                                MessageUtils.getMessage(ConstantMessage.ID_BKE00033, locate, new Object[] { name, max }));
                    }
                }
            }
        }
    }

    public static void check(MultipartFile file) throws ApiValidateException {
        if (DataUtil.isEmptyImage(file)) {
            throw new ApiValidateException(ConstantMessage.ID_BKE00034, "file", MessageUtils.getMessage(ConstantMessage.ID_BKE00034));
        }
    }
}
