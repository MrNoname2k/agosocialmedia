package org.api.repository;

import org.api.entities.NotificationEntity;
import org.api.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationEntityRepository extends BaseRepository<NotificationEntity, String> {

    @Query("select n FROM NotificationEntity n where n.postEntity.userEntityPost.id = ?1 ORDER BY n.createDate DESC")
    public Page<NotificationEntity> findAllByPostEntityUserEntityPostId(String idUser, Pageable pageable);

    @Query("SELECT n FROM NotificationEntity n WHERE n.postEntity.userEntityPost = ?1 ORDER BY n.createDate DESC")
    public List<NotificationEntity> findAllByUserEntity(UserEntity user);
}
