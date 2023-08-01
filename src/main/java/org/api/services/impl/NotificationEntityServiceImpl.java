package org.api.services.impl;

import org.api.annotation.LogExecutionTime;
import org.api.constants.*;
import org.api.entities.NotificationEntity;
import org.api.entities.PostEntity;
import org.api.entities.RelationshipEntity;
import org.api.entities.UserEntity;
import org.api.payload.ResultBean;
import org.api.payload.WebNotification;
import org.api.payload.request.PageableRequest;
import org.api.payload.response.homePageResponses.NotificationHomeResponse;
import org.api.payload.response.homePageResponses.NotifiHomePageResponse;
import org.api.payload.response.homePageResponses.PostHomeRespon;
import org.api.payload.response.homePageResponses.UserHomeRespon;
import org.api.repository.NotificationEntityRepository;
import org.api.repository.PostEntityRepository;
import org.api.repository.UserEntityRepository;
import org.api.services.AuthenticationService;
import org.api.services.NotificationEntityService;
import org.api.services.RelationshipEntityService;
import org.api.utils.ApiValidateException;
import org.api.utils.ItemNameUtils;
import org.api.utils.MessageUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@LogExecutionTime
@Service
@Transactional(rollbackFor = {ApiValidateException.class, Exception.class})
public class NotificationEntityServiceImpl implements NotificationEntityService {

    @Autowired
    private NotificationEntityRepository notificationEntityRepository;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private PostEntityRepository postEntityRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private RelationshipEntityService relationshipEntityService;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public NotificationEntity create(String idUser, String idPost, String type) throws ApiValidateException, Exception {
        UserEntity userEntity = userEntityRepository.findOneById(idUser).orElseThrow(() -> new ApiValidateException(ConstantMessage.ID_ERR00002, ConstantColumns.USER_ID,
                MessageUtils.getMessage(ConstantMessage.ID_ERR00002, null, ItemNameUtils.getItemName(ConstantColumns.USER_ID, "Notification"))));
        PostEntity postEntity = postEntityRepository.findOneById(idPost).orElseThrow(() -> new ApiValidateException(ConstantMessage.ID_ERR00002, ConstantColumns.POST_ID,
                MessageUtils.getMessage(ConstantMessage.ID_ERR00002, null, ItemNameUtils.getItemName(ConstantColumns.POST_ID, "Notification"))));
        NotificationEntity entity = new NotificationEntity();
        entity.setUserEntity(userEntity);
        entity.setPostEntity(postEntity);
        entity.setType(type);
        entity.setStatus(ConstNotificationStatus.UNCHECKED);

        if (type.equals(ConstantNotificationType.POST_AVATAR))
            entity.setContent(MessageUtils.getMessage(ConstantMessage.ID_NOTIFICATION_001, null, new Object[]{entity.getUserEntity().getFullName(), entity.getPostEntity().getContent()}));
        else if (type.equals(ConstantNotificationType.POST_BANNER))
            entity.setContent(MessageUtils.getMessage(ConstantMessage.ID_NOTIFICATION_002, null, new Object[]{entity.getUserEntity().getFullName(), entity.getPostEntity().getContent()}));
        else if (type.equals(ConstantNotificationType.POST_CREATE))
            entity.setContent(MessageUtils.getMessage(ConstantMessage.ID_NOTIFICATION_003, null, new Object[]{entity.getUserEntity().getFullName(), entity.getPostEntity().getContent()}));
        else if (type.equals(ConstantNotificationType.LIKE))
            entity.setContent(MessageUtils.getMessage(ConstantMessage.ID_NOTIFICATION_004, null, new Object[]{entity.getUserEntity().getFullName(), entity.getPostEntity().getContent()}));
        else if (type.equals(ConstantNotificationType.COMMENT))
            entity.setContent(MessageUtils.getMessage(ConstantMessage.ID_NOTIFICATION_005, null, new Object[]{entity.getUserEntity().getFullName(), entity.getPostEntity().getContent()}));
        NotificationEntity entityOld = notificationEntityRepository.save(entity);
        return entityOld;
    }

