package org.api.services.impl;

import com.google.gson.JsonObject;
import org.api.annotation.LogExecutionTime;
import org.api.constants.*;
import org.api.entities.AlbumEntity;
import org.api.entities.UserEntity;
import org.api.payload.ResultBean;
import org.api.repository.AlbumEntityRepository;
import org.api.services.AlbumEntityService;
import org.api.services.AuthenticationService;
import org.api.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@LogExecutionTime
@Service
@Transactional(rollbackFor = {ApiValidateException.class, Exception.class})
public class AlbumEntityServiceImpl implements AlbumEntityService {

    public static final String ALIAS = "Album";

    @Autowired
    private AlbumEntityRepository albumEntityRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public AlbumEntity createAlbumDefault(String type, UserEntity userEntity) {
        AlbumEntity entity = new AlbumEntity();
        entity.setName(type);
        entity.setTypeAlbum(type);
        entity.setUserEntity(userEntity);
        return albumEntityRepository.save(entity);
    }

    @Override
    public AlbumEntity findOneById(String id) {
        AlbumEntity entity = albumEntityRepository.findById(id).get();
        return entity;
    }

    @Override
    public ResultBean findOneByTypeAlbumAndUserEntityId(String tpeAlbum, String idUser) throws ApiValidateException, Exception {
        AlbumEntity entity = albumEntityRepository.findOneByTypeAlbumAndUserEntityId(tpeAlbum, idUser).orElseThrow(() -> new ApiValidateException(ConstantMessage.ID_ERR00002, "album",
                MessageUtils.getMessage(ConstantMessage.ID_ERR00002, null, ItemNameUtils.getItemName("album", ALIAS))));

        return new ResultBean(entity, ConstantStatus.STATUS_OK, ConstantMessage.MESSAGE_OK);
    }

    @Override
    public ResultBean createNewAlbum(String json) throws ApiValidateException, Exception {
        AlbumEntity entity = new AlbumEntity();
        JsonObject jsonObject = DataUtil.getJsonObject(json);
        ValidateData.validate(ConstantJsonFileValidate.FILE_ALBUM_JSON_VALIDATE, jsonObject, false);
        this.convertJsonToEntity(jsonObject, entity);
        UserEntity userEntity = authenticationService.authentication();
        entity.setUserEntity(userEntity);
        entity.setTypeAlbum(ConstantTypeAlbum.OTHER);
        AlbumEntity entityOld = albumEntityRepository.save(entity);
        return new ResultBean(entityOld, ConstantStatus.STATUS_OK, ConstantMessage.MESSAGE_OK);
    }

    private void convertJsonToEntity(JsonObject json, AlbumEntity entity) throws ApiValidateException {
        if (DataUtil.hasMember(json, ConstantColumns.NAME_ALBUM)) {
            entity.setName(DataUtil.getJsonString(json, ConstantColumns.NAME_ALBUM));
        }
    }
}
