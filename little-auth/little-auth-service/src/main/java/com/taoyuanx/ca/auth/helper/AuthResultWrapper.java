package com.taoyuanx.ca.auth.helper;

import com.taoyuanx.auth.dto.ApiAccountDTO;
import com.taoyuanx.ca.auth.dto.AuthResultDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author dushitaoyuan
 * @date 2020/5/8
 * 认证结果包装
 */
@Data
@AllArgsConstructor
public class AuthResultWrapper {
    private AuthResultDTO authResult;
    private ApiAccountDTO apiAccount;
}
