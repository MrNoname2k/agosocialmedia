package org.api.services;

import org.api.payload.ResultBean;
import org.api.utils.ApiValidateException;

public interface AgoService {

    public ResultBean homePage(String idUser) throws ApiValidateException, Exception;

}
