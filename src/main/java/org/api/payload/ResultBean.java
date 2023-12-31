package org.api.payload;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResultBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private MetaClass meta;
    private Object data;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object dataInfor;
    private List<ErrorBean> errors = new ArrayList<ErrorBean>();

    public ResultBean(Object data, String code, String message) {
        this.meta = new ResultBean.MetaClass(code, message);
        this.data = data;

    }
    public ResultBean(Object data, String code, String field ,String message) {
        this.meta = new ResultBean.MetaClass(code, field, message);
        this.data = data;

    }
    public ResultBean(String code, String message) {
        this.meta = new ResultBean.MetaClass(code, message);
    }
    public ResultBean(String code, String message, List<ErrorBean> errors) {
        this.meta = new ResultBean.MetaClass(code, message);
        this.errors = errors;
    }
    public ResultBean(String code, String field, String message) {
        this.meta = new ResultBean.MetaClass(code, field, message);
    }
    public MetaClass getMeta() {
        return meta;
    }
    public void setMeta(MetaClass meta) {
        this.meta = meta;
    }
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }
    public List<ErrorBean> getErrors() {
        return errors;
    }
    public void setErrors(List<ErrorBean> errors) {
        this.errors = errors;
    }
    public void addError(ErrorBean error) {
        this.errors.add(error);
    }
    public Object getDataInfor() {
        return dataInfor;
    }
    public void setDataInfor(Object dataInfor) {
        this.dataInfor = dataInfor;
    }

    public class MetaClass implements Serializable {

        private static final long serialVersionUID = 1L;

        private String code;
        private String field;
        private String message;

        public MetaClass(String code, String message) {
            this.code = code;
            this.message = message;
        }
        public MetaClass(String code, String field, String message) {
            this.code = code;
            this.message = message;
            this.field = field;
        }
        public String getCode() {
            return code;
        }
        public void setCode(String code) {
            this.code = code;
        }
        public String getMessage() {
            return message;
        }
        public void setMessage(String message) {
            this.message = message;
        }
        public String getField() {
            return field;
        }
        public void setField(String field) {
            this.field = field;
        }
    }
}