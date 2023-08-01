package org.api.repository;

import org.api.entities.CommentEntity;

import java.util.List;

public interface CommentEntityRepository extends BaseRepository<CommentEntity, String> {
    public List<CommentEntity> findByIdComment(String id);
}
