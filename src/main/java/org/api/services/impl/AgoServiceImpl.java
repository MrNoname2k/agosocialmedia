package org.api.services.impl;

import org.api.annotation.LogExecutionTime;
import org.api.constants.ConstantColumns;
import org.api.constants.ConstantMessage;
import org.api.constants.ConstantRelationshipStatus;
import org.api.constants.ConstantStatus;
import org.api.entities.PostEntity;
import org.api.entities.RelationshipEntity;
import org.api.entities.UserEntity;
import org.api.payload.ResultBean;
import org.api.payload.response.*;
import org.api.payload.response.homePageResponses.*;
import org.api.payload.response.pageResponse.MessagePageResponse;
import org.api.repository.PostEntityRepository;
import org.api.repository.RelationshipEntityRepository;
import org.api.repository.UserEntityRepository;
import org.api.services.AgoService;
import org.api.services.NotificationEntityService;
import org.api.services.PostEntityService;
import org.api.utils.ApiValidateException;
import org.api.utils.ItemNameUtils;
import org.api.utils.MessageUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@LogExecutionTime
@Service
@Transactional(rollbackFor = {ApiValidateException.class, Exception.class})
public class AgoServiceImpl implements AgoService {

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private RelationshipEntityRepository relationshipEntityRepository;

    @Autowired
    private PostEntityService postEntityService;

    @Autowired
    private PostEntityRepository postEntityRepository;

    @Autowired
    private NotificationEntityService notificationEntityService;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public ResultBean homePage(String idUser) throws ApiValidateException, Exception {
        UserEntity userEntity = userEntityRepository.findOneById(idUser).orElseThrow(() -> new ApiValidateException(ConstantMessage.ID_ERR00002, ConstantColumns.USER_ID,
                MessageUtils.getMessage(ConstantMessage.ID_ERR00002, null, ItemNameUtils.getItemName(ConstantColumns.USER_ID, "Home"))));

        List<RelationshipEntity> relationshipEntityList = relationshipEntityRepository.findAllByUserEntityOneIdOrUserEntityTowIdAndStatus(userEntity.getId(), ConstantRelationshipStatus.FRIEND);

        List<RelationshipResponse> relationshipResponses = relationshipEntityList.stream().map((relationshipEntity -> modelMapper.map(relationshipEntity, RelationshipResponse.class))).collect(Collectors.toList());

        PostHomePageResponse postEntityPage = postEntityService.findAllByUserEntityPostIdInPage(10, userEntity.getId());
        NotifiHomePageResponse notificationEntityPage = notificationEntityService.findAllByPostEntityUserEntityPostIdPage(5, userEntity.getId());

        List<PostHomeRespon> avatarResponses = this.getAvatarOrBanner(userEntity, "avatar");
        List<PostHomeRespon> bannerResponses = this.getAvatarOrBanner(userEntity, "banner");

        for(RelationshipResponse relationshipResponse: relationshipResponses) {
            relationshipResponse.getUserEntityOne().setBanners(getAvatarOrBanner(relationshipResponse.getUserEntityOne(), "banner"));            relationshipResponse.getUserEntityOne().setBanners(getAvatarOrBanner(relationshipResponse.getUserEntityOne(), "banner"));
            relationshipResponse.getUserEntityOne().setAvatars(getAvatarOrBanner(relationshipResponse.getUserEntityOne(), "avatar"));

            relationshipResponse.getUserEntityTow().setBanners(getAvatarOrBanner(relationshipResponse.getUserEntityTow(), "banner"));            relationshipResponse.getUserEntityOne().setBanners(getAvatarOrBanner(relationshipResponse.getUserEntityOne(), "banner"));
            relationshipResponse.getUserEntityTow().setAvatars(getAvatarOrBanner(relationshipResponse.getUserEntityTow(), "avatar"));
        };

        List<UserEntity> recommendFriendsList = userEntityRepository.recommendFriends(idUser);
        List<RecommendFriendsHomeResponse> recommendFriends = recommendFriendsList.stream().map((relationshipEntity -> modelMapper.map(relationshipEntity, RecommendFriendsHomeResponse.class))).collect(Collectors.toList());

        UserHomeRespon userHomeRespon = modelMapper.map(userEntity, UserHomeRespon.class);
        userHomeRespon.setAvatars(avatarResponses);
        userHomeRespon.setBanners(bannerResponses);
        HomePageResponse homePageResponse = new HomePageResponse();
        homePageResponse.setUserEntity(userHomeRespon);
        homePageResponse.setRelationshipEntities(relationshipResponses);
        homePageResponse.setPostEntityPage(postEntityPage);
        homePageResponse.setNotificationEntityPage(notificationEntityPage);
        homePageResponse.setRecommendFriends(recommendFriends);

        return new ResultBean(homePageResponse, ConstantStatus.STATUS_OK, ConstantMessage.MESSAGE_OK);
    }

    private List<PostHomeRespon> getAvatarOrBanner(UserEntity userEntity, String status) {
        if(status == "avatar") {
            List<PostEntity> postAvatar = postEntityRepository.getPostByUserAndType(userEntity, "avatar");
            return postAvatar.stream().map(p -> modelMapper.map(p, PostHomeRespon.class)).collect(Collectors.toList());
        }else {
            List<PostEntity> postBanner = postEntityRepository.getPostByUserAndType(userEntity, "banner");
            return postBanner.stream().map(p -> modelMapper.map(p, PostHomeRespon.class)).collect(Collectors.toList());
        }
    }

    private List<PostHomeRespon> getAvatarOrBanner(UserHomeRespon userEntity, String status) {
        if(status == "avatar") {
            List<PostEntity> postAvatar = postEntityRepository.getPostByUserIdAndType(userEntity.getId(), "avatar");
            return postAvatar.stream().map(p -> modelMapper.map(p, PostHomeRespon.class)).collect(Collectors.toList());
        }else {
            List<PostEntity> postBanner = postEntityRepository.getPostByUserIdAndType(userEntity.getId(), "banner");
            return postBanner.stream().map(p -> modelMapper.map(p, PostHomeRespon.class)).collect(Collectors.toList());
        }
    }
}
