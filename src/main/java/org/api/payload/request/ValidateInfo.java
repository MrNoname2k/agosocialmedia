package org.api.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ValidateInfo {
    private String id;
    private String type = null;
    //0: all, 1: add, 2: update
    private String required = null;

    private int minLength = -1;

    private int maxLength = 0;

    private String number = null;

    private boolean isKanji = false;

    private boolean isKana = false;

    private boolean isEmail = false;

    private boolean isPhoneNumber = false;

    private boolean isDate = false;

    private boolean isDateFull = false;

    private boolean isDateTime = false;

    private String min = null;

    private String minEqual = null;

    private String max = null;

    private String maxEqual = null;

    private String pattern = null;

    private String range = null;

    private String decimal = null;
}