    @Override
    public void sendNotification(NotificationEntity notificationEntity) throws ApiValidateException, Exception {
        if (notificationEntity.getType().equals(ConstantNotificationType.LIKE) || notificationEntity.getType().equals(ConstantNotificationType.COMMENT)) {
            WebNotification webNotification = new WebNotification();

            UserEntity userEntity = notificationEntity.getUserEntity();

            UserHomeRespon userHomeRespon = modelMapper.map(userEntity, UserHomeRespon.class);
            List<PostHomeRespon> avatarResponses = this.getAvatarOrBanner(userEntity.getId(), "avatar");
            List<PostHomeRespon> bannerResponses = this.getAvatarOrBanner(userEntity.getId(), "banner");
            userHomeRespon.setAvatars(avatarResponses);
            userHomeRespon.setBanners(bannerResponses);
            webNotification.setUserEntity(userHomeRespon);
            String url = "/user/" + notificationEntity.getPostEntity().getUserEntityPost().getMail() + "/notifications";
            webNotification.setContent(notificationEntity.getContent());
            webNotification.setPostEntity(modelMapper.map(notificationEntity.getPostEntity(), PostHomeRespon.class));
            messagingTemplate.convertAndSend(url, webNotification);
        } else {
            List<RelationshipEntity> listFriends = relationshipEntityService.findAllByUserEntityOneIdOrUserEntityTowAndStatus(notificationEntity.getUserEntity().getId(), ConstantRelationshipStatus.FRIEND);
            if (!listFriends.isEmpty()) {
                for (RelationshipEntity friend : listFriends) {
                    if (friend.getUserEntityOne().getId().equals(notificationEntity.getUserEntity().getId())) {
                        WebNotification webNotification = new WebNotification();
                        UserEntity userEntity = friend.getUserEntityOne();

                        UserHomeRespon userHomeRespon = modelMapper.map(userEntity, UserHomeRespon.class);
                        List<PostHomeRespon> avatarResponses = this.getAvatarOrBanner(userEntity.getId(), "avatar");
                        List<PostHomeRespon> bannerResponses = this.getAvatarOrBanner(userEntity.getId(), "banner");
                        userHomeRespon.setAvatars(avatarResponses);
                        userHomeRespon.setBanners(bannerResponses);
                        webNotification.setUserEntity(userHomeRespon);

                        String url = "/user/" + friend.getUserEntityTow().getMail() + "/notifications";
                        webNotification.setContent(notificationEntity.getContent());
                        webNotification.setPostEntity(modelMapper.map(notificationEntity.getPostEntity(), PostHomeRespon.class));
                        messagingTemplate.convertAndSend(url, webNotification);
                    } else if (friend.getUserEntityTow().getId().equals(notificationEntity.getUserEntity().getId())) {
                        WebNotification webNotification = new WebNotification();
                        UserEntity userEntity = friend.getUserEntityTow();
                        UserHomeRespon userHomeRespon = modelMapper.map(userEntity, UserHomeRespon.class);
                        List<PostHomeRespon> avatarResponses = this.getAvatarOrBanner(userEntity.getId(), "avatar");
                        List<PostHomeRespon> bannerResponses = this.getAvatarOrBanner(userEntity.getId(), "banner");
                        userHomeRespon.setAvatars(avatarResponses);
                        userHomeRespon.setBanners(bannerResponses);
                        webNotification.setUserEntity(userHomeRespon);

                        String url = "/user/" + friend.getUserEntityOne().getMail() + "/notifications";
                        webNotification.setContent(notificationEntity.getContent());
                        webNotification.setPostEntity(modelMapper.map(notificationEntity.getPostEntity(), PostHomeRespon.class));
                        messagingTemplate.convertAndSend(url, webNotification);
                    }
                }
            }
        }
    }

