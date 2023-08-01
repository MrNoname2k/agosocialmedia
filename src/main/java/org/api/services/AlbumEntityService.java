package org.api.services;

import org.api.entities.AlbumEntity;
import org.api.entities.UserEntity;
import org.api.payload.ResultBean;
import org.api.utils.ApiValidateException;

import java.util.Optional;

public interface AlbumEntityService {

    public AlbumEntity createAlbumDefault(String type, UserEntity userEntity);

    public AlbumEntity findOneById(String id);

    public ResultBean findOneByTypeAlbumAndUserEntityId(String tpeAlbum, String idUser)  throws ApiValidateException, Exception;

    public ResultBean createNewAlbum(String json) throws ApiValidateException, Exception;

}
