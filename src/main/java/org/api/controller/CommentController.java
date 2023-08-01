package org.api.controller;

import org.api.annotation.LogExecutionTime;
import org.api.constants.ConstantMessage;
import org.api.constants.ConstantStatus;
import org.api.payload.ResultBean;
import org.api.services.CommentEntityService;
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
@RequestMapping(value = "/v1/api/comments/")
public class CommentController {

    private static final Logger log = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private CommentEntityService commentEntityService;

    @PostMapping(value = "/create-comment", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResultBean> createUser(@RequestBody String json) {
        try {
            ResultBean resultBean = commentEntityService.createComment(json);
            return new ResponseEntity<ResultBean>(resultBean, HttpStatus.CREATED);
        } catch (ApiValidateException ex) {
            return new ResponseEntity<ResultBean>(new ResultBean(ex.getCode(), ex.getMessage()), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<ResultBean>(new ResultBean(ConstantStatus.STATUS_BAD_REQUEST, ConstantMessage.MESSAGE_SYSTEM_ERROR), HttpStatus.OK);
        }
    }

    @DeleteMapping(value = "/delete-comment/{commentId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResultBean> deleteComment(@PathVariable("commentId") String id) {
        try {
            ResultBean resultBean = commentEntityService.deleteComment(id);
            return new ResponseEntity<ResultBean>(resultBean, HttpStatus.OK);
        }catch (ApiValidateException a) {
            return new ResponseEntity<ResultBean>(new ResultBean(a.getCode(), a.getMessage()), HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<ResultBean>(new ResultBean(ConstantStatus.STATUS_BAD_REQUEST, e.getMessage()), HttpStatus.OK);
        }
    }

    @PutMapping(value = "/update-comment", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResultBean> updateComment(@RequestBody String json) {
        try {
            ResultBean resultBean = commentEntityService.updateComment(json);
            return new ResponseEntity<ResultBean>(resultBean, HttpStatus.OK);
        }catch (ApiValidateException a) {
            return new ResponseEntity<ResultBean>(new ResultBean(a.getCode(), a.getMessage()), HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<ResultBean>(new ResultBean(ConstantStatus.STATUS_BAD_REQUEST, e.getMessage()), HttpStatus.OK);
        }
    }
}
