package com.taoyuanx.ca.auth.service;


import com.taoyuanx.auth.dto.ApiAccountDTO;

/**
 * @author dushitaoyuan
 * @date 2020/2/17
 */
public interface ApiAccountService {

    ApiAccountDTO getByApiAccount(String apiAccount);
    ApiAccountDTO getById(Long apiId);
    void expire(String apiAccount);
}
