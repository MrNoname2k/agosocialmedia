package org.api.services;

import org.api.entities.NotificationEntity;
import org.api.payload.ResultBean;
import org.api.payload.WebNotification;
import org.api.payload.response.homePageResponses.NotifiHomePageResponse;
import org.api.utils.ApiValidateException;

import java.util.List;

public interface NotificationEntityService {

    public NotificationEntity create(String idUser, String idPost, String type) throws Exception, ApiValidateException;

    public void sendNotification(NotificationEntity notificationEntity) throws ApiValidateException, Exception;

    public ResultBean findAllByPostEntityUserEntityPostId(int size, String idUser) throws ApiValidateException, Exception;
    public NotifiHomePageResponse findAllByPostEntityUserEntityPostIdPage(int size, String idUser) throws ApiValidateException, Exception;
    public ResultBean updateNotificationStatus() throws ApiValidateException, Exception;

}
