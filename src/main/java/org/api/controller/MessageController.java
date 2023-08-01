package org.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.api.annotation.LogExecutionTime;
import org.api.constants.ConstantMessage;
import org.api.constants.ConstantStatus;
import org.api.entities.MessageEntity;
import org.api.entities.UserEntity;
import org.api.payload.ResultBean;
import org.api.payload.request.AllMessagesResponse;
import org.api.payload.request.FriendMessageResponse;
import org.api.payload.response.messageResponse.MessageResponse;
import org.api.payload.response.messageResponse.MessageViewAllResponse;
import org.api.payload.response.messageResponse.MessagesBetweenTwoUserResponse;
import org.api.services.MessageEntityService;
import org.api.utils.ApiValidateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@LogExecutionTime
@RestController
@RequestMapping(value = "/message")
public class MessageController {

    private static final Logger log = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private MessageEntityService messageEntityService;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @GetMapping(value = "/all/{id}")
    public ResponseEntity<ResultBean> getAllMessages(@PathVariable(value = "id") String id, Authentication principal) throws ApiValidateException, Exception {
        String userForm = principal.getName();

        MessagesBetweenTwoUserResponse messageResponses = messageEntityService.getAllMessages(userForm, id);

        return new ResponseEntity<ResultBean>(new ResultBean(messageResponses, ConstantStatus.STATUS_OK, ConstantMessage.MESSAGE_OK), HttpStatus.OK);
    }

    @GetMapping("/friend")
    public ResponseEntity<ResultBean> getALlFriendMessages(Authentication principal) throws ApiValidateException, Exception {
        String loggedInUserMail = principal.getName();

        List<MessageEntity> messageResponses = messageEntityService.getAllFriendMessages(loggedInUserMail);

        return new ResponseEntity<ResultBean>(new ResultBean(messageResponses, ConstantStatus.STATUS_OK, ConstantMessage.MESSAGE_OK), HttpStatus.OK);
    }

    @PutMapping("/update-status")
    public ResponseEntity<ResultBean> updateStatus(@RequestBody String json) throws Exception {
        ResultBean resultBean = null;

        try {
            resultBean = messageEntityService.updateStatus(json);
            return new ResponseEntity<ResultBean>(resultBean, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(new ResultBean(ConstantStatus.STATUS_BAD_REQUEST, e.getMessage()), HttpStatus.OK);
        }
    }
    @MessageMapping("/message")
    public void createMessage(@RequestBody String json) throws ApiValidateException, Exception{
        try {
            MessageResponse messageResponse = messageEntityService.createMessage(json);
            MessageViewAllResponse messageViewAllResponse = new MessageViewAllResponse();
            messageViewAllResponse.setContent(messageResponse.getContent());
            messageViewAllResponse.setStatus(messageResponse.getStatus());
            messageViewAllResponse.setFromUserId(messageResponse.getUserEntityFrom().getId());
            messageViewAllResponse.setId(messageResponse.getId());
            messageViewAllResponse.setCreateDate(messageResponse.getCreateDate());
            messageViewAllResponse.setUpdateDate(messageResponse.getUpdateDate());
            messageViewAllResponse.setDelFlg(messageResponse.getDelFlg());

            String response = objectMapper.writeValueAsString(messageViewAllResponse);
            messagingTemplate.convertAndSend("/user/" + messageResponse.getUserEntityTo().getMail() + "/messages", messageResponse);
            messagingTemplate.convertAndSend("/user/" + messageResponse.getUserEntityFrom().getMail() + "/messages", messageResponse);
        }catch(ApiValidateException a) {
            a.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
