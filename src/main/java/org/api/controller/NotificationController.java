package org.api.controller;

import org.api.annotation.LogExecutionTime;
import org.api.constants.ConstantMessage;
import org.api.constants.ConstantStatus;
import org.api.payload.ResultBean;
import org.api.services.NotificationEntityService;
import org.api.utils.ApiValidateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@LogExecutionTime
@RestController
@RequestMapping("/v1/api/notification")
public class NotificationController {

    private static final Logger log = LoggerFactory.getLogger(NotificationController.class);

    @Autowired
    private NotificationEntityService notificationEntityService;

    @GetMapping(value = "/get-all", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResultBean> getAllNotificationByIdUser(@RequestParam(name = "size", required = false) Integer size,
                                                                 @RequestParam(name = "idUser", required = false) String idUser) {
        try {
            ResultBean resultBean = notificationEntityService.findAllByPostEntityUserEntityPostId(size, idUser);
            return new ResponseEntity<ResultBean>(resultBean, HttpStatus.CREATED);
        } catch (ApiValidateException ex) {
            return new ResponseEntity<ResultBean>(new ResultBean(ex.getCode(), ex.getMessage()), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<ResultBean>(new ResultBean(ConstantStatus.STATUS_BAD_REQUEST, ConstantMessage.MESSAGE_SYSTEM_ERROR), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/update-status", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResultBean> updateNotificationStatus() {
        try {
            return new ResponseEntity<ResultBean>(notificationEntityService.updateNotificationStatus(), HttpStatus.OK);

        } catch (ApiValidateException ex) {
            return new ResponseEntity<ResultBean>(new ResultBean(ex.getCode(), ex.getMessage()), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<ResultBean>(new ResultBean(ConstantStatus.STATUS_BAD_REQUEST, ConstantMessage.MESSAGE_SYSTEM_ERROR), HttpStatus.OK);
        }
    }
}

