package org.api.services.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.api.annotation.LogExecutionTime;
import org.api.constants.*;
import org.api.entities.*;
import org.api.payload.ResultBean;
import org.api.payload.request.PageableRequest;
import org.api.payload.response.homePageResponses.PostHomeRespon;
import org.api.payload.response.homePageResponses.PostHomePageResponse;
import org.api.repository.AlbumEntityRepository;
import org.api.repository.FileEntityRepository;
import org.api.repository.PostEntityRepository;
import org.api.repository.RelationshipEntityRepository;
import org.api.services.*;
import org.api.utils.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@LogExecutionTime
@Service
@Transactional(rollbackFor = {ApiValidateException.class, Exception.class})
public class PostEntityServiceImpl implements PostEntityService {

    public static final String ALIAS = "Post";

    @Autowired
    private PostEntityRepository postEntityRepository;

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private AlbumEntityService albumEntityService;

    @Autowired
    private FileEntityService fileEntityService;

    @Autowired
    private FileEntityRepository fileEntityRepository;

    @Autowired
    private RelationshipEntityRepository relationshipEntityRepository;

    @Autowired
    private NotificationEntityService notificationEntityService;

    @Autowired
    private AlbumEntityRepository albumEntityRepository;
    @Autowired
    private Gson gson;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ResultBean createPost(String json, MultipartFile[] files) throws ApiValidateException, Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        PostEntity entity = new PostEntity();
        JsonObject jsonObject = DataUtil.getJsonObject(json);
        ValidateData.validate(ConstantJsonFileValidate.FILE_POST_JSON_VALIDATE, jsonObject, false);
        entity = gson.fromJson(jsonObject, PostEntity.class);
        UserEntity userEntity = authenticationService.authentication();
        entity.setUserEntityPost(userEntity);
        Optional<AlbumEntity> albumEntity = albumEntityRepository.findOneByTypeAlbumAndUserEntityId(ConstantTypeAlbum.POSTS, userEntity.getId());

        if (albumEntity.isPresent()) {
            entity.setAlbumEntityPost(albumEntity.get());
        } else {
            AlbumEntity albumEntityOld = albumEntityService.createAlbumDefault(ConstantTypeAlbum.POSTS, userEntity);
            entity.setAlbumEntityPost(albumEntityOld);
        }

