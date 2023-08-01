package org.api.configuration;

import org.api.constants.ConstantMessage;
import org.api.constants.ConstantStatus;
import org.api.payload.ResultBean;
import org.api.utils.ApiValidateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ResultBean> handleException(Exception ex) {
        log.error(ex.getMessage(), ex);
        ResultBean resultBean = new ResultBean(ConstantStatus.STATUS_SYSTEM_ERROR, ConstantMessage.MESSAGE_SYSTEM_ERROR);
        return new ResponseEntity<ResultBean>(resultBean, HttpStatus.OK);
    }

    @ExceptionHandler(ApiValidateException.class)
    protected ResponseEntity<ResultBean> handleApiValidateException(ApiValidateException ex) {
        log.error(ex.getMessage(), ex);
        ResultBean resultBean = new ResultBean(ex.getCode(), ex.getField(), ex.getMessage());
        return new ResponseEntity<ResultBean>(resultBean, HttpStatus.OK);
    }
}
