package org.api.services;

import org.api.payload.ResultBean;
import org.api.utils.ApiValidateException;

public interface LikeEntityService {

    public ResultBean likeOrUnlike(String json) throws ApiValidateException, Exception;

}
