package org.api.services.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.api.annotation.LogExecutionTime;
import org.api.constants.*;
import org.api.entities.LikeEntity;
import org.api.entities.NotificationEntity;
import org.api.entities.PostEntity;
import org.api.entities.UserEntity;
import org.api.payload.ResultBean;
import org.api.payload.response.homePageResponses.LikeHomeResponse;
import org.api.payload.response.homePageResponses.PostHomeRespon;
import org.api.repository.LikeEntityRepository;
import org.api.services.AuthenticationService;
import org.api.services.LikeEntityService;
import org.api.services.NotificationEntityService;
import org.api.services.PostEntityService;
import org.api.utils.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@LogExecutionTime
@Service
@Transactional(rollbackFor = {ApiValidateException.class, Exception.class})
public class LikeEntityServiceImpl implements LikeEntityService {

    @Autowired
    private LikeEntityRepository likeEntityRepository;

    @Autowired
    private PostEntityService postEntityService;
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private Gson gson;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private NotificationEntityService notificationEntityService;

    @Override
    public ResultBean likeOrUnlike(String json) throws ApiValidateException, Exception {
        UserEntity userEntity = authenticationService.authentication();
        LikeEntity entityOld = null;
        LikeEntity entity = new LikeEntity();
        entity.setUserEntityLike(userEntity);
        JsonObject jsonObject = DataUtil.getJsonObject(json);
        ValidateData.validate(ConstantJsonFileValidate.FILE_LIKE_JSON_VALIDATE, jsonObject, false);
        PostEntity postEntity = gson.fromJson(jsonObject, PostEntity.class);

        Optional<LikeEntity> likeEntityOptional = likeEntityRepository.findOneByPostEntityIdAndUserEntityLikeId(postEntity.getId(), entity.getUserEntityLike().getId());
        if(likeEntityOptional.isPresent()) {
            if(likeEntityOptional.get().getDelFlg() == 1) {
                entity.setDelFlg(0);
                NotificationEntity notificationEntity = notificationEntityService.create(userEntity.getId(), postEntity.getId(), ConstantNotificationType.LIKE);
                notificationEntityService.sendNotification(notificationEntity);
            }else {
                entity.setDelFlg(1);
            }
            entity.setId(likeEntityOptional.get().getId());
            entity.setPostEntity(postEntity);
            entityOld = likeEntityRepository.save(entity);
        }else {
            entity.setPostEntity(postEntity);
            entityOld = likeEntityRepository.save(entity);
            NotificationEntity notificationEntity = notificationEntityService.create(userEntity.getId(), postEntity.getId(), ConstantNotificationType.LIKE);
            notificationEntityService.sendNotification(notificationEntity);
        }

        LikeHomeResponse respon = modelMapper.map(entityOld, LikeHomeResponse.class);

        return new ResultBean(respon, ConstantStatus.STATUS_OK, ConstantMessage.MESSAGE_OK);
    }

    private void convertJsonToEntity(JsonObject json, LikeEntity entity) throws ApiValidateException, Exception {
        if (DataUtil.hasMember(json, ConstantColumns.ID_POST)) {
            PostEntity postEntity = postEntityService.findOneById(DataUtil.getJsonString(json, ConstantColumns.ID_POST));
            entity.setPostEntity(postEntity);
        }
        if (DataUtil.hasMember(json, ConstantColumns.DEL_FLG)) {
            entity.setDelFlg(DataUtil.getJsonInteger(json, ConstantColumns.DEL_FLG));
        }
    }

}
