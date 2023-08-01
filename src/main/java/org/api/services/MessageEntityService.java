package org.api.services;

import org.api.entities.MessageEntity;
import org.api.payload.ResultBean;
import org.api.payload.request.FriendMessageResponse;
import org.api.payload.response.messageResponse.MessageResponse;
import org.api.payload.response.messageResponse.MessagesBetweenTwoUserResponse;
import org.api.utils.ApiValidateException;

import java.util.List;

public interface MessageEntityService {
    public MessageResponse createMessage(String json) throws ApiValidateException, Exception;

    public MessagesBetweenTwoUserResponse getAllMessages(String loggedInUsername, String chatUserId) throws ApiValidateException, Exception;

    public List<MessageEntity> getAllFriendMessages(String loggedInUsername) throws ApiValidateException, Exception;

    public ResultBean updateStatus(String json)throws Exception;
}