        PostEntity entityOld = postEntityRepository.save(entity);
        map.put(ConstantColumns.POST_ENTITY, entityOld);
        map.put(ConstantColumns.USER_ENTITY, userEntity);
        if (!DataUtil.isLengthImage(files)) {
            for (MultipartFile file : files) {
                String fileName = firebaseService.uploadImage(file, entityOld.getId(), ConstantFirebase.FIREBASE_STORAGE_USER + userEntity.getId());
                fileEntityService.createFile(entityOld.getAlbumEntityPost(), entityOld, fileName);
            }
        }
        NotificationEntity notificationEntity = notificationEntityService.create(entity.getUserEntityPost().getId(), entity.getId(), ConstantNotificationType.POST_CREATE);
        notificationEntityService.sendNotification(notificationEntity);
        return new ResultBean(map, ConstantStatus.STATUS_201, ConstantMessage.MESSAGE_OK);
    }

    @Override
    public ResultBean createAvatar(String json, MultipartFile file) throws ApiValidateException, Exception {
        PostEntity entity = new PostEntity();
        JsonObject jsonObject = DataUtil.getJsonObject(json);
        ValidateData.validate(ConstantJsonFileValidate.FILE_POST_JSON_VALIDATE, jsonObject, false);
        entity = gson.fromJson(jsonObject, PostEntity.class);
        UserEntity userEntity = authenticationService.authentication();
        entity.setUserEntityPost(userEntity);
        Optional<AlbumEntity> albumEntity = albumEntityRepository.findOneByTypeAlbumAndUserEntityId(ConstantTypeAlbum.AVATAR, userEntity.getId());
        List<FileEntity> fileEntities = new ArrayList<>();
        if (albumEntity.isPresent()) {
            entity.setAlbumEntityPost(albumEntity.get());
            FileEntity currentAvatar = fileEntityRepository.findCurrentAvatar(userEntity.getId(), ConstantTypeAlbum.AVATAR);

            currentAvatar.setIsCurrenAvatar(1);
            fileEntityRepository.save(currentAvatar);
        } else {
            AlbumEntity albumEntityOld = albumEntityService.createAlbumDefault(ConstantTypeAlbum.AVATAR, userEntity);
            entity.setAlbumEntityPost(albumEntityOld);
        }

        PostEntity entityOld = postEntityRepository.save(entity);

        if (!DataUtil.isEmptyImage(file)) {
            String fileName = firebaseService.uploadImage(file, entityOld.getId(), ConstantFirebase.FIREBASE_STORAGE_USER + userEntity.getId());
            FileEntity newFile = fileEntityService.createFile(entityOld.getAlbumEntityPost(), entityOld, fileName);
            fileEntities.add(newFile);
        }

        entityOld.setFileEntities(fileEntities);
        return new ResultBean(entityOld, ConstantStatus.STATUS_201, ConstantMessage.MESSAGE_OK);
    }

    @Override
    public ResultBean createBanner(String json, MultipartFile file) throws ApiValidateException, Exception {
        PostEntity entity = new PostEntity();
        JsonObject jsonObject = DataUtil.getJsonObject(json);
        ValidateData.validate(ConstantJsonFileValidate.FILE_POST_JSON_VALIDATE, jsonObject, false);
        entity = gson.fromJson(jsonObject, PostEntity.class);
        UserEntity userEntity = authenticationService.authentication();
        entity.setUserEntityPost(userEntity);
        Optional<AlbumEntity> albumEntity = albumEntityRepository.findOneByTypeAlbumAndUserEntityId(ConstantTypeAlbum.BANNER, userEntity.getId());
        List<FileEntity> fileEntities = new ArrayList<>();
        if (albumEntity.isPresent()) {
            entity.setAlbumEntityPost(albumEntity.get());
            FileEntity currentAvatar = fileEntityRepository.findCurrentBanner(userEntity.getId(), ConstantTypeAlbum.BANNER);

            currentAvatar.setIsCurrenBanner(1);
            fileEntityRepository.save(currentAvatar);
        } else {
            AlbumEntity albumEntityOld = albumEntityService.createAlbumDefault(ConstantTypeAlbum.BANNER, userEntity);
            entity.setAlbumEntityPost(albumEntityOld);
        }

        PostEntity entityOld = postEntityRepository.save(entity);

        if (!DataUtil.isEmptyImage(file)) {
            String fileName = firebaseService.uploadImage(file, entityOld.getId(), ConstantFirebase.FIREBASE_STORAGE_USER + userEntity.getId());
            FileEntity newFile = fileEntityService.createFile(entityOld.getAlbumEntityPost(), entityOld, fileName);
            fileEntities.add(newFile);
        }

        entityOld.setFileEntities(fileEntities);
        return new ResultBean(entityOld, ConstantStatus.STATUS_201, ConstantMessage.MESSAGE_OK);
    }

    @Override
    public PostEntity findOneById(String id) throws ApiValidateException, Exception {
        PostEntity entity = postEntityRepository.findById(id).orElseThrow(() -> new ApiValidateException(ConstantMessage.ID_ERR00002, ConstantColumns.POST_ID,
                MessageUtils.getMessage(ConstantMessage.ID_ERR00002, null, ItemNameUtils.getItemName(ConstantColumns.POST_ID, ALIAS))));
        return entity;
    }

    @Override
    public PostHomePageResponse findAllByUserEntityPostIdInPage(int size, String idUser) throws ApiValidateException, Exception {
        List<RelationshipEntity> listFriends = relationshipEntityRepository.findAllByUserEntityOneIdOrUserEntityTowIdAndStatus(idUser, ConstantRelationshipStatus.FRIEND);
        if (!listFriends.isEmpty()) {
            List<String> listIdFriend = new ArrayList<>();

            for (RelationshipEntity friend : listFriends) {
                if (friend.getUserEntityOne().getId().equals(idUser))
                    listIdFriend.add(friend.getUserEntityTow().getId());
                else if (friend.getUserEntityTow().getId().equals(idUser))
                    listIdFriend.add(friend.getUserEntityOne().getId());
            }
            PageableRequest pageableRequest = new PageableRequest();
            PostHomePageResponse pageResponse = new PostHomePageResponse();

            pageableRequest.setSize(size);
            pageableRequest.setSort(Sort.by("id").ascending());
            pageableRequest.setPage(0);
            Page<PostEntity> pagePostEntity = postEntityRepository.findAllByUserEntityPostIdIn(listIdFriend, pageableRequest.getPageable());
            List<PostEntity> postEntities = pagePostEntity.getContent();
            List<PostHomeRespon> homeRespons = postEntities.stream().map(postEntity -> modelMapper.map(postEntity, PostHomeRespon.class)).collect(Collectors.toList());

            homeRespons.stream().forEach(res -> {
                List<PostEntity> avatars = postEntityRepository.getPostByUserIdAndType(res.getUserEntityPost().getId(), "avatar");
                List<PostHomeRespon> avaRespons = avatars.stream().map(avatar -> modelMapper.map(avatar, PostHomeRespon.class)).collect(Collectors.toList());
                List<PostEntity> banners = postEntityRepository.getPostByUserIdAndType(res.getUserEntityPost().getId(), "banner");
                List<PostHomeRespon> baRespons = banners.stream().map(banner -> modelMapper.map(banner, PostHomeRespon.class)).collect(Collectors.toList());

                res.getUserEntityPost().setAvatars(avaRespons);
                res.getUserEntityPost().setBanners(baRespons);

            });
            if (pagePostEntity.hasContent()) {
                pageResponse.setResults(homeRespons);
                pageResponse.setCurrentPage(pagePostEntity.getNumber());
                pageResponse.setNoRecordInPage(pagePostEntity.getSize());
                pageResponse.setTotalPage(pagePostEntity.getTotalPages());
                pageResponse.setTotalRecords(pagePostEntity.getTotalElements());
            }
            return pageResponse;
        }
        return null;
    }

    @Override
    public ResultBean updateAvatar(String json) throws ApiValidateException, Exception {
        UserEntity userEntity = authenticationService.authentication();
        PostEntity postEntity = new PostEntity();
        JsonObject jsonObject = DataUtil.getJsonObject(json);
        ValidateData.validate(ConstantJsonFileValidate.FILE_POST_JSON_VALIDATE, jsonObject, true);
        postEntity = gson.fromJson(jsonObject, PostEntity.class);

        FileEntity currentAvatar = fileEntityRepository.findCurrentAvatar(userEntity.getId(), ConstantTypeAlbum.AVATAR);
        currentAvatar.setIsCurrenAvatar(1);
        fileEntityRepository.save(currentAvatar);

        FileEntity newImage = postEntity.getFileEntities().get(0);

        newImage.setIsCurrenAvatar(0);
        newImage.setPostEntity(postEntity);

        FileEntity savedFile = fileEntityRepository.save(newImage);
        List<FileEntity> newFile = new ArrayList<>();
        newFile.add(savedFile);
        postEntity.setFileEntities(newFile);
        return new ResultBean(postEntity, ConstantStatus.STATUS_OK, ConstantMessage.MESSAGE_OK);
    }

    @Override
    public ResultBean updateBanner(String json) throws ApiValidateException, Exception {
        UserEntity userEntity = authenticationService.authentication();
        PostEntity postEntity = new PostEntity();
        JsonObject jsonObject = DataUtil.getJsonObject(json);
        ValidateData.validate(ConstantJsonFileValidate.FILE_POST_JSON_VALIDATE, jsonObject, true);
        postEntity = gson.fromJson(jsonObject, PostEntity.class);

        FileEntity currentBanner = fileEntityRepository.findCurrentBanner(userEntity.getId(), ConstantTypeAlbum.BANNER);
        currentBanner.setIsCurrenBanner(1);
        fileEntityRepository.save(currentBanner);

        FileEntity newImage = postEntity.getFileEntities().get(0);

        newImage.setIsCurrenBanner(0);
        newImage.setPostEntity(postEntity);

        FileEntity savedFile = fileEntityRepository.save(newImage);
        List<FileEntity> newFile = new ArrayList<>();
        newFile.add(savedFile);
        postEntity.setFileEntities(newFile);
        return new ResultBean(postEntity, ConstantStatus.STATUS_OK, ConstantMessage.MESSAGE_OK);
    }
}
