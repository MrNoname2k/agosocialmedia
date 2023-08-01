package org.api.controller;

import org.api.annotation.LogExecutionTime;
import org.api.constants.ConstantMessage;
import org.api.constants.ConstantRelationshipStatus;
import org.api.constants.ConstantStatus;
import org.api.payload.ResultBean;
import org.api.services.RelationshipEntityService;
import org.api.services.UserEntityService;
import org.api.utils.ApiValidateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@LogExecutionTime
@RestController
@RequestMapping(value = "/v1/api/relationships/")
public class RelationshipController {

    private static final Logger log = LoggerFactory.getLogger(RelationshipController.class);

    @Autowired
    private RelationshipEntityService relationshipEntityService;

    @Autowired
    private UserEntityService userEntityService;

    @PostMapping(value = {"/friend", "/unFriend"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResultBean> friendOrUnFriend(@RequestBody String json, HttpServletRequest request) {
        try {
            ResultBean resultBean = null;
            if (request.getRequestURI().contains("/friend")) {
                resultBean = relationshipEntityService.friendOrUnFriend(json, ConstantRelationshipStatus.FRIEND);
            } else if (request.getRequestURI().contains("/unFriend")) {
                resultBean = relationshipEntityService.friendOrUnFriend(json, ConstantRelationshipStatus.UN_FRIEND);
                ;
            }
            return new ResponseEntity<ResultBean>(resultBean, HttpStatus.CREATED);
        } catch (ApiValidateException ex) {
            return new ResponseEntity<ResultBean>(new ResultBean(ex.getCode(), ex.getMessage()), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<ResultBean>(new ResultBean(ConstantStatus.STATUS_BAD_REQUEST, ConstantMessage.MESSAGE_SYSTEM_ERROR), HttpStatus.OK);
        }
    }

    @GetMapping(value = {"/friend/{idUser}", "/unFriend/{idUser}"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResultBean> getAllByUserEntityOneIdAndStatusFriend(@PathVariable String idUser, HttpServletRequest request) {
        try {
            ResultBean resultBean = null;
            if (request.getRequestURI().contains("/friend/")) {
                resultBean = relationshipEntityService.findAllByUserEntityOneIdAndStatus(idUser, ConstantRelationshipStatus.FRIEND);
            } else if (request.getRequestURI().contains("/unFriend/")) {
                resultBean = relationshipEntityService.findAllByUserEntityOneIdAndStatus(idUser, ConstantRelationshipStatus.UN_FRIEND);
            }
            return new ResponseEntity<ResultBean>(resultBean, HttpStatus.CREATED);
        } catch (ApiValidateException ex) {
            return new ResponseEntity<ResultBean>(new ResultBean(ex.getCode(), ex.getMessage()), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<ResultBean>(new ResultBean(ConstantStatus.STATUS_BAD_REQUEST, ConstantMessage.MESSAGE_SYSTEM_ERROR), HttpStatus.OK);
        }
    }

    /*@GetMapping(value = {"/recommend-friends/{idUser}"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResultBean> recommendFriends(@PathVariable String idUser, HttpServletRequest request) {
        try {
            ResultBean resultBean = userEntityService.recommendFriends(idUser);
            return new ResponseEntity<ResultBean>(resultBean, HttpStatus.CREATED);
        } catch (ApiValidateException ex) {
            return new ResponseEntity<ResultBean>(new ResultBean(ex.getCode(), ex.getMessage()), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<ResultBean>(new ResultBean(ConstantStatus.STATUS_BAD_REQUEST, ConstantMessage.MESSAGE_SYSTEM_ERROR), HttpStatus.OK);
        }
    }*/

}
