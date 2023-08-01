package org.api.services;

import org.api.entities.RelationshipEntity;
import org.api.payload.ResultBean;
import org.api.payload.response.RelationshipResponse;
import org.api.utils.ApiValidateException;

import java.util.List;

public interface RelationshipEntityService {

    public ResultBean findAllByUserEntityOneIdAndStatus(String id, String status) throws ApiValidateException, Exception;

    public ResultBean friendOrUnFriend(String json, String status) throws ApiValidateException, Exception;

    public List<RelationshipEntity> findAllByUserEntityOneIdOrUserEntityTowAndStatus(String idOne, String status) throws ApiValidateException, Exception;
}
