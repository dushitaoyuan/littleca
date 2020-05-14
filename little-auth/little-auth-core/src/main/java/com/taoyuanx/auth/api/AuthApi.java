package com.taoyuanx.auth.api;

import com.taoyuanx.auth.dto.request.AuthRefreshRequestDTO;
import com.taoyuanx.auth.dto.request.AuthRequestDTO;
import com.taoyuanx.auth.dto.response.AuthResultDTO;

/**
 * @author dushitaoyuan
 * @date 2020/5/1122:15
 * @desc: 认证接口
 */
public interface AuthApi {

    /**
     * token获取接口
     */
    AuthResultDTO auth(AuthRequestDTO authRequestDTO);

    /**
     * token续期接口
     */
    AuthResultDTO authRefresh(AuthRefreshRequestDTO authRefreshRequestDTO);

    /**
     * 时间戳接口
     */
    Long timestamp();


}
