package org.api.services;

import org.api.entities.AlbumEntity;
import org.api.entities.FileEntity;
import org.api.entities.PostEntity;
import org.api.payload.ResultBean;
import org.api.utils.ApiValidateException;

import java.util.List;

public interface FileEntityService {

    public FileEntity createFile(AlbumEntity album, PostEntity post, String fileName);

    public ResultBean findAllByUserAndTypeAlbum(String idUser, String typeAlbum) throws ApiValidateException, Exception;

    public List<String> findAllByPostEntityId(String idPost);

    public FileEntity findAllByPostEntityUserEntityPostIdAndAlbumEntityFileTypeAlbumAndIsCurrenAvatar(String idUser, String typeAlbum, int isCurrenAvatar);

    public FileEntity findAllByPostEntityUserEntityPostIdAndAlbumEntityFileTypeAlbumAndIsCurrenBanner(String idUser, String typeAlbum, int isCurrenBanner);

}
