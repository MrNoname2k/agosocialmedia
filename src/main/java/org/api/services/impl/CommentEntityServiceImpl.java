package org.api.services.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.api.annotation.LogExecutionTime;
import org.api.constants.*;
import org.api.entities.CommentEntity;
import org.api.entities.NotificationEntity;
import org.api.entities.PostEntity;
import org.api.entities.UserEntity;
import org.api.payload.ResultBean;
import org.api.repository.CommentEntityRepository;
import org.api.services.AuthenticationService;
import org.api.services.CommentEntityService;
import org.api.services.NotificationEntityService;
import org.api.services.PostEntityService;
import org.api.utils.*;
import org.checkerframework.checker.units.qual.C;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@LogExecutionTime
@Service
@Transactional(rollbackFor = {ApiValidateException.class, Exception.class})
public class CommentEntityServiceImpl implements CommentEntityService {
    @Autowired
    private CommentEntityRepository commentEntityRepository;
    @Autowired
    private PostEntityService postEntityService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private NotificationEntityService notificationEntityService;

    @Autowired
    private Gson gson;

    @Override
    public ResultBean createComment(String json) throws ApiValidateException, Exception {
        UserEntity userEntity = authenticationService.authentication();
        CommentEntity entity = new CommentEntity();
        entity.setUserEntityComment(userEntity);
        JsonObject jsonObject = DataUtil.getJsonObject(json);
        ValidateData.validate(ConstantJsonFileValidate.FILE_LIKE_JSON_VALIDATE, jsonObject, false);
        this.convertJsonToEntity(jsonObject, entity);
        CommentEntity entityOld = commentEntityRepository.save(entity);
        NotificationEntity notificationEntity = notificationEntityService.create(userEntity.getId(), entity.getPostEntity().getId(), ConstantNotificationType.COMMENT);
        notificationEntityService.sendNotification(notificationEntity);
        return new ResultBean(entityOld, ConstantStatus.STATUS_201, ConstantMessage.MESSAGE_OK);
    }

    @Override
    public ResultBean deleteComment(String commentId) throws ApiValidateException, Exception {
        CommentEntity commentEntity = null;
        if (!commentId.isEmpty()) {
            commentEntity = commentEntityRepository.findById(commentId).orElseThrow(() -> new ApiValidateException(ConstantMessage.ID_ERR00005, ConstantColumns.ID_COMMENT,
                    MessageUtils.getMessage(ConstantMessage.ID_ERR00005, null, ItemNameUtils.getItemName(ConstantColumns.ID_COMMENT, "Comment"))));

            commentEntity.setDelFlg(1);
        }

        List<CommentEntity> commentEntityList = commentEntityRepository.findByIdComment(commentEntity.getId());

        if (!commentEntityList.isEmpty()) {
            for (CommentEntity entity : commentEntityList) {
                entity.setDelFlg(1);
            }
        }
        return new ResultBean(ConstantStatus.STATUS_OK, ConstantMessage.MESSAGE_OK);
    }

    @Override
    public ResultBean updateComment(String json) throws ApiValidateException, Exception {
        CommentEntity commentEntity = new CommentEntity();

        JsonObject jsonObject = DataUtil.getJsonObject(json);
        ValidateData.validate(ConstantJsonFileValidate.FILE_LIKE_JSON_VALIDATE, jsonObject, true);
        commentEntity = gson.fromJson(jsonObject, CommentEntity.class);

        CommentEntity commentExisted = commentEntityRepository.findById(commentEntity.getId()).orElseThrow(() -> new ApiValidateException(ConstantMessage.ID_ERR00005, ConstantColumns.ID_COMMENT,
                MessageUtils.getMessage(ConstantMessage.ID_ERR00005, null, ItemNameUtils.getItemName(ConstantColumns.ID_COMMENT, "Comment"))));

        commentExisted.setContent(commentEntity.getContent());
        commentEntityRepository.save(commentExisted);
        return new ResultBean(ConstantStatus.STATUS_OK, ConstantMessage.MESSAGE_OK);
    }

    private void convertJsonToEntity(JsonObject json, CommentEntity entity) throws ApiValidateException, Exception {
        if (DataUtil.hasMember(json, ConstantColumns.CONTENT)) {
            entity.setContent(DataUtil.getJsonString(json, ConstantColumns.CONTENT));
        }
        if (DataUtil.hasMember(json, ConstantColumns.ID_COMMENT)) {
            entity.setIdComment(DataUtil.getJsonString(json, ConstantColumns.ID_COMMENT));
        }
        if (DataUtil.hasMember(json, ConstantColumns.ID_POST)) {
            PostEntity postEntity = postEntityService.findOneById(DataUtil.getJsonString(json, ConstantColumns.ID_POST));
            entity.setPostEntity(postEntity);
        }
    }

}
