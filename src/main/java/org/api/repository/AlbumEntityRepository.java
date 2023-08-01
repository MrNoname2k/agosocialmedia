package org.api.repository;

import org.api.entities.AlbumEntity;

import java.util.Optional;

public interface AlbumEntityRepository extends BaseRepository<AlbumEntity, String> {

    public Optional<AlbumEntity> findOneByTypeAlbumAndUserEntityId(String tpeAlbum, String idUser);

}
