package com.taoyuanx.ca.auth.controller;

import com.taoyuanx.auth.dto.ApiAccountDTO;
import com.taoyuanx.ca.auth.service.ApiAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author dushitaoyuan
 * @desc api账户信息获取接口(内部调用)
 * @date 2020/2/26
 */
@RestController
@RequestMapping("apiAccount")

public class ApiAccountInfoController {
    @Autowired
    ApiAccountService apiAccountService;

    @GetMapping(value = "info")
    public ApiAccountDTO info(String apiAccount, Long apiId) {
        if (Objects.nonNull(apiId)) {
            return apiAccountService.getById(apiId);
        }
        return apiAccountService.getByApiAccount(apiAccount);
    }
}
