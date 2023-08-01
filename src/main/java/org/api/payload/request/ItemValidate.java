package org.api.payload.request;

import java.util.List;

public class ItemValidate {

    private String alias;

    private List<ValidateInfo> checks;

    public String getAlias() {
        return alias;
    }

    public List<ValidateInfo> getChecks() {
        return checks;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setChecks(List<ValidateInfo> checks) {
        this.checks = checks;
    }

}