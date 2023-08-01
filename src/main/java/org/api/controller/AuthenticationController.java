package org.api.controller;

import org.api.annotation.LogExecutionTime;
import org.api.constants.ConstantMessage;
import org.api.constants.ConstantStatus;
import org.api.payload.ResultBean;
import org.api.services.AuthenticationService;
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
@RequestMapping(value = "/v1/api/auth/")
public class AuthenticationController {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping(value = "/login", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<ResultBean> login(@RequestBody String json){
        try{
            ResultBean resultBean = authenticationService.loginAuth(json);
            return new ResponseEntity<ResultBean>(resultBean, HttpStatus.CREATED);
        }catch (ApiValidateException ex){
            ex.printStackTrace();
            return new ResponseEntity<ResultBean>(new ResultBean(ex.getCode(), ex.getMessage()), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<ResultBean>(new ResultBean(ConstantStatus.STATUS_BAD_REQUEST,ConstantMessage.MESSAGE_SYSTEM_ERROR), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/register", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<ResultBean> register(@RequestBody String json) {
        try{
            ResultBean resultBean = authenticationService.registerAuth(json);
            return new ResponseEntity<ResultBean>(resultBean, HttpStatus.CREATED);
        }catch (ApiValidateException ex){
            return new ResponseEntity<ResultBean>(new ResultBean(ex.getCode(), ex.getMessage()), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<ResultBean>(new ResultBean(ConstantStatus.STATUS_BAD_REQUEST,ConstantMessage.MESSAGE_SYSTEM_ERROR), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/forgot-password", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<ResultBean> forgotPassword(@RequestParam(name = "mail") String mail) {
        try{
            ResultBean resultBean = authenticationService.forgotPasswordAuth(mail);
            return new ResponseEntity<ResultBean>(resultBean, HttpStatus.CREATED);
        }catch (ApiValidateException ex){
            return new ResponseEntity<ResultBean>(new ResultBean(ex.getCode(), ex.getMessage()), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<ResultBean>(new ResultBean(ConstantStatus.STATUS_BAD_REQUEST,ConstantMessage.MESSAGE_SYSTEM_ERROR), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/check-code", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<ResultBean> checkCode(@RequestBody String json) {
        try{
            ResultBean resultBean = authenticationService.checkCode(json);
            return new ResponseEntity<ResultBean>(resultBean, HttpStatus.CREATED);
        }catch (ApiValidateException ex){
            return new ResponseEntity<ResultBean>(new ResultBean(ex.getCode(), ex.getMessage()), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<ResultBean>(new ResultBean(ConstantStatus.STATUS_BAD_REQUEST,ConstantMessage.MESSAGE_SYSTEM_ERROR), HttpStatus.OK);
        }
    }


}
