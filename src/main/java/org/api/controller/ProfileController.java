package org.api.controller;

import org.api.annotation.LogExecutionTime;
import org.api.constants.ConstantMessage;
import org.api.constants.ConstantStatus;
import org.api.constants.ConstantTypeAlbum;
import org.api.payload.ResultBean;
import org.api.services.FileEntityService;
import org.api.services.PostEntityService;
import org.api.utils.ApiValidateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@LogExecutionTime
@RestController
@RequestMapping(value = "/v1/api/profiles/")
public class ProfileController {

    private static final Logger log = LoggerFactory.getLogger(ProfileController.class);

    @Autowired
    private PostEntityService postEntityService;

    @Autowired
    private FileEntityService fileEntityService;

    @PostMapping(value = "/create-avatar", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResultBean> createAvatar(@RequestPart("json") String json, @RequestPart("file") MultipartFile file) {
        try {
            ResultBean resultBean = postEntityService.createAvatar(json, file);
            return new ResponseEntity<ResultBean>(resultBean, HttpStatus.CREATED);
        } catch (ApiValidateException ex) {
            return new ResponseEntity<ResultBean>(new ResultBean(ex.getCode(), ex.getMessage()), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<ResultBean>(new ResultBean(ConstantStatus.STATUS_BAD_REQUEST, ConstantMessage.MESSAGE_SYSTEM_ERROR), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/create-banner", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResultBean> createBanner(@RequestPart("json") String json, @RequestPart("file") MultipartFile file) {
        try {
            ResultBean resultBean = postEntityService.createBanner(json, file);
            return new ResponseEntity<ResultBean>(resultBean, HttpStatus.CREATED);
        } catch (ApiValidateException ex) {
            return new ResponseEntity<ResultBean>(new ResultBean(ex.getCode(), ex.getMessage()), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<ResultBean>(new ResultBean(ConstantStatus.STATUS_BAD_REQUEST, ConstantMessage.MESSAGE_SYSTEM_ERROR), HttpStatus.OK);
        }
    }

    @GetMapping(value = {"/avatar/{idUser}", "/banner/{idUser}", "/post/{idUser}"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResultBean> findAllByUserAndTypeAlbum(@PathVariable String idUser, HttpServletRequest request) {
        try {
            ResultBean resultBean = null;
            if (request.getRequestURI().contains("/avatar/")) {
                resultBean = fileEntityService.findAllByUserAndTypeAlbum(idUser, ConstantTypeAlbum.AVATAR);
            } else if (request.getRequestURI().contains("/banner/")) {
                resultBean = fileEntityService.findAllByUserAndTypeAlbum(idUser, ConstantTypeAlbum.BANNER);
            } else if (request.getRequestURI().contains("/post/")) {
                resultBean = fileEntityService.findAllByUserAndTypeAlbum(idUser, ConstantTypeAlbum.POSTS);
            }
            return new ResponseEntity<ResultBean>(resultBean, HttpStatus.CREATED);
        } catch (ApiValidateException ex) {
            return new ResponseEntity<ResultBean>(new ResultBean(ex.getCode(), ex.getMessage()), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<ResultBean>(new ResultBean(ConstantStatus.STATUS_OK, ConstantMessage.MESSAGE_SYSTEM_ERROR), HttpStatus.OK);
        }
    }
}
