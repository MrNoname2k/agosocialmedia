package org.api.controller;

import org.api.annotation.LogExecutionTime;
import org.api.constants.ConstantMessage;
import org.api.constants.ConstantStatus;
import org.api.payload.ResultBean;
import org.api.services.UserEntityService;
import org.api.utils.ApiValidateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@LogExecutionTime
@RestController
@RequestMapping(value = "/v1/api/users/")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserEntityService userEntityService;

    @PostMapping(value = "/create-user", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResultBean> createUser(@RequestBody String json) {
        try {
            ResultBean resultBean = userEntityService.createUser(json);
            return new ResponseEntity<ResultBean>(resultBean, HttpStatus.CREATED);
        } catch (ApiValidateException ex) {
            return new ResponseEntity<ResultBean>(new ResultBean(ex.getCode(), ex.getMessage()), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<ResultBean>(new ResultBean(ConstantStatus.STATUS_BAD_REQUEST, ConstantMessage.MESSAGE_SYSTEM_ERROR), HttpStatus.OK);
        }
    }

    @PutMapping(value = "/update-user", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResultBean> updateUser(@RequestBody String json) {
        try {
            ResultBean resultBean = userEntityService.updateUser(json);
            return new ResponseEntity<ResultBean>(resultBean, HttpStatus.CREATED);
        } catch (ApiValidateException ex) {
            ex.printStackTrace();
            return new ResponseEntity<ResultBean>(new ResultBean(ex.getCode(), ex.getMessage()), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<ResultBean>(new ResultBean(ConstantStatus.STATUS_BAD_REQUEST, ConstantMessage.MESSAGE_SYSTEM_ERROR), HttpStatus.OK);
        }
    }

    @GetMapping(value = "{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResultBean> getUserById(@PathVariable String id) {
        try {
            ResultBean resultBean = userEntityService.getById(id);
            return new ResponseEntity<ResultBean>(resultBean, HttpStatus.OK);
        } catch (ApiValidateException ex) {
            return new ResponseEntity<ResultBean>(new ResultBean(ex.getCode(), ex.getMessage()), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<ResultBean>(new ResultBean(ConstantStatus.STATUS_BAD_REQUEST, ConstantMessage.MESSAGE_SYSTEM_ERROR), HttpStatus.OK);
        }
    }
}
