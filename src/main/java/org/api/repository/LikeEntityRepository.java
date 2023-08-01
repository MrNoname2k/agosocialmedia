package org.api.repository;

import org.api.entities.LikeEntity;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LikeEntityRepository extends BaseRepository<LikeEntity, String> {

    @Query("SELECT l FROM LikeEntity l WHERE l.postEntity.id = ?1 AND l.userEntityLike.id = ?2")
    public Optional<LikeEntity> findOneByPostEntityIdAndUserEntityLikeId(String idPost, String idUser);

}
