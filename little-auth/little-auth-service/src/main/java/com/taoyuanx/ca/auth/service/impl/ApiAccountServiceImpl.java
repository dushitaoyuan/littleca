package com.taoyuanx.ca.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taoyuanx.auth.dto.ApiAccountDTO;
import com.taoyuanx.ca.auth.constants.AuthCaheConstant;
import com.taoyuanx.ca.auth.dao.ApiAccountDao;
import com.taoyuanx.ca.auth.dozer.CBeanMapper;
import com.taoyuanx.ca.auth.entity.ApiAccountEntity;
import com.taoyuanx.ca.auth.service.ApiAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @author dushitaoyuan
 * @date 2020/2/17
 */
@Service
public class ApiAccountServiceImpl implements ApiAccountService {
    @Autowired
    ApiAccountDao apiAccountDao;

    @Cacheable(cacheNames = AuthCaheConstant.API_ACCOUNT_CACHE_NAME, key = "#apiAccount", unless = "#result == null")
    @Transactional(readOnly = true)
    @Override
    public ApiAccountDTO getByApiAccount(String apiAccount) {
        ApiAccountEntity apiAccountEntity = apiAccountDao.selectOne(new LambdaQueryWrapper<ApiAccountEntity>().eq(ApiAccountEntity::getApiAccount, apiAccount));
        if (Objects.isNull(apiAccountEntity)) {
            return null;
        }
        return CBeanMapper.map(apiAccountEntity, ApiAccountDTO.class);
    }

    @Cacheable(cacheNames = AuthCaheConstant.API_ACCOUNT_CACHE_NAME, key = "#apiId", unless = "#result == null")
    @Transactional(readOnly = true)
    @Override
    public ApiAccountDTO getById(Long apiId) {
        ApiAccountEntity apiAccountEntity = apiAccountDao.selectById(apiId);
        if (Objects.isNull(apiAccountEntity)) {
            return null;
        }
        return CBeanMapper.map(apiAccountEntity, ApiAccountDTO.class);
    }

    /**
     * 缓存失效
     */
    @CacheEvict(cacheNames = AuthCaheConstant.API_ACCOUNT_CACHE_NAME, key = "#apiAccount")
    @Override
    public void expire(String apiAccount) {

    }
}