    @Override
    public ResultBean findAllByPostEntityUserEntityPostId(int size, String idUser) throws ApiValidateException, Exception {
        PageableRequest pageableRequest = new PageableRequest();
        pageableRequest.setSize(size);
        pageableRequest.setSort(Sort.by("id").ascending());
        pageableRequest.setPage(0);
        Page<NotificationEntity> notificationEntityPage = notificationEntityRepository.findAllByPostEntityUserEntityPostId(idUser, pageableRequest.getPageable());
        List<NotificationEntity> notificationEntities = notificationEntityPage.getContent();
        List<WebNotification> responses = notificationEntities.stream().map(notificationEntity -> modelMapper.map(notificationEntity, WebNotification.class)).collect(Collectors.toList());

        NotifiHomePageResponse pageResponse = new NotifiHomePageResponse();
        if (notificationEntityPage.hasContent()) {
            pageResponse.setResults(responses);
            pageResponse.setCurrentPage(notificationEntityPage.getNumber());
            pageResponse.setNoRecordInPage(notificationEntityPage.getSize());
            pageResponse.setTotalPage(notificationEntityPage.getTotalPages());
            pageResponse.setTotalRecords(notificationEntityPage.getTotalElements());
        }
        return new ResultBean(pageResponse, ConstantStatus.STATUS_OK, ConstantMessage.MESSAGE_OK);
    }

    @Override
    public NotifiHomePageResponse findAllByPostEntityUserEntityPostIdPage(int size, String idUser) throws ApiValidateException, Exception {
        PageableRequest pageableRequest = new PageableRequest();
        pageableRequest.setSize(size);
        pageableRequest.setSort(Sort.by("id").ascending());
        pageableRequest.setPage(0);
        Page<NotificationEntity> notificationEntityPage = notificationEntityRepository.findAllByPostEntityUserEntityPostId(idUser, pageableRequest.getPageable());
        List<NotificationEntity> notificationEntities = notificationEntityPage.getContent();
        List<WebNotification> responses = notificationEntities.stream().map(notificationEntity -> modelMapper.map(notificationEntity, WebNotification.class)).collect(Collectors.toList());
        responses.forEach(res -> {
            List<PostHomeRespon> avatars = this.getAvatarOrBanner(res.getUserEntity().getId(), "avatar");
            res.getUserEntity().setAvatars(avatars);
        });
        NotifiHomePageResponse pageResponse = new NotifiHomePageResponse();
        if (notificationEntityPage.hasContent()) {
            pageResponse.setResults(responses);
            pageResponse.setCurrentPage(notificationEntityPage.getNumber());
            pageResponse.setNoRecordInPage(notificationEntityPage.getSize());
            pageResponse.setTotalPage(notificationEntityPage.getTotalPages());
            pageResponse.setTotalRecords(notificationEntityPage.getTotalElements());
        }
        return pageResponse;
    }

    @Override
    public ResultBean updateNotificationStatus() throws ApiValidateException, Exception {
        UserEntity user = authenticationService.authentication();
        List<NotificationEntity> notificationEntities = notificationEntityRepository.findAllByUserEntity(user);

        if (notificationEntities.size() > 0) {
            for (NotificationEntity notify : notificationEntities) {
                if (notify.getStatus() != null && notify.getStatus().equals(ConstNotificationStatus.UNCHECKED)) {
                    notify.setStatus(ConstNotificationStatus.CHECKED);
                    notificationEntityRepository.save(notify);
                }
            }
        }

        List<WebNotification> responses = notificationEntities.stream().map(notify -> modelMapper.map(notify, WebNotification.class)).collect(Collectors.toList());

        responses.forEach(res -> {
            List<PostHomeRespon> avatars = this.getAvatarOrBanner(res.getUserEntity().getId(), "avatar");
            res.getUserEntity().setAvatars(avatars);
        });

        return new ResultBean(responses, ConstantStatus.STATUS_OK, ConstantMessage.MESSAGE_OK);
    }

    private List<PostHomeRespon> getAvatarOrBanner(String id, String status) {
        if (status == "avatar") {
            List<PostEntity> postAvatar = postEntityRepository.getPostByUserIdAndType(id, "avatar");
            return postAvatar.stream().map(p -> modelMapper.map(p, PostHomeRespon.class)).collect(Collectors.toList());
        } else {
            List<PostEntity> postBanner = postEntityRepository.getPostByUserIdAndType(id, "banner");
            return postBanner.stream().map(p -> modelMapper.map(p, PostHomeRespon.class)).collect(Collectors.toList());
        }
    }

}
