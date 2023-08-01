package org.api.services.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.api.annotation.LogExecutionTime;
import org.api.constants.*;
import org.api.entities.MessageEntity;
import org.api.entities.PostEntity;
import org.api.entities.RelationshipEntity;
import org.api.entities.UserEntity;
import org.api.payload.ResultBean;
import org.api.payload.request.FriendMessageResponse;
import org.api.payload.request.MessageRequest;
import org.api.payload.response.RelationshipResponse;
import org.api.payload.response.homePageResponses.PostHomeRespon;
import org.api.payload.response.homePageResponses.UserHomeRespon;
import org.api.payload.response.messageResponse.MessageResponse;
import org.api.payload.response.messageResponse.MessageViewAllResponse;
import org.api.payload.response.messageResponse.MessagesBetweenTwoUserResponse;
import org.api.repository.MessageEntityRepository;
import org.api.repository.PostEntityRepository;
import org.api.repository.RelationshipEntityRepository;
import org.api.repository.UserEntityRepository;
import org.api.services.AuthenticationService;
import org.api.services.MessageEntityService;
import org.api.services.UserEntityService;
import org.api.utils.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@LogExecutionTime
@Service
@Transactional(rollbackFor = {ApiValidateException.class, Exception.class})
public class MessageEntityServiceImpl implements MessageEntityService {

    @Autowired
    private MessageEntityRepository messageEntityRepository;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private RelationshipEntityRepository relationshipEntityRepository;
    @Autowired
    private Gson gson;

    @Autowired
    private PostEntityRepository postEntityRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public MessageResponse createMessage(String json) throws ApiValidateException, Exception {
        MessageRequest messageMapping = new MessageRequest();
        JsonObject jsonObject = DataUtil.getJsonObject(json);
        ValidateData.validate(ConstantJsonFileValidate.FILE_MESSAGE_JSON_VALIDATE, jsonObject, false);
        messageMapping = gson.fromJson(jsonObject, MessageRequest.class);

        UserEntity toUser = userEntityRepository.findOneById(messageMapping.getToUserId()).orElseThrow(() -> new ApiValidateException(ConstantMessage.ID_ERR00005, ConstantColumns.USER_ID,
                MessageUtils.getMessage(ConstantMessage.ID_ERR00005, null, ItemNameUtils.getItemName(ConstantColumns.USER_ID, "Message"))));

        UserEntity fromUser = userEntityRepository.findOneById(messageMapping.getFromUserId()).orElseThrow(() -> new ApiValidateException(ConstantMessage.ID_ERR00005, ConstantColumns.USER_ID,
                MessageUtils.getMessage(ConstantMessage.ID_ERR00005, null, ItemNameUtils.getItemName(ConstantColumns.USER_ID, "Message"))));

        RelationshipEntity relationship = relationshipEntityRepository.findRelationshipByUserOneAndUserTwo(fromUser.getId(), toUser.getId());

        MessageEntity message = new MessageEntity();
        message.setContent(messageMapping.getContent());
        message.setRelationship(relationship);
        message.setUserEntityTo(toUser);
        message.setUserEntityFrom(fromUser);
        message.setStatus(ConstNotificationStatus.UNCHECKED);

        MessageEntity savedMessage = messageEntityRepository.save(message);

        if(savedMessage != null) {
            return modelMapper.map(savedMessage, MessageResponse.class);
        }

        throw new ApiValidateException(ConstantMessage.ID_ERR00004, MessageUtils.getMessage(ConstantMessage.ID_ERR00004));
    }

    @Override
    public MessagesBetweenTwoUserResponse getAllMessages(String loggedInUsername, String chatUserId) throws ApiValidateException, Exception {
        UserEntity loggedInUser = userEntityRepository.findOneByMail(loggedInUsername).orElseThrow(() -> new ApiValidateException(ConstantMessage.ID_ERR00005, ConstantColumns.USER_ID,
                MessageUtils.getMessage(ConstantMessage.ID_ERR00005, null, ItemNameUtils.getItemName(ConstantColumns.USER_ID, "Message"))));

        UserEntity chatUser = userEntityRepository.findOneById(chatUserId).orElseThrow(() -> new ApiValidateException(ConstantMessage.ID_ERR00005, ConstantColumns.USER_ID,
                MessageUtils.getMessage(ConstantMessage.ID_ERR00005, null, ItemNameUtils.getItemName(ConstantColumns.USER_ID, "Message"))));

        List<MessageEntity> allMessageBetweenTwoUsers = messageEntityRepository.findAllMessageBetweenTwoUser(loggedInUser.getId(), chatUser.getId());

        List<MessageViewAllResponse> messageResponses = allMessageBetweenTwoUsers.stream()
                .map(((messageEntity -> {
                    MessageViewAllResponse messageResponse = modelMapper.map(messageEntity, MessageViewAllResponse.class);
                    messageResponse.setFromUserId(messageEntity.getUserEntityFrom().getId());
                    return messageResponse;
                }))).collect(Collectors.toList());

        MessagesBetweenTwoUserResponse messagesBetweenTwoUserResponse = new MessagesBetweenTwoUserResponse();

        List<PostEntity> avatarEntities = postEntityRepository.getPostByUserIdAndType(chatUser.getId(), "avatar");

        List<PostHomeRespon> avatarResponses = avatarEntities.stream().map(post -> modelMapper.map(post, PostHomeRespon.class)).collect(Collectors.toList());

        messagesBetweenTwoUserResponse.setToUserId(chatUser.getId());
        messagesBetweenTwoUserResponse.setToUserFirstName(chatUser.getFirstName());
        messagesBetweenTwoUserResponse.setToUserLastName(chatUser.getLastName());
        messagesBetweenTwoUserResponse.setLogin(chatUser.getOnline());
        messagesBetweenTwoUserResponse.setMessages(messageResponses);
        messagesBetweenTwoUserResponse.setAvatar(avatarResponses);
        return messagesBetweenTwoUserResponse;
    }

    @Override
    public List<MessageEntity> getAllFriendMessages(String loggedInUsername) throws ApiValidateException, Exception {
        UserEntity loggedInUser = userEntityRepository.findOneByMail(loggedInUsername).orElseThrow(() -> new ApiValidateException(ConstantMessage.ID_ERR00005, ConstantColumns.USER_ID,
                MessageUtils.getMessage(ConstantMessage.ID_ERR00005, null, ItemNameUtils.getItemName(ConstantColumns.USER_ID, "Message"))));

        List<MessageEntity> allMessage = messageEntityRepository.getAllFriendMessages(loggedInUser.getId());

        return allMessage;
    }

    @Override
    public ResultBean updateStatus(String json) throws Exception {
        JsonObject jsonObject = DataUtil.getJsonObject(json);
        String fromUserId = DataUtil.getJsonString(jsonObject, "fromUserId");
        String toUserId = DataUtil.getJsonString(jsonObject, "toUserId");

        List<MessageEntity> messageEntityList = messageEntityRepository.findAllMessageBetweenTwoUser(fromUserId, toUserId);

        messageEntityList.forEach(messageEntity -> {
            messageEntity.setStatus(ConstNotificationStatus.CHECKED);
            messageEntityRepository.save(messageEntity);
        });

        return new ResultBean(ConstantStatus.STATUS_OK, ConstantMessage.MESSAGE_OK);
    }


}
