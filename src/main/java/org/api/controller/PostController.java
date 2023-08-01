package org.api.controller;

import org.api.annotation.LogExecutionTime;
import org.api.constants.ConstantMessage;
import org.api.constants.ConstantStatus;
import org.api.payload.ResultBean;
import org.api.payload.response.homePageResponses.PostHomePageResponse;
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

@LogExecutionTime
@RestController
@RequestMapping(value = "/v1/api/posts/")
public class PostController {

    private static final Logger log = LoggerFactory.getLogger(PostController.class);

    @Autowired
    private PostEntityService postEntityService;

    @PostMapping(value = "/create-post", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResultBean> createPost(@RequestPart("json") String json, @RequestPart("files") MultipartFile[] files) {
        try {
            ResultBean resultBean = postEntityService.createPost(json, files);
            return new ResponseEntity<ResultBean>(resultBean, HttpStatus.CREATED);
        } catch (ApiValidateException ex) {
            return new ResponseEntity<ResultBean>(new ResultBean(ex.getCode(), ex.getMessage()), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<ResultBean>(new ResultBean(ConstantStatus.STATUS_BAD_REQUEST, ConstantMessage.MESSAGE_SYSTEM_ERROR), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/create-avatar", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResultBean> createAvatar(@RequestPart("json") String json, @RequestPart("file") MultipartFile file) {
        try {
            ResultBean resultBean = postEntityService.createAvatar(json, file);
            return new ResponseEntity<ResultBean>(resultBean, HttpStatus.CREATED);
        } catch (ApiValidateException ex) {
            return new ResponseEntity<ResultBean>(new ResultBean(ex.getCode(), ex.getMessage()), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
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
            ex.printStackTrace();
            return new ResponseEntity<ResultBean>(new ResultBean(ConstantStatus.STATUS_BAD_REQUEST, ConstantMessage.MESSAGE_SYSTEM_ERROR), HttpStatus.OK);
        }
    }

    @PutMapping(value = "/update-avatar", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResultBean> updateAvatar(@RequestBody String json) {
        try {
            ResultBean resultBean = postEntityService.updateAvatar(json);
            return new ResponseEntity<ResultBean>(resultBean, HttpStatus.OK);
        }catch (ApiValidateException a) {
            return new ResponseEntity<ResultBean>(new ResultBean(a.getCode(), a.getMessage()), HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<ResultBean>(new ResultBean(ConstantStatus.STATUS_BAD_REQUEST, e.getMessage()), HttpStatus.OK);
        }
    }
    @PutMapping(value = "/update-banner", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResultBean> updateBanner(@RequestBody String json) {
        try {
            ResultBean resultBean = postEntityService.updateBanner(json);
            return new ResponseEntity<ResultBean>(resultBean, HttpStatus.OK);
        }catch (ApiValidateException a) {
            return new ResponseEntity<ResultBean>(new ResultBean(a.getCode(), a.getMessage()), HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<ResultBean>(new ResultBean(ConstantStatus.STATUS_BAD_REQUEST, e.getMessage()), HttpStatus.OK);
        }
    }


    @GetMapping(value = "allPostOfFriends/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResultBean> getAllPostOfFriend(@PathVariable("id") String id) {
        try {
            PostHomePageResponse response = postEntityService.findAllByUserEntityPostIdInPage(10, id);
            ResultBean resultBean = new ResultBean(response, ConstantStatus.STATUS_OK, ConstantMessage.MESSAGE_OK);
            return new ResponseEntity<ResultBean>(resultBean, HttpStatus.OK);
        } catch (ApiValidateException apiValidateException) {
            return new ResponseEntity<ResultBean>(new ResultBean(apiValidateException.getCode(), apiValidateException.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResultBean>(new ResultBean(ConstantStatus.STATUS_BAD_REQUEST, e.getMessage()), HttpStatus.OK);
        }
    }

}
