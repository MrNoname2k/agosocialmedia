package org.api.services.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.api.annotation.LogExecutionTime;
import org.api.constants.ConstantColumns;
import org.api.constants.ConstantJsonFileValidate;
import org.api.constants.ConstantMessage;
import org.api.constants.ConstantStatus;
import org.api.entities.RelationshipEntity;
import org.api.entities.UserEntity;
import org.api.payload.ResultBean;
import org.api.repository.RelationshipEntityRepository;
import org.api.repository.UserEntityRepository;
import org.api.services.AuthenticationService;
import org.api.services.RelationshipEntityService;
import org.api.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@LogExecutionTime
@Service
@Transactional(rollbackFor = {ApiValidateException.class, Exception.class})
public class RelationshipEntityServiceImpl implements RelationshipEntityService {

    @Autowired
    private RelationshipEntityRepository relationshipEntityRepository;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private Gson gson;

    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public ResultBean findAllByUserEntityOneIdAndStatus(String id, String status) {
        List<RelationshipEntity> lists = relationshipEntityRepository.findAllByUserEntityOneIdAndStatus(id, status);
        return new ResultBean(lists, ConstantStatus.STATUS_OK, ConstantMessage.MESSAGE_OK);
    }

    @Override
    public ResultBean friendOrUnFriend(String json, String status) throws ApiValidateException, Exception {
        RelationshipEntity entity = new RelationshipEntity();
        JsonObject jsonObject = DataUtil.getJsonObject(json);
        ValidateData.validate(ConstantJsonFileValidate.FILE_RELATIONSHIP_JSON_VALIDATE, jsonObject, false);

        String userTwoId = DataUtil.getJsonString(jsonObject, ConstantColumns.ID_USER_TOW);

        UserEntity userTwo = userEntityRepository.findOneById(userTwoId).orElseThrow(() -> new ApiValidateException(ConstantMessage.ID_ERR00005, ConstantColumns.USER_ID,
                MessageUtils.getMessage(ConstantMessage.ID_ERR00005, null, ItemNameUtils.getItemName(ConstantColumns.USER_ID, "Relationship"))));

        UserEntity userOne = authenticationService.authentication();

        entity.setUserEntityOne(userOne);
        entity.setUserEntityTow(userTwo);
        entity.setStatus(status);
        RelationshipEntity entityOld = relationshipEntityRepository.save(entity);
        return new ResultBean(ConstantStatus.STATUS_OK, ConstantMessage.MESSAGE_OK);
    }

    @Override
    public List<RelationshipEntity> findAllByUserEntityOneIdOrUserEntityTowAndStatus(String idOne, String status) throws ApiValidateException, Exception{
        List<RelationshipEntity> list = relationshipEntityRepository.findAllByUserEntityOneIdOrUserEntityTowIdAndStatus(idOne, status);
        return list;
    }

    private void convertJsonToEntity(JsonObject json, RelationshipEntity entity, String status) throws ApiValidateException {
        entity.setStatus(status);
        if (DataUtil.hasMember(json, ConstantColumns.ID_USER_ONE)) {
            entity.setUserEntityOne(userEntityRepository.findOneById(DataUtil.getJsonString(json, ConstantColumns.ID_USER_ONE)).get());
        }
        if (DataUtil.hasMember(json, ConstantColumns.ID_USER_TOW)) {
            entity.setUserEntityTow(userEntityRepository.findOneById(DataUtil.getJsonString(json, ConstantColumns.ID_USER_TOW)).get());
        }
    }
}
