package org.api.repository;

import org.api.entities.RelationshipEntity;
import org.api.entities.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface RelationshipEntityRepository extends BaseRepository<RelationshipEntity, String> {

    public List<RelationshipEntity> findAllByUserEntityOneIdAndStatus(String id, String status);

    @Query("select r from RelationshipEntity r WHERE (r.userEntityOne.id = ?1 and r.status = ?2) or (r.userEntityTow.id = ?1 and r.status = ?2)")
    public List<RelationshipEntity> findAllByUserEntityOneIdOrUserEntityTowIdAndStatus(String idOne, String status);

    @Query("SELECT r FROM RelationshipEntity r " +
            "WHERE ((r.userEntityOne.id = :id1 AND r.userEntityTow.id = :id2)" +
            "OR (r.userEntityTow.id = :id1 AND r.userEntityOne = :id2))")
    public RelationshipEntity findRelationshipByUserOneAndUserTwo(@Param(value = "id1") String userOneId, @Param(value = "id2") String userTwoId);

}


