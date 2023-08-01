package org.api.services.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.api.annotation.LogExecutionTime;
import org.api.constants.*;
import org.api.entities.UserEntity;
import org.api.payload.ResultBean;
import org.api.payload.response.UserResponse.UserResponse;
import org.api.repository.UserEntityRepository;
import org.api.services.UserEntityService;
import org.api.utils.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@LogExecutionTime
@Service
@Transactional(rollbackFor = {ApiValidateException.class, Exception.class})
public class UserEntityServiceImpl implements UserEntityService {

    public static final String ALIAS = "User";

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private Gson gson;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public ResultBean createUser(String json) throws ApiValidateException, Exception {
        UserEntity entity = new UserEntity();
        JsonObject jsonObject = DataUtil.getJsonObject(json);
        ValidateData.validate(ConstantJsonFileValidate.FILE_USER_JSON_VALIDATE, jsonObject, false);
        entity = gson.fromJson(jsonObject, UserEntity.class);
        if (userEntityRepository.existsByMail(entity.getMail())) {
            throw new ApiValidateException(ConstantMessage.ID_ERR00003, MessageUtils.getMessage(ConstantMessage.ID_ERR00003));
        }
        UserEntity entityOld = userEntityRepository.save(entity);
        return new ResultBean(entityOld, ConstantStatus.STATUS_201, ConstantMessage.MESSAGE_OK);
    }

    @Override
    public ResultBean updateUser(String json) throws ApiValidateException, Exception {
        UserEntity entity = new UserEntity();
        JsonObject jsonObject = DataUtil.getJsonObject(json);
        ValidateData.validate(ConstantJsonFileValidate.FILE_USER_JSON_VALIDATE, jsonObject, true);
        entity = gson.fromJson(jsonObject, UserEntity.class);

        UserEntity checkingUser = userEntityRepository.findOneById(entity.getId()).orElseThrow(() -> new ApiValidateException(ConstantMessage.ID_ERR00002, ConstantColumns.USER_ID,
                MessageUtils.getMessage(ConstantMessage.ID_ERR00002, null, ItemNameUtils.getItemName(ConstantColumns.USER_ID, ALIAS))));
        Optional<UserEntity> checkingExistedMain = userEntityRepository.existsByMailAndId(entity.getMail(), entity.getId());

        if (checkingExistedMain.isPresent()) {
            throw new ApiValidateException(ConstantMessage.ID_ERR00003, MessageUtils.getMessage(ConstantMessage.ID_ERR00003));
        }

        checkingUser.setFirstName(entity.getFirstName());
        checkingUser.setLastName(entity.getLastName());
        checkingUser.setAddress(entity.getAddress());
        checkingUser.setMail(entity.getMail());
        checkingUser.setStatus(ConstUserStatus.UPDATED);
        checkingUser.setBirthDay(entity.getBirthDay());
        checkingUser.setCity(entity.getCity());
        checkingUser.setDescription(entity.getDescription());
        checkingUser.setLinkIg(entity.getLinkIg());
        checkingUser.setLinkFacebook(entity.getLinkFacebook());

        return new ResultBean(ConstantStatus.STATUS_OK, ConstantMessage.MESSAGE_OK);
    }

    @Override
    public ResultBean getById(String id) throws ApiValidateException, Exception {
        UserEntity user = userEntityRepository.findOneById(id).orElseThrow(() -> new ApiValidateException(ConstantMessage.ID_ERR00002, ConstantColumns.USER_ID,
                MessageUtils.getMessage(ConstantMessage.ID_ERR00002, null, ItemNameUtils.getItemName(ConstantColumns.USER_ID, ALIAS))));

        UserResponse response = modelMapper.map(user, UserResponse.class);
            return new ResultBean(response, ConstantStatus.STATUS_OK, ConstantMessage.MESSAGE_OK);
    }

    @Override
    public ResultBean getByMail(String json) throws ApiValidateException, Exception {
        return null;
    }

    @Override
    public ResultBean getAll() throws ApiValidateException, Exception {
        return null;
    }

    @Override
    public UserEntity updateLastLogin(String mail) throws ApiValidateException, Exception {
        UserEntity user = userEntityRepository.findOneByMail(mail).orElseThrow(() -> new ApiValidateException(ConstantMessage.ID_ERR00002, ConstantColumns.MAIL,
                MessageUtils.getMessage(ConstantMessage.ID_ERR00002, null, ItemNameUtils.getItemName(ConstantColumns.MAIL, ALIAS))));

        user.setLastLoginDate(new Date());
        UserEntity entityOld = userEntityRepository.save(user);
        return entityOld;
    }

    @Override
    public UserEntity findOneByMail(String mail) throws ApiValidateException, Exception {
        UserEntity entityOld = userEntityRepository.findOneByMail(mail).get();
        return entityOld;
    }

    @Override
    public UserEntity updateOnline(String mail, boolean online) throws ApiValidateException, Exception {
        UserEntity user = userEntityRepository.findOneByMail(mail).orElseThrow(() -> new ApiValidateException(ConstantMessage.ID_ERR00002, ConstantColumns.MAIL,
                MessageUtils.getMessage(ConstantMessage.ID_ERR00002, null, ItemNameUtils.getItemName(ConstantColumns.MAIL, ALIAS))));
            user.setOnline(online);
            UserEntity entityOld = userEntityRepository.save(user);
            return entityOld;
    }

    @Override
    public ResultBean recommendFriends(String idUser) throws ApiValidateException, Exception {
        List<UserEntity> list = userEntityRepository.recommendFriends(idUser);
        return new ResultBean(list, ConstantStatus.STATUS_OK, ConstantMessage.MESSAGE_OK);
    }
}
