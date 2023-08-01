package org.api.enumeration;

public enum SortEnum {

    DESC("desc"),
    ASC("asc");

	SortEnum(String text) {
        this.text = text;
    }
    public String getText() {
        return text;
    }

    private String text;
}