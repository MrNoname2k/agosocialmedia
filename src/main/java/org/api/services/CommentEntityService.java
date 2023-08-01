package org.api.services;

import org.api.payload.ResultBean;
import org.api.utils.ApiValidateException;

public interface CommentEntityService {

    public ResultBean createComment(String json) throws ApiValidateException, Exception;
    public ResultBean deleteComment(String commentId) throws ApiValidateException, Exception;

    public ResultBean updateComment(String json) throws ApiValidateException, Exception;

}
