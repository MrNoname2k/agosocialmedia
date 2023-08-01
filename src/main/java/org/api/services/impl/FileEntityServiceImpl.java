package org.api.services.impl;

import org.api.annotation.LogExecutionTime;
import org.api.constants.ConstantMessage;
import org.api.constants.ConstantStatus;
import org.api.entities.AlbumEntity;
import org.api.entities.FileEntity;
import org.api.entities.PostEntity;
import org.api.payload.ResultBean;
import org.api.repository.FileEntityRepository;
import org.api.services.FileEntityService;
import org.api.utils.ApiValidateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@LogExecutionTime
@Service
@Transactional(rollbackFor = {ApiValidateException.class, Exception.class})
public class FileEntityServiceImpl implements FileEntityService {

    public static final String ALIAS = "File";

    @Autowired
    private FileEntityRepository fileEntityRepository;


    @Override
    public FileEntity createFile(AlbumEntity album, PostEntity post, String fileName) {
        FileEntity entity = new FileEntity();
        entity.setFileName(fileName);
        entity.setPostEntity(post);
        return fileEntityRepository.save(entity);
    }

    @Override
    public ResultBean findAllByUserAndTypeAlbum(String idUser, String typeAlbum) throws ApiValidateException, Exception {
        List<FileEntity> list = fileEntityRepository.findAllByUserIdAndAlbumType(idUser, typeAlbum);
        return new ResultBean(list, ConstantStatus.STATUS_OK, ConstantMessage.MESSAGE_OK);
    }

    @Override
    public List<String> findAllByPostEntityId(String idPost) {
        List<String> listFileName = new ArrayList<>();
        List<FileEntity> list = fileEntityRepository.findAllByPostEntityId(idPost);
        if (list.isEmpty()) return null;
        for (FileEntity file : list) {
            listFileName.add(file.getFileName());
        }
        return listFileName;
    }

    @Override
    public FileEntity findAllByPostEntityUserEntityPostIdAndAlbumEntityFileTypeAlbumAndIsCurrenAvatar(String idUser, String typeAlbum, int isCurrenAvatar) {
        Optional<FileEntity> entity = fileEntityRepository.findByPostEntityUserEntityPostIdAndAlbumEntityFileTypeAlbumAndIsCurrenAvatar(idUser, typeAlbum, isCurrenAvatar);
        if (entity.isEmpty()) return null;
        return entity.get();
    }

    @Override
    public FileEntity findAllByPostEntityUserEntityPostIdAndAlbumEntityFileTypeAlbumAndIsCurrenBanner(String idUser, String typeAlbum, int isCurrenBanner) {
        Optional<FileEntity> entity = fileEntityRepository.findByPostEntityUserEntityPostIdAndAlbumEntityFileTypeAlbumAndIsCurrenBanner(idUser, typeAlbum, isCurrenBanner);
        if (entity.isEmpty()) return null;
        return entity.get();
    }
}
